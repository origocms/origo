package main.origo.core.helpers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import main.origo.core.Node;
import main.origo.core.ThemeRepository;
import main.origo.core.annotations.Decorates;
import main.origo.core.internal.CachedDecorator;
import main.origo.core.internal.CachedTheme;
import main.origo.core.internal.CachedThemeVariant;
import main.origo.core.internal.ReflectionInvoker;
import main.origo.core.ui.DecoratedNode;
import main.origo.core.ui.DecorationContext;
import main.origo.core.ui.Element;
import models.origo.core.EventHandler;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.api.templates.Html;
import play.mvc.Content;

import java.util.List;
import java.util.Map;

public class ThemeHelper {

    public static DecoratedNode decorate(Node node, CachedThemeVariant themeVariant) {
        DecoratedNode decoratedNode = new DecoratedNode(node.nodeId());
        setupRegions(themeVariant, decoratedNode);
        decoratedNode.themeVariant(themeVariant);
        decoratedNode.title(node.title());
        CachedTheme theme = ThemeRepository.getTheme(themeVariant.themeId);
        DecorationContext decorationContext = new DecorationContext(theme, themeVariant, node, decoratedNode);
        for (String pageRegion : node.regions()) {
            for (Element element : node.elements(pageRegion)) {
                Html decoratedContent = decorate(element, decorationContext);

                switch(pageRegion) {

                    case Node.HEAD: {
                        if (!element.isAlwaysInBody()) {
                            throw new RuntimeException("Element ["+element.getType()+"] is not allowed in the head. Tried to add "+element.toString());
                        }
                        decoratedNode.addHead(decoratedContent);
                        break;
                    }

                    case Node.TAIL: {
                        if (element.isAlwaysInHead()) {
                            throw new RuntimeException("Element ["+element.getType()+"] is not allowed in the body. Tried to add "+element.toString());
                        }
                        decoratedNode.addTail(decoratedContent);
                        break;
                    }

                    default: {
                        if (element.isAlwaysInHead()) {
                            throw new RuntimeException("Element ["+element.getType()+"] is not allowed in the body. Tried to add "+element.toString());
                        }
                        decoratedNode.add(pageRegion, decoratedContent);
                        break;
                    }
                }
            }
        }
        return decoratedNode;
    }

    /**
     * Sets all the regions in the rendered node so the template can access them without
     * nullpointer even if the region has no ui elements.
     *
     * @param themeVariant the theme variant that holds the regions available
     * @param decoratedNode the node about to rendered
     */
    private static void setupRegions(CachedThemeVariant themeVariant, DecoratedNode decoratedNode) {
        Map<String, List<Html>> regions = Maps.newHashMap();
        for (String region : themeVariant.regions) {
            regions.put(region, Lists.<Html>newArrayList());
        }
        decoratedNode.regions(regions);
    }

    public static Html decorate(Element element, DecorationContext decorationContext) {
        decorationContext.nest(element);
        Html decoratedOutput = null;

        List<CachedDecorator> decorators = ThemeRepository.getDecorators(decorationContext.themeVariant.themeId, element);

        if (!decorators.isEmpty()) {
            CachedDecorator decorator = selectDecorator(element.getClass(), decorationContext.theme, decorators);
            try {
                decoratedOutput = ReflectionInvoker.execute(decorator, element, decorationContext);
            } catch (Throwable e) {
                throw new RuntimeException("", e);
            }
        }
        if (decoratedOutput == null) {
            decoratedOutput = element.decorate(decorationContext);
            if (decoratedOutput == null) {
                Logger.error("Unable to decorate ["+element.getClass()+"]");
            }
        }
        decorationContext.unNest();
        return decoratedOutput;
    }

    public static Html decorateChildren(Element parent, DecorationContext decorationContext) {
        Html decoratedOutput = Html.empty();
        if (parent.hasChildren()) {
            decorationContext.nest(parent);
            @SuppressWarnings("unchecked") final List<Element> children = parent.getChildren();
            for (Element childElement : children) {
                decoratedOutput.$plus(decorate(childElement, decorationContext));
            }
            decorationContext.unNest();
        }
        if (parent.hasBody()) {
            decoratedOutput.$plus(parent.getBody());
        }
        return decoratedOutput;
    }

    public static CachedThemeVariant loadTheme(Node node, String fallbackTheme) {
        CachedThemeVariant themeVariant = ThemeRepository.getThemeVariant(node.themeVariant());
        if (themeVariant == null) {
            if (StringUtils.isEmpty(fallbackTheme)) {
                throw new RuntimeException("No theme set for node and no default theme variant set");
            }
            Logger.debug("Using default theme variant [" + fallbackTheme + "]");
            themeVariant = ThemeRepository.getThemeVariant(fallbackTheme);
        }
        if (themeVariant == null) {
            // TODO: Add some sort of fallback for when a theme is removed
            throw new RuntimeException("No theme selected for " + node.toString());
        }
        return themeVariant;
    }

    public static Content render(DecoratedNode decoratedNode) {
        CachedThemeVariant cachedThemeVariant = decoratedNode.themeVariant();
        return ReflectionInvoker.execute(cachedThemeVariant, decoratedNode);
    }

    public static CachedDecorator selectDecorator(Class<? extends Element> type, CachedTheme theme, List<CachedDecorator> cachedDecorators) {
        if (cachedDecorators.isEmpty()) {
            return null;
        }

        EventHandler storedEventHandlerType = EventHandler.findWithAnnotationAndWithType(Decorates.class.getName(), theme.getClass().getCanonicalName(), type.getName());
        if (storedEventHandlerType == null) {
            return setFirstDecoratorAsDefault(type, cachedDecorators);
        }
        for (CachedDecorator cachedDecorator : cachedDecorators) {
            if (storedEventHandlerType.handlerClass.equals(cachedDecorator.method.getDeclaringClass().getCanonicalName())) {
                return cachedDecorator;
            }
        }
        Logger.error("The stored EventHandler [" + storedEventHandlerType + "] is not available, resetting the value for type [" + type.getName() + "]");
        return setFirstDecoratorAsDefault(type, cachedDecorators);
    }

    private static CachedDecorator setFirstDecoratorAsDefault(Class<? extends Element> elementType, List<CachedDecorator> decorators) {
        CachedDecorator annotation = decorators.iterator().next();
        Logger.info("Setting ["+annotation.method.getDeclaringClass().getCanonicalName()+"] as default for type ["+ elementType+"]");
        EventHandler eventHandler = new EventHandler();
        eventHandler.annotation = Decorates.class.getName();
        eventHandler.withType = elementType.getName();
        eventHandler.nodeType = null;
        eventHandler.handlerClass = annotation.method.getDeclaringClass().getCanonicalName();
        eventHandler.create();
        return annotation;
    }

}
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
import main.origo.core.ui.Element;
import main.origo.core.ui.RenderedNode;
import main.origo.core.ui.RenderingContext;
import models.origo.core.EventHandler;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.api.templates.Html;
import play.mvc.Content;

import java.util.List;
import java.util.Map;

public class ThemeHelper {

    public static RenderedNode decorate(Node node, CachedThemeVariant themeVariant) {
        RenderedNode renderedNode = new RenderedNode(node.nodeId());
        setupRegions(themeVariant, renderedNode);
        renderedNode.themeVariant(themeVariant);
        renderedNode.title(node.title());
        CachedTheme theme = ThemeRepository.getTheme(themeVariant.themeId);
        RenderingContext renderingContext = new RenderingContext(theme, themeVariant, node, renderedNode);
        for (String pageRegion : node.regions()) {
            for (Element element : node.elements(pageRegion)) {
                Html decoratedContent = decorate(element, renderingContext);

                switch(pageRegion) {

                    case Node.HEAD: {
                        if (!element.isAlwaysInBody()) {
                            throw new RuntimeException("Element ["+element.getType()+"] is not allowed in the head. Tried to add "+element.toString());
                        }
                        renderedNode.addHead(decoratedContent);
                        break;
                    }

                    case Node.TAIL: {
                        if (element.isAlwaysInHead()) {
                            throw new RuntimeException("Element ["+element.getType()+"] is not allowed in the body. Tried to add "+element.toString());
                        }
                        renderedNode.addTail(decoratedContent);
                        break;
                    }

                    default: {
                        if (element.isAlwaysInHead()) {
                            throw new RuntimeException("Element ["+element.getType()+"] is not allowed in the body. Tried to add "+element.toString());
                        }
                        renderedNode.add(pageRegion, decoratedContent);
                        break;
                    }
                }
            }
        }
        return renderedNode;
    }

    /**
     * Sets all the regions in the rendered node so the template can access them without
     * nullpointer even if the region has no ui elements.
     *
     * @param themeVariant the theme variant that holds the regions available
     * @param renderedNode the node about to rendered
     */
    private static void setupRegions(CachedThemeVariant themeVariant, RenderedNode renderedNode) {
        Map<String, List<Html>> regions = Maps.newHashMap();
        for (String region : themeVariant.regions) {
            regions.put(region, Lists.<Html>newArrayList());
        }
        renderedNode.regions(regions);
    }

    public static Html decorate(Element element, RenderingContext renderingContext) {
        renderingContext.nest(element);
        Html decoratedOutput = null;

        List<CachedDecorator> decorators = ThemeRepository.getDecorators(renderingContext.themeVariant.themeId, element);

        if (!decorators.isEmpty()) {
            CachedDecorator decorator = selectDecorator(element.getClass(), renderingContext.theme, decorators);
            try {
                decoratedOutput = ReflectionInvoker.execute(decorator, element, renderingContext);
            } catch (Throwable e) {
                throw new RuntimeException("", e);
            }
        }
        if (decoratedOutput == null) {
            decoratedOutput = element.decorate(renderingContext);
            if (decoratedOutput == null) {
                Logger.error("Unable to decorate ["+element.getClass()+"]");
            }
        }
        renderingContext.unNest();
        return decoratedOutput;
    }

    public static Html decorateChildren(Element parent, RenderingContext renderingContext) {
        Html decoratedOutput = Html.empty();
        if (parent.hasChildren()) {
            renderingContext.nest(parent);
            @SuppressWarnings("unchecked") final List<Element> children = parent.getChildren();
            for (Element childElement : children) {
                decoratedOutput.$plus(decorate(childElement, renderingContext));
            }
            renderingContext.unNest();
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

    public static Content render(RenderedNode renderedNode) {
        CachedThemeVariant cachedThemeVariant = renderedNode.themeVariant();
        return ReflectionInvoker.execute(cachedThemeVariant, renderedNode);
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
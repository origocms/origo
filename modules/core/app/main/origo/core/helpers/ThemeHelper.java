package main.origo.core.helpers;

import com.google.common.collect.Sets;
import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.ThemeRepository;
import main.origo.core.annotations.Decorates;
import main.origo.core.internal.CachedDecorator;
import main.origo.core.internal.CachedTheme;
import main.origo.core.internal.CachedThemeVariant;
import main.origo.core.internal.ReflectionInvoker;
import main.origo.core.preview.PreviewEventGenerator;
import main.origo.core.ui.DecoratedNode;
import main.origo.core.ui.DecorationContext;
import main.origo.core.ui.Element;
import models.origo.core.EventHandler;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.Play;
import play.api.templates.Html;
import play.mvc.Content;

import java.util.List;
import java.util.Set;

public class ThemeHelper {

    public static DecoratedNode decorate(Node node, CachedThemeVariant themeVariant) throws ModuleException, NodeLoadException {
        DecoratedNode decoratedNode = new DecoratedNode(node.nodeId());
        decoratedNode.themeVariant(themeVariant);
        decoratedNode.title(node.title());
        CachedTheme theme = ThemeRepository.getTheme(themeVariant.themeId);

        handleRegionsInNodeMissingInTheme(themeVariant, node);
        handleRegionsInThemeMissingInNode(themeVariant, node);

        DecorationContext decorationContext = new DecorationContext(theme, themeVariant, node, decoratedNode);

        // Combine theme regions with the implicit HEAD and TAIL regions
        Set<String> regions = Sets.newHashSet(themeVariant.regions);
        regions.add(Node.HEAD);
        regions.add(Node.TAIL);

        decorateRegions(node, decoratedNode, decorationContext, regions);

        return decoratedNode;
    }

    private static void decorateRegions(Node node, DecoratedNode decoratedNode, DecorationContext decorationContext, Set<String> regions) {
        for (String pageRegion : regions) {
            List<Element> elements = node.elements(pageRegion);
            if (elements != null) {
                decorateElements(elements, pageRegion, decoratedNode, decorationContext);
            }
        }
    }

    private static void decorateElements(List<Element> elements, String pageRegion, DecoratedNode decoratedNode, DecorationContext decorationContext) {
        for (Element element : elements) {
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
        if (parent.hasBody()) {
            decoratedOutput.$plus$eq(parent.getBody());
        }
        if (parent.hasChildren()) {
            decorationContext.nest(parent);
            @SuppressWarnings("unchecked") final List<Element> children = parent.getChildren();
            for (Element childElement : children) {
                decoratedOutput.$plus$eq(decorate(childElement, decorationContext));
            }
            decorationContext.unNest();
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

    private static void handleRegionsInNodeMissingInTheme(CachedThemeVariant theme, Node node) throws NodeLoadException, ModuleException {
        Set<String> missingRegions = Sets.newHashSet();
        for (String region : node.regions()) {
            switch(region) {

                // Implicit regions always present
                case Node.HEAD:
                case Node.TAIL: {
                    break;
                }
                default: {
                    if (!theme.regions.contains(region)) {
                        missingRegions.add(region);
                    }
                }
            }
        }

        if (!missingRegions.isEmpty()) {
            if (Play.isProd() && PreviewEventGenerator.getValidTicket() == null) {
                if (!missingRegions.isEmpty()) {
                    for (String region : missingRegions) {
                        Logger.warn("Your node has elements for a region named '" + region + "' but the theme has no matching region.");
                    }
                }
                return;
            }

            Element wrapper = new Element.Alert(Element.Alert.Type.ERROR).setWeight(10000);
            for (String region : missingRegions) {
                wrapper.addChild(new Element.Paragraph().setBody("Your node has elements for a region named '" + region + "' but the theme has no matching region."));
            }
            node.addElement(wrapper);
        }

    }

    private static void handleRegionsInThemeMissingInNode(CachedThemeVariant theme, Node node) throws NodeLoadException, ModuleException {
        Set<String> missingRegions = Sets.newHashSet();
        for (String region : theme.regions) {
            if (node.elements(region) == null) {
                missingRegions.add(region);
            }
        }

        if (!missingRegions.isEmpty()) {
            if (Play.isProd() && PreviewEventGenerator.getValidTicket() == null) {
                if (!missingRegions.isEmpty()) {
                    for (String region : missingRegions) {
                        Logger.warn("Your theme has a region named '"+region+"' but your node has no elements for that region.");
                    }
                }
                return;
            }

            Element wrapper = new Element.Alert(Element.Alert.Type.ERROR).setWeight(10000);
            for (String region : missingRegions) {
                wrapper.addChild(new Element.Paragraph().setWeight(10000).setBody("Your theme has a region named '"+region+"' but your node has no elements for that region."));
            }
            node.addElement(wrapper);
        }
    }

}
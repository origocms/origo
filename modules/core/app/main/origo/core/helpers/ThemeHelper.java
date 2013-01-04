package main.origo.core.helpers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import main.origo.core.Node;
import main.origo.core.ThemeRepository;
import main.origo.core.annotations.Decorates;
import main.origo.core.annotations.ThemeVariant;
import main.origo.core.internal.CachedDecorator;
import main.origo.core.internal.CachedThemeVariant;
import main.origo.core.ui.Element;
import main.origo.core.ui.RenderedNode;
import main.origo.core.ui.RenderingContext;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.api.templates.Html;
import play.api.templates.HtmlFormat;
import play.mvc.Result;

import java.util.List;
import java.util.Map;

public class ThemeHelper {

    public static RenderedNode decorate(Node node, CachedThemeVariant themeVariant) {
        RenderedNode renderedNode = new RenderedNode(node.getNodeId());
        setupRegions(themeVariant, renderedNode);
        renderedNode.template(themeVariant);
        renderedNode.title(node.getTitle());
        RenderingContext renderingContext = new RenderingContext(themeVariant, node, renderedNode);
        for (String pageRegion : node.getRegions()) {
            for (Element element : node.getElements(pageRegion)) {
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
     * nullpointer even if the page has no ui elements.
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
            CachedDecorator decorator = selectDecorator(element.getClass(), decorators);
            try {
                decoratedOutput = (Html) decorator.method.invoke(null, new Decorates.Context(element, renderingContext));
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
        Html decoratedOutput;
        if (parent.hasBody()) {
            decoratedOutput = parent.getBody();
        } else {
            decoratedOutput = HtmlFormat.raw("");
        }
        if (parent.hasChildren()) {
            renderingContext.nest(parent);
            for (Element childElement : parent.getChildren()) {
                decoratedOutput.$plus(decorate(childElement, renderingContext));
            }
            renderingContext.unNest();
        }
        return decoratedOutput;
    }

    public static CachedThemeVariant loadTheme(Node node, String fallbackTheme) {
        CachedThemeVariant themeVariant = ThemeRepository.getThemeVariant(node.getThemeVariant());
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

    public static Result render(RenderedNode renderedNode) {
        CachedThemeVariant cachedThemeVariant = renderedNode.template();
        try {
            return (Result) cachedThemeVariant.templateMethod.invoke(null, new ThemeVariant.Context(renderedNode));
        } catch (Throwable e) {
            throw new RuntimeException("", e);
        }
    }

    public static CachedDecorator selectDecorator(Class<? extends Element> type, List<CachedDecorator> cachedDecorators) {
        if (cachedDecorators.isEmpty()) {
            return null;
        }

        if (cachedDecorators.size() == 1) {
            return setFirstDecoratorAsDefault(type, cachedDecorators);
        }

        String storedEventHandlerType = CoreSettingsHelper.getDecorator(type);
        if (StringUtils.isBlank(storedEventHandlerType)) {
            return setFirstDecoratorAsDefault(type, cachedDecorators);
        }
        for (CachedDecorator cachedDecorator : cachedDecorators) {
            if (storedEventHandlerType.equals(cachedDecorator.method.getDeclaringClass().getName())) {
                return cachedDecorator;
            }
        }
        Logger.error("The stored EventHandler [" + storedEventHandlerType + "] is not available, resetting the value for type [" + type.getName() + "]");
        return setFirstDecoratorAsDefault(type, cachedDecorators);
    }

    private static CachedDecorator setFirstDecoratorAsDefault(Class<? extends Element> type, List<CachedDecorator> decorators) {
        CachedDecorator annotation = decorators.iterator().next();
        Logger.info("Setting ["+annotation.method.getDeclaringClass().getName()+"] as default for type ["+type.getName()+"]");
        CoreSettingsHelper.setDecorator(type, annotation.method.getDeclaringClass());
        return annotation;
    }

}
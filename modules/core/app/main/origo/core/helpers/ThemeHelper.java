package main.origo.core.helpers;

import com.google.common.collect.Maps;
import main.origo.core.CachedDecorator;
import main.origo.core.CachedThemeVariant;
import main.origo.core.Node;
import main.origo.core.Themes;
import main.origo.core.annotations.Decorates;
import main.origo.core.annotations.ThemeVariant;
import main.origo.core.ui.RenderedNode;
import main.origo.core.ui.RenderingContext;
import main.origo.core.ui.UIElement;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.api.templates.Html;
import play.mvc.Result;

import java.util.HashMap;
import java.util.Map;

public class ThemeHelper {

    public static RenderedNode decorate(Node node, CachedThemeVariant themeVariant) {
        RenderedNode renderedNode = new RenderedNode(node.getNodeId());
        setupRegions(themeVariant, renderedNode);
        renderedNode.setTemplate(themeVariant);
        renderedNode.setTitle(node.getTitle());
        RenderingContext renderingContext = new RenderingContext(themeVariant, node);
        for (String region : node.getRegions()) {
            for (UIElement uiElement : node.getUIElements(region)) {
                Html decoratedContent = decorate(uiElement, renderingContext);
                switch(uiElement.getType()) {
                    case UIElement.META: {
                        if (Node.HEAD.equalsIgnoreCase(region)) {
                            renderedNode.addMeta(decoratedContent);
                        } else {
                            throw new RuntimeException("META is not allowed outside of head");
                        }
                    }
                    case UIElement.LINK: {
                        if (Node.HEAD.equalsIgnoreCase(region)) {
                            renderedNode.addLink(decoratedContent);
                        } else {
                            throw new RuntimeException("LINK is not allowed outside of head");
                        }
                    }
                    case UIElement.SCRIPT: {
                        if (Node.HEAD.equalsIgnoreCase(region)) {
                            renderedNode.addScript(decoratedContent);
                        } else {
                            renderedNode.add(region, decoratedContent);
                        }
                    }
                    case UIElement.STYLE: {
                        if (Node.HEAD.equalsIgnoreCase(region)) {
                            renderedNode.addStyle(decoratedContent);
                        } else {
                            renderedNode.add(region, decoratedContent);
                        }
                    }
                    default:
                        renderedNode.add(region, decoratedContent);
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
        Map<String, Html> regions = Maps.newHashMap();
        for (String region : themeVariant.regions) {
            regions.put(region, new Html(""));
        }
        renderedNode.setRegions(regions);
    }

    public static Html decorate(UIElement uiElement, RenderingContext renderingContext) {
        Map<String, CachedDecorator> decorators = Themes.getDecoratorsForTheme(renderingContext.getThemeVariant().themeId);
        renderingContext.nest(uiElement);
        Html decoratedOutput = null;
        if (decorators.containsKey(uiElement.getType())) {
            CachedDecorator decorator = decorators.get(uiElement.getType());
            try {
                decoratedOutput = (Html)decorator.method.invoke(null, new Decorates.Context(uiElement, renderingContext));
            } catch (Throwable e) {
                throw new RuntimeException("", e);
            }

        }
        if (decoratedOutput == null) {
            decoratedOutput = DefaultDecorator.decorate(uiElement, renderingContext);
        }
        renderingContext.unNest();
        return decoratedOutput;
    }

    public static String decorateChildren(UIElement parent, RenderingContext renderingContext) {
        if (!parent.hasChildren()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        renderingContext.nest(parent);
        for (UIElement childElement : parent.getChildren()) {
            sb.append(decorate(childElement, renderingContext));
        }
        renderingContext.unNest();
        return sb.toString();
    }

    public static CachedThemeVariant loadTheme(Node node) {
        CachedThemeVariant themeVariant = Themes.getThemeVariant(node.getThemeVariant());
        if (themeVariant == null) {
            String themeVariantId = SettingsHelper.Core.getThemeVariant();
            if (StringUtils.isEmpty(themeVariantId)) {
                throw new RuntimeException("No theme set for node and no default theme variant set");
            }
            Logger.debug("Using default theme variant [" + themeVariantId + "]");
            themeVariant = Themes.getThemeVariant(themeVariantId);
        }
        if (themeVariant == null) {
            // TODO: Add some sort of fallback for when a theme is removed
            throw new RuntimeException("No theme selected for " + node.toString());
        }
        return themeVariant;
    }

    public static Result render(RenderedNode renderedNode) {
        CachedThemeVariant cachedThemeVariant = renderedNode.getTemplate();
        try {
            return (Result) cachedThemeVariant.templateMethod.invoke(null, new ThemeVariant.Context(renderedNode));
        } catch (Throwable e) {
            throw new RuntimeException("", e);
        }
    }
}
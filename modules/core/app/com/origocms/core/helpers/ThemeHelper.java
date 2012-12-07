package com.origocms.core.helpers;

import com.origocms.core.CachedDecorator;
import com.origocms.core.CachedThemeVariant;
import com.origocms.core.Node;
import com.origocms.core.Themes;
import com.origocms.core.ui.RenderedNode;
import com.origocms.core.ui.RenderingContext;
import com.origocms.core.ui.UIElement;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
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
                String decoratedContent = decorate(uiElement, renderingContext);
                if (UIElement.META.equalsIgnoreCase(uiElement.getType())) {
                    if (Node.HEAD.equalsIgnoreCase(region)) {
                        renderedNode.addMeta(decoratedContent);
                    } else {
                        throw new RuntimeException("META is not allowed outside of head");
                    }
                } else if (UIElement.LINK.equalsIgnoreCase(uiElement.getType())) {
                    if (Node.HEAD.equalsIgnoreCase(region)) {
                        renderedNode.addLink(decoratedContent);
                    } else {
                        throw new RuntimeException("LINK is not allowed outside of head");
                    }
                } else if (UIElement.SCRIPT.equalsIgnoreCase(uiElement.getType())) {
                    if (Node.HEAD.equalsIgnoreCase(region)) {
                        renderedNode.addScript(decoratedContent);
                    } else {
                        renderedNode.add(region, decoratedContent);
                    }
                } else if (UIElement.STYLE.equalsIgnoreCase(uiElement.getType())) {
                    if (Node.HEAD.equalsIgnoreCase(region)) {
                        renderedNode.addStyle(decoratedContent);
                    } else {
                        renderedNode.add(region, decoratedContent);
                    }
                } else {
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
        Map<String, String> regions = new HashMap<String, String>();
        for (String region : themeVariant.regions) {
            regions.put(region, "");
        }
        renderedNode.setRegions(regions);
    }

    public static String decorate(UIElement uiElement, RenderingContext renderingContext) {
        Map<String, CachedDecorator> decorators = Themes.getDecoratorsForTheme(renderingContext.getThemeVariant().themeId);
        renderingContext.nestle(uiElement);
        String decoratedOutput = null;
        if (decorators.containsKey(uiElement.getType())) {
            CachedDecorator decorator = decorators.get(uiElement.getType());
            decoratedOutput = ReflectionHelper.invokeDecorator(decorator, null);
        }
        if (decoratedOutput == null) {
            decoratedOutput = DefaultDecorator.decorate(uiElement, renderingContext);
        }
        renderingContext.unNestle();
        return decoratedOutput;
    }

    public static String decorateChildren(UIElement parent, RenderingContext renderingContext) {
        if (!parent.hasChildren()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        renderingContext.nestle(parent);
        for (UIElement childElement : parent.getChildren()) {
            sb.append(decorate(childElement, renderingContext));
        }
        renderingContext.unNestle();
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
        return ReflectionHelper.invokeTemplate(renderedNode);
    }
}
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
        RenderingContext renderingContext = new RenderingContext(themeVariant, node);
        for (String pageRegion : node.getRegions()) {
            for (Element element : node.getUIElements(pageRegion)) {
                Html decoratedContent = decorate(element, renderingContext);

                switch(pageRegion) {

                    case Node.HEAD: {
                        if (!element.isAlwaysInBody()) {
                            throw new RuntimeException("Element ["+element.getType()+"] is not allowed in the head");
                        }
                        renderedNode.addHead(decoratedContent);
                    }

                    case Node.TAIL: {
                        if (element.isAlwaysInHead()) {
                            throw new RuntimeException("Element ["+element.getType()+"] is not allowed in the body");
                        }
                        renderedNode.addTail(decoratedContent);

                    }

                    default: {
                        if (element.isAlwaysInHead()) {
                            throw new RuntimeException("Element ["+element.getType()+"] is not allowed in the body");
                        }
                        renderedNode.add(pageRegion, decoratedContent);
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
        Map<Class<? extends Element>, CachedDecorator> decorators = ThemeRepository.getDecoratorsForTheme(renderingContext.getThemeVariant().themeId);
        renderingContext.nest(element);
        Html decoratedOutput = null;
        if (decorators.containsKey(element.getClass())) {
            CachedDecorator decorator = decorators.get(element.getClass());
            try {
                decoratedOutput = (Html) decorator.method.invoke(null, new Decorates.Context(element, renderingContext));
            } catch (Throwable e) {
                throw new RuntimeException("", e);
            }
        }
        if (decoratedOutput == null) {
            decoratedOutput = element.decorate(renderingContext);
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

    public static CachedThemeVariant loadTheme(Node node) {
        CachedThemeVariant themeVariant = ThemeRepository.getThemeVariant(node.getThemeVariant());
        if (themeVariant == null) {
            String themeVariantId = SettingsCoreHelper.getThemeVariant();
            if (StringUtils.isEmpty(themeVariantId)) {
                throw new RuntimeException("No theme set for node and no default theme variant set");
            }
            Logger.debug("Using default theme variant [" + themeVariantId + "]");
            themeVariant = ThemeRepository.getThemeVariant(themeVariantId);
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

}
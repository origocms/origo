package main.origo.themes.bootstrap;

import main.origo.core.annotations.Decorates;
import main.origo.core.annotations.Theme;
import main.origo.core.annotations.ThemeVariant;
import main.origo.core.ui.Element;
import main.origo.core.ui.RenderedNode;
import main.origo.core.ui.RenderingContext;
import play.api.templates.Html;
import play.mvc.Content;
import views.html.origo.themes.bootstrap.variant_main_and_left_columns;
import views.html.origo.themes.bootstrap.variant_main_only;
import views.html.origo.themes.bootstrap.variant_three_columns;

@Theme(id = main.origo.themes.bootstrap.BootstrapTheme.ID)
public class BootstrapTheme {

    public static final String ID = "bootstrap";

    @ThemeVariant(id = "bootstrap-main_only", regions = {"main"})
    public static Content renderDefaultMainOnlyTemplate(RenderedNode node) {
        return variant_main_only.render(node);
    }

    @ThemeVariant(id = "bootstrap-main_and_left_columns", regions = {"main", "left"})
    public static Content renderDefaultMainAndLeftColumnTemplate(RenderedNode node) {
        return variant_main_and_left_columns.render(node);
    }

    @ThemeVariant(id = "bootstrap-three_columns", regions = {"main", "left", "right"})
    public static Content renderDefaultThreeColumnTemplate(RenderedNode node) {
        return variant_three_columns.render(node);
    }

    @Decorates(types = {Element.InputSubmit.class, Element.InputButton.class, Element.InputReset.class})
    public static Html decorateButton(Element element, RenderingContext renderingContext) {
        element.addAttribute("class", "btn");
        return element.decorate(renderingContext);
    }

    @Decorates(types = {Element.Error.class})
    public static Html decorateEmphasisError(Element element, RenderingContext renderingContext) {
        element.addAttribute("class", "text-error");
        return element.decorate(renderingContext);
    }

    @Decorates(types = {Element.Warning.class})
    public static Html decorateEmphasisWarning(Element element, RenderingContext renderingContext) {
        element.addAttribute("class", "text-warning");
        return element.decorate(renderingContext);
    }

    @Decorates(types = {Element.Info.class})
    public static Html decorateEmphasisInfo(Element element, RenderingContext renderingContext) {
        element.addAttribute("class", "text-info");
        return element.decorate(renderingContext);
    }

    @Decorates(types = {Element.Success.class})
    public static Html decorateEmphasisSuccess(Element element, RenderingContext renderingContext) {
        element.addAttribute("class", "text-success");
        return element.decorate(renderingContext);
    }

    @Decorates(types = {Element.Field.class})
    public static Html decorateField(Element element, RenderingContext renderingContext) {
        element.addAttribute("class", "control-group");
        return element.decorate(renderingContext);
    }

    @Decorates(types = {Element.Help.class})
    public static Html decorateHelp(Element element, RenderingContext renderingContext) {
        element.addAttribute("class", "help-inline");
        return element.decorate(renderingContext);
    }

}

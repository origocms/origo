package main.origo.themes.bootstrap;

import main.origo.core.annotations.Decorates;
import main.origo.core.annotations.Theme;
import main.origo.core.annotations.ThemeVariant;
import main.origo.core.ui.Element;
import main.origo.core.ui.RenderedNode;
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
    public static Html decorateButton(Decorates.Context context) {
        context.element.addAttribute("class", "btn");
        return context.element.decorate(context.renderingContext);
    }

    @Decorates(types = {Element.Error.class})
    public static Html decorateEmphasisError(Decorates.Context context) {
        context.element.addAttribute("class", "text-error");
        return context.element.decorate(context.renderingContext);
    }

    @Decorates(types = {Element.Warning.class})
    public static Html decorateEmphasisWarning(Decorates.Context context) {
        context.element.addAttribute("class", "text-warning");
        return context.element.decorate(context.renderingContext);
    }

    @Decorates(types = {Element.Info.class})
    public static Html decorateEmphasisInfo(Decorates.Context context) {
        context.element.addAttribute("class", "text-info");
        return context.element.decorate(context.renderingContext);
    }

    @Decorates(types = {Element.Success.class})
    public static Html decorateEmphasisSuccess(Decorates.Context context) {
        context.element.addAttribute("class", "text-success");
        return context.element.decorate(context.renderingContext);
    }

    @Decorates(types = {Element.Field.class})
    public static Html decorateField(Decorates.Context context) {
        context.element.addAttribute("class", "control-group");
        return context.element.decorate(context.renderingContext);
    }

    @Decorates(types = {Element.Help.class})
    public static Html decorateHelp(Decorates.Context context) {
        context.element.addAttribute("class", "help-inline");
        return context.element.decorate(context.renderingContext);
    }

}

package main.origo.themes.bootstrap3;

import main.origo.core.annotations.Decorates;
import main.origo.core.annotations.Theme;
import main.origo.core.annotations.ThemeVariant;
import main.origo.core.ui.DecoratedNode;
import main.origo.core.ui.DecorationContext;
import main.origo.core.ui.Element;
import play.api.templates.Html;
import play.mvc.Content;
import views.html.origo.themes.bootstrap3.variant_main_and_left_columns;
import views.html.origo.themes.bootstrap3.variant_main_only;
import views.html.origo.themes.bootstrap3.variant_three_columns;

@Theme(id = Bootstrap3Theme.ID)
public class Bootstrap3Theme {

    public static final String ID = "origo.themes.bootstrap3";

    @ThemeVariant(id = "bootstrap3-main_only", regions = {"main"})
    public static Content renderDefaultMainOnlyTemplate(DecoratedNode node) {
        return variant_main_only.render(node);
    }

    @ThemeVariant(id = "bootstrap3-main_and_left_columns", regions = {"main", "left"})
    public static Content renderDefaultMainAndLeftColumnTemplate(DecoratedNode node) {
        return variant_main_and_left_columns.render(node);
    }

    @ThemeVariant(id = "bootstrap3-three_columns", regions = {"main", "left", "right"})
    public static Content renderDefaultThreeColumnTemplate(DecoratedNode node) {
        return variant_three_columns.render(node);
    }

    @Decorates(types = {Element.ErrorEmphasis.class})
    public static Html decorateEmphasisError(Element element, DecorationContext decorationContext) {
        element.addAttribute("class", "text-error");
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Element.WarningEmphasis.class})
    public static Html decorateEmphasisWarning(Element element, DecorationContext decorationContext) {
        element.addAttribute("class", "text-warning");
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Element.InfoEmphasis.class})
    public static Html decorateEmphasisInfo(Element element, DecorationContext decorationContext) {
        element.addAttribute("class", "text-info");
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Element.SuccessEmphasis.class})
    public static Html decorateEmphasisSuccess(Element element, DecorationContext decorationContext) {
        element.addAttribute("class", "text-success");
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Element.PrimaryEmphasis.class})
    public static Html decorateEmphasisPrimary(Element element, DecorationContext decorationContext) {
        element.addAttribute("class", "text-primary");
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Element.MutedEmphasis.class})
    public static Html decorateEmphasisMuted(Element element, DecorationContext decorationContext) {
        element.addAttribute("class", "text-muted");
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Element.ErrorAlert.class})
    public static Html decorateAlertError(Element element, DecorationContext decorationContext) {
        element.addAttribute("class", "alert alert-danger");
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Element.WarningAlert.class})
    public static Html decorateAlertWarning(Element element, DecorationContext decorationContext) {
        element.addAttribute("class", "alert alert-warning");
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Element.InfoAlert.class})
    public static Html decorateAlertInfo(Element element, DecorationContext decorationContext) {
        element.addAttribute("class", "alert alert-info");
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Element.SuccessAlert.class})
    public static Html decorateAlertSuccess(Element element, DecorationContext decorationContext) {
        element.addAttribute("class", "alert alert-success");
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Element.Form.class})
    public static Html decorateForm(Element element, DecorationContext decorationContext) {
        element.addAttribute("role", "form");
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Element.Field.class})
    public static Html decorateField(Element element, DecorationContext decorationContext) {
        element.addAttribute("class", "form-group");
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Element.Help.class})
    public static Html decorateHelp(Element element, DecorationContext decorationContext) {
        element.addAttribute("class", "help-inline");
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Element.InputButton.class,Element.InputReset.class})
    public static Html decorateButton(Element element, DecorationContext decorationContext) {
        element.attributes.put("class", "btn btn-default");
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Element.InputSubmit.class})
    public static Html decorateSubmit(Element element, DecorationContext decorationContext) {
        element.attributes.put("class", "btn btn-default btn-primary");
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Element.InputText.class, Element.InputSelect.class, Element.InputPassword.class, Element.InputTextArea.class})
    public static Html decorateFormControls(Element element, DecorationContext decorationContext) {
        element.attributes.put("class", "form-control");
        return element.decorate(decorationContext);
    }
}

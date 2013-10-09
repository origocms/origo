package main.origo.themes.bootstrap2;

import main.origo.core.annotations.Decorates;
import main.origo.core.annotations.Theme;
import main.origo.core.annotations.ThemeVariant;
import main.origo.core.ui.DecoratedNode;
import main.origo.core.ui.DecorationContext;
import main.origo.core.ui.Element;
import play.api.templates.Html;
import play.mvc.Content;
import views.html.origo.themes.bootstrap2.variant_main_and_left_columns;
import views.html.origo.themes.bootstrap2.variant_main_only;
import views.html.origo.themes.bootstrap2.variant_three_columns;

@Theme(id = Bootstrap2Theme.ID)
public class Bootstrap2Theme {

    public static final String ID = "origo.themes.bootstrap2";

    @ThemeVariant(id = "bootstrap2-main_only", regions = {"main"})
    public static Content renderDefaultMainOnlyTemplate(DecoratedNode node) {
        return variant_main_only.render(node);
    }

    @ThemeVariant(id = "bootstrap2-main_and_left_columns", regions = {"main", "left"})
    public static Content renderDefaultMainAndLeftColumnTemplate(DecoratedNode node) {
        return variant_main_and_left_columns.render(node);
    }

    @ThemeVariant(id = "bootstrap2-three_columns", regions = {"main", "left", "right"})
    public static Content renderDefaultThreeColumnTemplate(DecoratedNode node) {
        return variant_three_columns.render(node);
    }

    @Decorates(types = {Element.Emphasis.class})
    public static Html decorateEmphasisWarning(Element.Emphasis element, DecorationContext decorationContext) {
        switch(element.type) {
            case ERROR:
                element.addAttribute("class", "text-error");
                break;
            case WARNING:
                element.addAttribute("class", "text-warning");
                break;
            case INFO:
                element.addAttribute("class", "text-info");
                break;
            case MUTED:
                element.addAttribute("class", "muted");
                break;
            case PRIMARY:
                element.addAttribute("class", "text-info");
                break;
            case SUCCESS:
                element.addAttribute("class", "text-success");
                break;
            case ITALICS:
            case SMALL:
            case BOLD:
                break;
        }
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Element.Alert.class})
    public static Html decorateAlertError(Element.Alert element, DecorationContext decorationContext) {
        switch(element.type) {
            case ERROR:
                element.addAttribute("class", "alert alert-error");
                break;
            case WARNING:
                element.addAttribute("class", "alert alert-warning");
                break;
            case INFO:
                element.addAttribute("class", "alert alert-info");
                break;
            case SUCCESS:
                element.addAttribute("class", "alert alert-success");
                break;
        }
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Element.Field.class})
    public static Html decorateField(Element element, DecorationContext decorationContext) {
        element.addAttribute("class", "control-group");
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Element.Help.class})
    public static Html decorateHelp(Element element, DecorationContext decorationContext) {
        element.addAttribute("class", "help-inline");
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Element.InputButton.class,Element.InputReset.class})
    public static Html decorateButton(Element element, DecorationContext decorationContext) {
        element.addAttribute("class", "btn");
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Element.InputSubmit.class})
    public static Html decorateSubmit(Element element, DecorationContext decorationContext) {
        element.addAttribute("class", "btn btn-primary");
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Element.AnchorButton.class})
    public static Html decorateAnchorButton(Element element, DecorationContext decorationContext) {
        element.addAttribute("class", "btn");
        return element.decorate(decorationContext);
    }

}

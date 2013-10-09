package main.origo.admin.themes;

import main.origo.admin.annotations.Admin;
import main.origo.core.annotations.Decorates;
import main.origo.core.annotations.Theme;
import main.origo.core.annotations.ThemeVariant;
import main.origo.core.helpers.ElementHelper;
import main.origo.core.helpers.ThemeHelper;
import main.origo.core.ui.DecoratedNode;
import main.origo.core.ui.DecorationContext;
import main.origo.core.ui.Element;
import models.origo.core.Meta;
import play.api.templates.Html;
import play.mvc.Content;
import views.html.origo.admin.themes.AdminTheme.variant_main_and_left_columns;
import views.html.origo.admin.themes.AdminTheme.variant_main_only;
import views.html.origo.admin.themes.AdminTheme.variant_three_columns;
import views.html.origo.core.decorators.base;

import java.util.Collections;

@Theme(id = "admin")
public class AdminTheme {

    public static final String DEFAULT_VARIANT_NAME = "admin-main_only";
    public static final String LEFT_AND_MAIN_COLUMNS_VARIANT_NAME = "admin-main_and_left_columns";
    public static final String THREE_COLUMNS_VARIANT_NAME = "admin-three_columns";

    @ThemeVariant(id = DEFAULT_VARIANT_NAME, regions = {"top", "main"})
    public static Content renderDefaultTemplate(DecoratedNode decoratedNode) {
        return variant_main_only.render(decoratedNode);
    }

    @ThemeVariant(id = LEFT_AND_MAIN_COLUMNS_VARIANT_NAME, regions = {"top", "main", "left"})
    public static Content renderDefaultMainAndLeftColumnTemplate(DecoratedNode decoratedNode) {
        return variant_main_and_left_columns.render(decoratedNode);
    }

    @ThemeVariant(id = THREE_COLUMNS_VARIANT_NAME, regions = {"top", "main", "left", "right"})
    public static Content renderDefaultThreeColumnTemplate(DecoratedNode decoratedNode) {
        return variant_three_columns.render(decoratedNode);
    }

    public static String defaultDashboardClasses() {
        return "dashboard";
    }

    @Decorates(types = Admin.Dashboard.class)
    public static Html decorateDashboard(Element element, DecorationContext decorationContext) {
        Html body = ThemeHelper.decorateChildren(element, decorationContext);
        return base.render("div", element, body,
                ElementHelper.combineAttributes(element.getAttributes(),
                        Collections.singletonMap("class", defaultDashboardClasses() + " row")));
    }

    public static String defaultDashboardItemClasses() {
        return "dashboard-item item";
    }

    @Decorates(types = Admin.DashboardItem.class)
    public static Html decorateDashboardItem(Element element, DecorationContext decorationContext) {
        Html body = ThemeHelper.decorateChildren(element, decorationContext);
        return base.render("div", element, body,
                ElementHelper.combineAttributes(element.getAttributes(),
                        Collections.singletonMap("class", defaultDashboardItemClasses() + " col-md-3")));
    }

    @Decorates(types = {Element.Emphasis.class})
    public static Html decorateEmphasisWarning(Element.Emphasis element, DecorationContext decorationContext) {
        switch(element.type) {
            case ERROR:
                element.addAttribute("class", "text-danger");
                break;
            case WARNING:
                element.addAttribute("class", "text-warning");
                break;
            case INFO:
                element.addAttribute("class", "text-info");
                break;
            case MUTED:
                element.addAttribute("class", "text-muted");
                break;
            case PRIMARY:
                element.addAttribute("class", "text-primary");
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
                element.addAttribute("class", "alert alert-danger");
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

    @Decorates(types = {Element.Form.class})
    public static Html decorateForm(Element element, DecorationContext decorationContext) {
        element.addAttribute("role", "form");
        element.addAttribute("class", "horizontal");
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
        element.addAttribute("class", "btn btn-default");
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Element.InputSubmit.class})
    public static Html decorateSubmit(Element element, DecorationContext decorationContext) {
        element.addAttribute("class", "btn btn-default btn-primary");
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Element.InputText.class, Element.InputSelect.class, Element.InputPassword.class, Element.InputTextArea.class})
    public static Html decorateFormControls(Element element, DecorationContext decorationContext) {
        element.addAttribute("class", "form-control");
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Admin.TabBar.class})
    public static Html decorateTabBar(Element element, DecorationContext decorationContext) {
        element.addAttribute("class", "nav nav-pills");
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Admin.TabItem.class})
    public static Html decorateTabItem(Element element, DecorationContext decorationContext) {
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Admin.TabPane.class})
    public static Html decorateTabPane(Element element, DecorationContext decorationContext) {
        element.addAttribute("class", "tab-pane");
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Admin.TabContent.class})
    public static Html decorateTabContent(Element element, DecorationContext decorationContext) {
        element.addAttribute("class", "tab-content");
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Element.AnchorButton.class})
    public static Html decorateAnchorButton(Element element, DecorationContext decorationContext) {
        element.addAttribute("class", "btn btn-default");
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Element.Anchor.class})
    public static Html decorateAnchor(Element element, DecorationContext decorationContext) {
        if (decorationContext.parent().getType().equals(new Element.Anchor().getType())) {
            element.addAttribute("data-toggle", "tab");
        }
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Admin.ActionPanel.class})
    public static Html decorateActionPanel(Admin.ActionPanel element, DecorationContext decorationContext) {
        Element panel = new Element.Container().setId(element.getId()).setWeight(element.getWeight());
        for (String attributeKey : element.getAttributes().keySet()) {
            panel.addAttribute(attributeKey, element.getAttributes().get(attributeKey));
        }
        panel.addAttribute("class", "panel panel-default");
        Element.Container containerBody = new Element.Container().addAttribute("class", "panel-body");
        panel.addChild(containerBody);
        if (element.cancel != null) {
            containerBody.addChild(new Element.Container().
                    addAttribute("class", "pull-left").
                    addChild(element.cancel)
            );
        }
        if (element.submit != null || element.reset != null) {
            Element.Container buttonContainer = new Element.Container().addAttribute("class", "pull-right");
            containerBody.addChild(buttonContainer);
            if (element.submit != null) {
                buttonContainer.addChild(element.submit);
            }
            if (element.reset != null) {
                buttonContainer.addChild(element.reset);
            }
        }

        return panel.decorate(decorationContext);
    }


    // TODO: Load this from database instead
    public static Meta centerColumnMeta() {
        Meta meta = new Meta();
        meta.region = "center";
        meta.weight = 100;
        return meta;
    }

    // TODO: Load this from database instead
    public static Meta leftColumnMeta() {
        Meta meta = new Meta();
        meta.region = "left";
        meta.weight = 100;
        return meta;
    }

    // TODO: Load this from database instead
    public static Meta getRightColumnMeta() {
        Meta meta = new Meta();
        meta.region = "right";
        meta.weight = 100;
        return meta;
    }

    // TODO: Load this from database instead
    public static Meta topMeta() {
        Meta meta = new Meta();
        meta.region = "top";
        meta.weight = 100;
        return meta;
    }
}

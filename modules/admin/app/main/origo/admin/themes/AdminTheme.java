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
                        Collections.singletonMap("class", defaultDashboardClasses() + " row-fluid")));
    }

    public static String defaultDashboardItemClasses() {
        return "dashboard-item item";
    }

    @Decorates(types = Admin.DashboardItem.class)
    public static Html decorateDashboardItem(Element element, DecorationContext decorationContext) {
        Html body = ThemeHelper.decorateChildren(element, decorationContext);
        return base.render("div", element, body,
                ElementHelper.combineAttributes(element.getAttributes(),
                        Collections.singletonMap("class", defaultDashboardItemClasses() + " span3")));
    }

    @Decorates(types = {Element.InputSubmit.class, Element.InputButton.class, Element.InputReset.class})
    public static Html decorateButton(Element element, DecorationContext decorationContext) {
        element.addAttribute("class", "btn");
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Element.ErrorAlert.class})
    public static Html decorateEmphasisError(Element element, DecorationContext decorationContext) {
        element.addAttribute("class", "text-error");
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Element.WarningAlert.class})
    public static Html decorateEmphasisWarning(Element element, DecorationContext decorationContext) {
        element.addAttribute("class", "text-warning");
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Element.InfoAlert.class})
    public static Html decorateEmphasisInfo(Element element, DecorationContext decorationContext) {
        element.addAttribute("class", "text-info");
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Element.SuccessAlert.class})
    public static Html decorateEmphasisSuccess(Element element, DecorationContext decorationContext) {
        element.addAttribute("class", "text-success");
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Element.SuccessAlert.class})
    public static Html decorateEmphasisMuted(Element element, DecorationContext decorationContext) {
        element.addAttribute("class", "muted");
        return element.decorate(decorationContext);
    }

    @Decorates(types = {Element.ErrorAlert.class})
    public static Html decorateAlertError(Element element, DecorationContext decorationContext) {
        element.addAttribute("class", "alert alert-error");
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

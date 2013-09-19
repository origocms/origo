package main.origo.admin.themes;

import main.origo.admin.annotations.Admin;
import main.origo.core.annotations.Decorates;
import main.origo.core.annotations.Theme;
import main.origo.core.annotations.ThemeVariant;
import main.origo.core.helpers.ElementHelper;
import main.origo.core.helpers.ThemeHelper;
import main.origo.core.ui.Element;
import main.origo.core.ui.RenderedNode;
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
    public static Content renderDefaultTemplate(RenderedNode node) {
        return variant_main_only.render(node);
    }

    @ThemeVariant(id = LEFT_AND_MAIN_COLUMNS_VARIANT_NAME, regions = {"top", "main", "left"})
    public static Content renderDefaultMainAndLeftColumnTemplate(RenderedNode node) {
        return variant_main_and_left_columns.render(node);
    }

    @ThemeVariant(id = THREE_COLUMNS_VARIANT_NAME, regions = {"top", "main", "left", "right"})
    public static Content renderDefaultThreeColumnTemplate(RenderedNode node) {
        return variant_three_columns.render(node);
    }

    public static String defaultDashboardClasses() {
        return "dashboard";
    }

    @Decorates(types = Admin.Dashboard.class)
    public static Html decorateDashboard(Decorates.Context context) {
        Html body = ThemeHelper.decorateChildren(context.element, context.renderingContext);
        return base.render("div", context.element, body,
                ElementHelper.combineAttributes(context.element.getAttributes(),
                        Collections.singletonMap("class", defaultDashboardClasses() + " row-fluid")));
    }

    public static String defaultDashboardItemClasses() {
        return "dashboard-item item";
    }

    @Decorates(types = Admin.DashboardItem.class)
    public static Html decorateDashboardItem(Decorates.Context context) {
        Html body = ThemeHelper.decorateChildren(context.element, context.renderingContext);
        return base.render("div", context.element, body,
                ElementHelper.combineAttributes(context.element.getAttributes(),
                        Collections.singletonMap("class", defaultDashboardItemClasses() + " span3")));
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

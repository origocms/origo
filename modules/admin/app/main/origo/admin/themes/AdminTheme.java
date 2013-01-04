package main.origo.admin.themes;

import main.origo.admin.annotations.Admin;
import main.origo.core.annotations.Decorates;
import main.origo.core.annotations.Theme;
import main.origo.core.annotations.ThemeVariant;
import main.origo.core.helpers.ElementHelper;
import main.origo.core.helpers.ThemeHelper;
import main.origo.core.ui.Element;
import models.origo.core.Meta;
import play.api.templates.Html;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.origo.admin.decorators.dashboard;
import views.html.origo.admin.decorators.dashboard_item;
import views.html.origo.admin.themes.AdminTheme.variant_main_and_left_columns;
import views.html.origo.admin.themes.AdminTheme.variant_main_only;
import views.html.origo.admin.themes.AdminTheme.variant_three_columns;

import java.util.Collections;

@Theme(id = "admin")
public class AdminTheme {

    public static final String DEFAULT_VARIANT_NAME = "admin-main_only";
    public static final String LEFT_AND_MAIN_COLUMNS_VARIANT_NAME = "admin-main_and_left_columns";
    public static final String THREE_COLUMNS_VARIANT_NAME = "admin-three_columns";

    @ThemeVariant(id = DEFAULT_VARIANT_NAME, regions = {"top", "main"})
    public static Result renderDefaultTemplate(ThemeVariant.Context context) {
        return Controller.ok(variant_main_only.render(context));
    }

    @ThemeVariant(id = LEFT_AND_MAIN_COLUMNS_VARIANT_NAME, regions = {"top", "main", "left"})
    public static Result renderDefaultMainAndLeftColumnTemplate(ThemeVariant.Context context) {
        return Controller.ok(variant_main_and_left_columns.render(context));
    }

    @ThemeVariant(id = THREE_COLUMNS_VARIANT_NAME, regions = {"top", "main", "left", "right"})
    public static Result renderDefaultThreeColumnTemplate(ThemeVariant.Context context) {
        return Controller.ok(variant_three_columns.render(context));
    }

    public static String defaultDashboardClasses() {
        return "dashboard";
    }

    @Decorates(types = Admin.Dashboard.class)
    public static Html decorateDashboard(Decorates.Context context) {
        Html body = ThemeHelper.decorateChildren(context.element, context.renderingContext);
        return dashboard.render(context.element, body,
                ElementHelper.combineAttributes(context.element.getAttributes(),
                        Collections.singletonMap("class", defaultDashboardClasses() + " row-fluid")));
    }

    public static String defaultDashboardItemClasses() {
        return "dashboard-item item";
    }

    @Decorates(types = Admin.DashboardItem.class)
    public static Html decorateDashboardItem(Decorates.Context context) {
        Html body = ThemeHelper.decorateChildren(context.element, context.renderingContext);
        return dashboard_item.render(context.element, body,
                ElementHelper.combineAttributes(context.element.getAttributes(),
                        Collections.singletonMap("class", defaultDashboardItemClasses() + " span3")));
    }

    @Decorates(types = {Element.InputSubmit.class, Element.InputButton.class, Element.InputReset.class})
    public static Html decorateButton(Decorates.Context context) {
        context.element.addAttribute("class", "btn");
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

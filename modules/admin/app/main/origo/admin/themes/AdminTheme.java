package main.origo.admin.themes;

import main.origo.admin.annotations.Admin;
import main.origo.core.annotations.Decorates;
import main.origo.core.annotations.Theme;
import main.origo.core.annotations.ThemeVariant;
import main.origo.core.helpers.ElementHelper;
import main.origo.core.helpers.ThemeHelper;
import main.origo.core.ui.Element;
import play.api.templates.Html;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.origo.admin.decorators.dashboard;
import views.html.origo.admin.decorators.dashboard_item;
import views.html.origo.admin.themes.AdminTheme.variant_main_only;

import java.util.Collections;

@Theme(id = "admin")
public class AdminTheme {

    public static final String DEFAULT_VARIANT_NAME = "admin-default";

    @ThemeVariant(id = DEFAULT_VARIANT_NAME, regions = "main")
    public static Result renderDefaultTemplate(ThemeVariant.Context context) {
        return Controller.ok(variant_main_only.render(context));
    }

    private static String defaultDashboardClasses() {
        return "dashboard";
    }

    @Decorates(type = Admin.Dashboard.class)
    public static Html decorateDashboard(Decorates.Context context) {
        Html body = ThemeHelper.decorateChildren(context.element, context.renderingContext);
        return dashboard.render(context.element, body,
                ElementHelper.combineAttributes(context.element.getAttributes(),
                        Collections.singletonMap("class", defaultDashboardClasses() + " row-fluid")));
    }

    public static String defaultDashboardItemClasses() {
        return "dashboard-item item";
    }

    @Decorates(type = Admin.DashboardItem.class)
    public static Html decorateDashboardItem(Decorates.Context context) {
        Html body = ThemeHelper.decorateChildren(context.element, context.renderingContext);
        return dashboard_item.render(context.element, body,
                ElementHelper.combineAttributes(context.element.getAttributes(),
                        Collections.singletonMap("class", defaultDashboardItemClasses() + " span3")));
    }

    @Decorates(type = {Element.InputSubmit.class, Element.InputButton.class, Element.InputReset.class})
    public static Html decorateButton(Decorates.Context context) {
        context.element.addAttribute("class", "btn");
        return context.element.decorate(context.renderingContext);
    }

}

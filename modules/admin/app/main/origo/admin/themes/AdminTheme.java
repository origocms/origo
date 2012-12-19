package main.origo.admin.themes;

import main.origo.admin.annotations.Admin;
import main.origo.core.annotations.Decorates;
import main.origo.core.annotations.Theme;
import main.origo.core.annotations.ThemeVariant;
import main.origo.core.helpers.ThemeHelper;
import main.origo.core.ui.RenderingContext;
import main.origo.core.ui.UIElement;
import play.api.templates.Html;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.origo.admin.decorators.dashboard_item;
import views.html.origo.core.themes.DefaultTheme.variant_main_only;

@Theme(id = "admin")
public class AdminTheme {

    public static final String DEFAULT_VARIANT_NAME = "admin-default";

    @ThemeVariant(id = DEFAULT_VARIANT_NAME, regions = "main")
    public static Result renderDefaultTemplate(ThemeVariant.Context context) {
        return Controller.ok(variant_main_only.render(context));
    }

    @Decorates(type = Admin.DASHBOARD)
    public static Html decorateDashboardItem(Decorates.Context context) {
        Html body = ThemeHelper.decorateChildren(context.uiElement, context.renderingContext);
        return dashboard_item.render(context.uiElement, body, context.uiElement.getAttributes());
    }

}

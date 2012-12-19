package main.origo.admin.helpers;

import controllers.origo.admin.routes;
import main.origo.core.Node;
import main.origo.core.helpers.SettingsHelper;
import main.origo.core.helpers.forms.EditorHelper;
import main.origo.core.ui.UIElement;
import models.origo.core.Content;

public class AdminHelper {

    public static String getURLForAdminAction(String type) {
        return routes.Dashboard.pageWithType(type).url();
    }

    public static String getURLForAdminAction(String type, String identifier) {
        return routes.Dashboard.pageWithTypeAndIdentifier(type, identifier).url();
    }

    public static UIElement createDashboardItem(String dashboardItemName, Node node) {
        DashboardHelper.triggerBeforeDashboardItemLoaded(dashboardItemName, node);
        UIElement uiElement = DashboardHelper.triggerProvidesDashboardItemListener(dashboardItemName, node);
        DashboardHelper.triggerAfterDashboardItemLoaded(dashboardItemName, node);
        return uiElement;
    }
}

package main.origo.admin.helpers;

import main.origo.admin.annotations.Admin;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.helpers.OnLoadHelper;
import main.origo.core.helpers.ProvidesHelper;
import main.origo.core.ui.UIElement;

import java.util.Set;

public class DashboardHelper {

    /*
    * Convenience methods for hooks with DASHBOARD type
    */

    public static UIElement createDashboard(String type, Node node) throws NodeLoadException {
        DashboardHelper.triggerBeforeDashboardLoaded(type, node);
        UIElement uiElement = DashboardHelper.triggerProvidesDashboardInterceptor(type, node);
        if (uiElement == null) {
            throw new NodeLoadException(node.getNodeId(), "The provider for type ["+type+"] did not return a UIElement");
        }

        Set<String> dashboardItemNames = ProvidesHelper.getAllProvidesWithForType(Admin.DASHBOARD_ITEM);
        for (String dashboardItemName : dashboardItemNames) {
            node.addUIElement(createDashboardItem(dashboardItemName, node));
        }

        DashboardHelper.triggerAfterDashboardLoaded(type, node);
        return uiElement;
    }

    public static UIElement triggerProvidesDashboardInterceptor(String withType, Node node) {
        return ProvidesHelper.triggerInterceptor(Admin.DASHBOARD, withType, node);
    }

    public static void triggerBeforeDashboardLoaded(String withType, Node node) {
        OnLoadHelper.triggerBeforeInterceptor(Admin.DASHBOARD, withType, node);
    }

    public static void triggerAfterDashboardLoaded(String withType, Node node) {
        OnLoadHelper.triggerAfterInterceptor(Admin.DASHBOARD, withType, node);
    }

    /*
    * Convenience methods for hooks with DASHBOARD_ITEM type
    */

    public static UIElement createDashboardItem(String dashboardItemName, Node node) {
        DashboardHelper.triggerBeforeDashboardItemLoaded(dashboardItemName, node);
        UIElement uiElement = DashboardHelper.triggerProvidesDashboardItemInterceptor(dashboardItemName, node);
        DashboardHelper.triggerAfterDashboardItemLoaded(dashboardItemName, node);
        return uiElement;
    }

    public static UIElement triggerProvidesDashboardItemInterceptor(String withType, Node node) {
        return ProvidesHelper.triggerInterceptor(Admin.DASHBOARD_ITEM, withType, node);
    }

    public static void triggerBeforeDashboardItemLoaded(String withType, Node node) {
        OnLoadHelper.triggerBeforeInterceptor(Admin.DASHBOARD_ITEM, withType, node);
    }

    public static void triggerAfterDashboardItemLoaded(String withType, Node node) {
        OnLoadHelper.triggerAfterInterceptor(Admin.DASHBOARD_ITEM, withType, node);
    }

}
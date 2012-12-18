package main.origo.admin.helpers;

import main.origo.admin.annotations.Admin;
import main.origo.core.Node;
import main.origo.core.helpers.OnLoadHelper;
import main.origo.core.helpers.ProvidesHelper;
import main.origo.core.ui.UIElement;

public class DashboardHelper {

    /*
    * Convenience methods for hooks with DASHBOARD_ITEM type
    */

    public static UIElement triggerProvidesDashboardItemListener(String withType, Node node) {
        return ProvidesHelper.triggerInterceptor(Admin.DASHBOARD, withType, node);
    }

    public static void triggerBeforeDashboardItemLoaded(String withType, Node node) {
        OnLoadHelper.triggerBeforeInterceptor(Admin.DASHBOARD, withType, node);
    }

    public static void triggerAfterDashboardItemLoaded(String withType, Node node) {
        OnLoadHelper.triggerAfterInterceptor(Admin.DASHBOARD, withType, node);
    }

}
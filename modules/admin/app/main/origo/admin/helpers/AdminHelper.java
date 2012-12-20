package main.origo.admin.helpers;

import controllers.origo.admin.routes;

public class AdminHelper {

    public static String getURLForAdminAction(String type) {
        return routes.Dashboard.pageWithType(type).url();
    }

    public static String getURLForAdminAction(String type, String identifier) {
        return routes.Dashboard.pageWithTypeAndIdentifier(type, identifier).url();
    }
}

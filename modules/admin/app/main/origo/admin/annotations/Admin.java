package main.origo.admin.annotations;

import main.origo.core.ui.Element;
import main.origo.core.ui.RenderingContext;
import play.api.templates.Html;

public class Admin {

    public static class Type {
        public static final String DASHBOARD = "dashboard";
        public static final String DASHBOARD_ITEM = "dashboard_item";
        public static final String NAVIGATION = "navigation";
    }

    public static class With {
        public static final String FRONT_PAGE = "origo.admin.dashboard.frontpage";
        public static final String USER_PAGE = "origo.admin.dashboard.user";
        public static final String CONTENT_PAGE = "origo.admin.dashboard.content";
        public static final String SETTINGS_PAGE = "origo.admin.dashboard.settings";
    }

    public static class Dashboard extends Element {

        public Dashboard() {
            super("dashboard");
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            return getBody();
        }

    }

    public static class DashboardItem extends Element {

        public DashboardItem() {
            super("dashboard_item");
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            return getBody();
        }

    }

}

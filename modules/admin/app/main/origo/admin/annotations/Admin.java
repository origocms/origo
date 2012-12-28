package main.origo.admin.annotations;

import main.origo.core.ui.Element;
import main.origo.core.ui.RenderingContext;
import play.api.templates.Html;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Admin {

    public static final String DASHBOARD = "dashboard";
    public static final String DASHBOARD_ITEM = "dashboard_item";

    public static final String FRONT_PAGE_TYPE = "origo.admin.dashboard.frontpage";
    public static final String USER_PAGE_TYPE = "origo.admin.dashboard.user";
    public static final String CONTENT_PAGE_TYPE = "origo.admin.dashboard.content";
    public static final String SETTINGS_PAGE_TYPE = "origo.admin.dashboard.settings";

    public static class Dashboard extends Element {

        public static final String TYPE = "dashboard";

        public Dashboard() {
            super(TYPE);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            return getBody();
        }

    }

    public static class DashboardItem extends Element {

        public static final String TYPE = "dashboard_item";

        public DashboardItem() {
            super(TYPE);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            return getBody();
        }

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    public static @interface Navigation {
        String name();

    }

}

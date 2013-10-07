package main.origo.admin.annotations;

import main.origo.core.ui.DecorationContext;
import main.origo.core.ui.Element;
import play.api.templates.Html;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Admin {

    private Admin() {
        super();
    }

    public static class Type {
        public static final String ADMIN_NODE = "admin_node";
        public static final String DASHBOARD = "dashboard";
        public static final String DASHBOARD_ITEM = "dashboard_item";
    }

    public static class With {
        public static final String FRONT_PAGE = "frontpage";
        public static final String CONTENT_PAGE = "content";
        public static final String USER_PAGE = "user";
        public static final String SETTINGS_PAGE = "settings";

        public static final String TAB_BAR = "tab_item";
        public static final String TAB_CONTENT = "tab_content";
    }

    public static class Dashboard extends Element {

        public Dashboard() {
            super("dashboard");
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            return getBody();
        }

    }

    public static class DashboardItem extends Element {

        public DashboardItem() {
            super("dashboard_item");
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            return getBody();
        }

    }

    public static class TabBar extends Element.ListBulleted {
        public TabBar() {
            super();
        }
    }

    public static class TabItem extends Element.ListItem {
        public TabItem() {
            super();
        }
    }

    public static class TabContent extends Element.Panel {
        public TabContent() {
            super();
        }
    }

    public static class TabPane extends Element.Panel {
        public TabPane() {
            super();
        }

    }

    public static class TabScript extends Element.Script {
        public TabScript() {
            super();
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    public static @interface Navigation {
        String alias();
        String key();

        int weight() default 1000;

    }
}

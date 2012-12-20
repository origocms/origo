package main.origo.admin.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Admin {

    public static final String DASHBOARD = "dashboard";
    public static final String DASHBOARD_ITEM = "dashboard_item";

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    public static @interface Navigation {
        String name();

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    public static @interface NavigationItem {
        String name();

        String parent();
    }

}

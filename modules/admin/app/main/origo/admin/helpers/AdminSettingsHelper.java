package main.origo.admin.helpers;

import main.origo.core.helpers.CoreSettingsHelper;
import models.origo.core.Settings;
import models.origo.core.navigation.BasicNavigation;

public class AdminSettingsHelper extends CoreSettingsHelper {

    public static interface Keys {
        public static final String THEME_VARIANT = "admin_theme_variant";
        public static final String NAVIGATION_TYPE = "admin_navigation_type";
        public static final String DASHBOARD_TYPE = "admin_dashboard_type";
    }

    public static String getThemeVariant() {
        return Settings.load().getValue(Keys.THEME_VARIANT);
    }

    public static String getDashboardType() {
        return Settings.load().getValue(Keys.DASHBOARD_TYPE);
    }

    public static String getNavigationType() {
        return getClassTypeIfExists(Keys.NAVIGATION_TYPE, BasicNavigation.class.getName());
    }

}

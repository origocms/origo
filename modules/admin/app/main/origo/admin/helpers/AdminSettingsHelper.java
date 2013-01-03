package main.origo.admin.helpers;

import main.origo.core.helpers.CoreSettingsHelper;
import models.origo.core.Settings;

public class AdminSettingsHelper extends CoreSettingsHelper {

    public static interface Keys {
        public static final String THEME_VARIANT = "admin_theme_variant";
        public static final String HOME_DASHBOARD_TYPE = "admin_home_dashboard_type";
    }

    public static String getThemeVariant() {
        return Settings.load().getValue(Keys.THEME_VARIANT);
    }

    public static String getHomeDashboard() {
        return Settings.load().getValue(Keys.HOME_DASHBOARD_TYPE);
    }

}

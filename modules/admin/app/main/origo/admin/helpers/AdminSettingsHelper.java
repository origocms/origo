package main.origo.admin.helpers;

import main.origo.admin.interceptors.forms.AdminFormProvider;
import main.origo.core.helpers.SettingsHelper;
import main.origo.core.interceptors.forms.DefaultSubmitHandler;
import models.origo.core.Settings;

public class AdminSettingsHelper {

    public static interface Keys {
        public static final String THEME_VARIANT = "admin_theme_variant";
        public static final String HOME_DASHBOARD_TYPE = "admin_home_dashboard_type";
        public static final String ADMIN_FORM_TYPE = "event.admin_form_type";
        public static final String SUBMIT_HANDLER = "event.admin_submit_handler";
    }

    public static String getThemeVariant() {
        return Settings.load().getValue(Keys.THEME_VARIANT);
    }

    public static String getHomeDashboard() {
        return Settings.load().getValue(Keys.HOME_DASHBOARD_TYPE);
    }

    public static String getDefaultFormType() {
        return SettingsHelper.getClassTypeIfExists(Keys.ADMIN_FORM_TYPE, AdminFormProvider.TYPE);
    }

    public static String getSubmitHandler() {
        return SettingsHelper.getClassTypeIfExists(Keys.SUBMIT_HANDLER, DefaultSubmitHandler.class.getName());
    }

}

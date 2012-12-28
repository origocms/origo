package main.origo.core.helpers;

import main.origo.core.interceptors.forms.DefaultFormProvider;
import main.origo.core.interceptors.forms.DefaultSubmitHandler;
import main.origo.core.ui.Element;
import models.origo.core.Settings;
import models.origo.core.navigation.BasicNavigation;
import play.Logger;

public class CoreSettingsHelper {

    public static interface Keys {
        public static final String BASE_URL = "base_url";
        public static final String START_PAGE = "start_page";
        public static final String PAGE_NOT_FOUND_PAGE = "page_not_found_page";
        public static final String INTERNAL_SERVER_ERROR_PAGE = "internal_server_error_page";

        public static final String THEME = "theme";
        public static final String THEME_VARIANT = "theme_variant";
        public static final String NAVIGATION_TYPE = "navigation_type";

        public static final String DEFAULT_FORM_TYPE = "default_form_type";
        public static final String SUBMIT_HANDLER = "submit_handler";
        public static final String RICHTEXT_EDITOR_TYPE = "richtext_editor_type";
    }

    public static String getBaseUrl() {
        return Settings.load().getValue(Keys.BASE_URL);
    }

    public static String getStartPage() {
        return Settings.load().getValue(Keys.START_PAGE);
    }

    public static String getPageNotFoundPage() {
        return Settings.load().getValue(Keys.PAGE_NOT_FOUND_PAGE);
    }

    public static String getInternalServerErrorPage() {
        return Settings.load().getValue(Keys.INTERNAL_SERVER_ERROR_PAGE);
    }

    public static String getTheme() {
        return Settings.load().getValue(Keys.THEME);
    }

    public static String getThemeVariant() {
        return Settings.load().getValue(Keys.THEME_VARIANT);
    }

    public static String getNavigationType() {
        return getClassTypeIfExists(Keys.NAVIGATION_TYPE, BasicNavigation.class.getName());
    }

    public static String getDefaultFormType() {
        return getClassTypeIfExists(Keys.DEFAULT_FORM_TYPE, DefaultFormProvider.TYPE);
    }

    public static String getSubmitHandler() {
        return getClassTypeIfExists(Keys.SUBMIT_HANDLER, DefaultSubmitHandler.class.getName());
    }

    public static String getEditorType() {
        return Settings.load().getValue(Keys.RICHTEXT_EDITOR_TYPE);
    }

    public static String getEventHandler(String withType) {
        return Settings.load().getValue("event."+withType);
    }

    public static void setEventHandler(String withType, Class annotatedClass) {
        Settings.load().setValue("event."+withType, annotatedClass.getName());
    }

    public static String getDecorator(Class<? extends Element> type) {
        return Settings.load().getValue("decorator."+type.getName());
    }

    public static void setDecorator(Class<? extends Element> type, Class annotatedClass) {
        Settings.load().setValue("decorator."+type.getName(), annotatedClass.getName());
    }

    protected static String getClassTypeIfExists(String propertyName, String fallback) {
        String classType = Settings.load().getValue(propertyName);
        if (classType != null) {
            try {
                return Class.forName(classType).getName();
            } catch (ClassNotFoundException e) {
                Logger.warn("Unable to find " + propertyName + " type [" + classType + "], using fallback [" + fallback + "] instead");
            }
        }
        return fallback;
    }
}
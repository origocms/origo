package main.origo.core.helpers;

import main.origo.core.interceptors.forms.DefaultFormProvider;
import main.origo.core.interceptors.forms.DefaultSubmitHandler;
import main.origo.core.interceptors.forms.DefaultValidationHandler;
import models.origo.core.Settings;
import models.origo.core.navigation.BasicNavigation;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.Period;

public class CoreSettingsHelper {

    public static interface Keys {

        public static final String BASE_URL = "base_url";
        public static final String START_PAGE = "start_page";
        public static final String PAGE_NOT_FOUND_PAGE = "page_not_found_page";
        public static final String INTERNAL_SERVER_ERROR_PAGE = "internal_server_error_page";
        public static final String UNAUTHORIZED_PAGE = "unauthorized_page";
        public static final String LOGIN_PAGE = "login_page";

        public static final String THEME = "theme";
        public static final String THEME_VARIANT = "theme_variant";
        public static final String NAVIGATION_TYPE = "navigation_type";

        public static final String DEFAULT_FORM_TYPE = "event.default_form_type";
        public static final String SUBMIT_HANDLER = "event.submit_handler";
        public static final String VALIDATION_HANDLER = "event.validation_processing_handler";

        public static final String USER_TYPE = "origo.authentication.user_type";

        public static final String PREVIEW_TICKET_PERIOD = "origo.preview.ticket_period";

        public static final String INPUT_SUPPRESS_PASSWORD_VALUE = "origo.input.suppress_password_value";

    }

    private static Period previewTicketPeriod;

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

    public static String getLoginPage() {
        return Settings.load().getValue(Keys.LOGIN_PAGE);
    }

    public static String getTheme() {
        return Settings.load().getValue(Keys.THEME);
    }

    public static String getThemeVariant() {
        return Settings.load().getValue(Keys.THEME_VARIANT);
    }

    public static String getNavigationType() {
        return SettingsHelper.getClassTypeIfExists(Keys.NAVIGATION_TYPE, BasicNavigation.TYPE);
    }

    public static String getDefaultFormType() {
        return SettingsHelper.getClassTypeIfExists(Keys.DEFAULT_FORM_TYPE, DefaultFormProvider.TYPE);
    }

    public static String getSubmitHandler() {
        return SettingsHelper.getClassTypeIfExists(Keys.SUBMIT_HANDLER, DefaultSubmitHandler.class.getName());
    }

    public static String getValidationHandler() {
        return SettingsHelper.getClassTypeIfExists(Keys.VALIDATION_HANDLER, DefaultValidationHandler.class.getName());
    }

    public static String getUserType() {
        return Settings.load().getValue(Keys.USER_TYPE);
    }

    public static Period getPreviewTicketPeriod() {
        if (previewTicketPeriod == null) {
            String period = Settings.load().getValue(Keys.PREVIEW_TICKET_PERIOD);
            if (StringUtils.isNotBlank(period)) {
                previewTicketPeriod = DateHelper.parsePeriod(period);
            } else {
                previewTicketPeriod = Period.minutes(30);
            }
        }
        return previewTicketPeriod;
    }

    public static boolean isSuppressPasswordValues() {
        return Settings.load().getValueAsBoolean(Keys.INPUT_SUPPRESS_PASSWORD_VALUE, true);
    }

}

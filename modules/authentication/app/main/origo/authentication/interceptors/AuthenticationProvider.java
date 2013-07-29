package main.origo.authentication.interceptors;

import be.objectify.deadbolt.core.models.Subject;
import controllers.origo.authentication.routes;
import controllers.origo.core.CoreLoader;
import main.origo.authentication.helpers.EncryptionHelper;
import main.origo.core.ModuleException;
import main.origo.core.NodeLoadException;
import main.origo.core.NodeNotFoundException;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.core.security.AuthorizationEventGenerator;
import main.origo.core.utils.ExceptionUtil;
import models.origo.authentication.BasicUser;
import models.origo.core.Settings;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import play.Logger;
import play.Play;
import play.mvc.Content;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

@Interceptor
public class AuthenticationProvider {

    private static final String EMAIL_SESSION_KEY = "e%a";

    private static final String TIMESTAMP_SESSION_KEY = "t^s";

    private static Period sessionMaxAge;

    public static void register() {
        String maxAge = Play.application().configuration().getString("application.session.maxAge", "30m");
        PeriodFormatter format = new PeriodFormatterBuilder()
                .appendDays()
                .appendSuffix("d", "d")
                .printZeroRarelyFirst()
                .appendHours()
                .appendSuffix("h", "h")
                .printZeroRarelyFirst()
                .appendMinutes()
                .appendSuffix("m", "m")
                .toFormatter();
        sessionMaxAge = format.parsePeriod(maxAge);
        Logger.debug("Session maxAge is: " +
                formatIfNotZero(sessionMaxAge.getDays(), "days", "day") +
                formatIfNotZero(sessionMaxAge.getHours(), "hours", "hour") +
                formatIfNotZero(sessionMaxAge.getMinutes(), "minutes", "minute")
        );
    }

    @Provides(type = Core.Type.USER, with = Core.With.AUTHORIZATION_CHECK)
    public static Result authenticateUser(Provides.Context context) {
        if(getCurrent() != null) {
            return null;
        }
        return Controller.redirect(routes.Authentication.login());
    }

    @Provides(type = Core.Type.USER, with = Core.With.AUTHORIZATION_FAILURE)
    public static Result handleAuthFailure(Provides.Context context) {

        BasicUser user = AuthenticationProvider.getCurrent();
        AuthorizationEventGenerator.triggerBeforeAuthorizationFailure(user);

        try {
            String unauthorizedPage = Settings.load().getValue(CoreSettingsHelper.Keys.UNAUTHORIZED_PAGE);
            try {
                if (StringUtils.isNotBlank(unauthorizedPage)) {
                    Content content = CoreLoader.loadAndDecoratePage(unauthorizedPage, 0);
                    if (user != null) {
                        return Controller.forbidden(content);
                    } else {
                        return Controller.unauthorized(content);
                    }
                }
            } catch (NodeNotFoundException | NodeLoadException | ModuleException e) {
                ExceptionUtil.assertExceptionHandling(e);
                return CoreLoader.loadPageLoadErrorPage();
            }

            if (user != null) {
                Logger.warn("Using fallback forbidden handling, sending 403 with no content");
                return Controller.forbidden();
            } else {
                Logger.warn("Using fallback unauthorized handling, sending 401 with no content");
                return Controller.unauthorized();
            }
        } finally {
            AuthorizationEventGenerator.triggerAfterAuthorizationFailure(user);
        }
    }


    @Provides(type = Core.Type.USER, with = Core.With.AUTHORIZATION_SUBJECT)
    public static BasicUser getCurrentSubject(Provides.Context context) {
        return getCurrent();
    }

    private static BasicUser getCurrent() {
        String email = getSessionUserName();
        if (email != null) {
            return BasicUser.findWithEmail(email);
        } else {
            return null;
        }
    }

    private static String formatIfNotZero(int value, String plural, String singleton) {
        if (value > 0) {
            if (value > 1) {
                return "" + value + " " + plural;
            }
            return "" + value + " " + singleton;
        }
        return "";
    }

    public static Subject authenticate(final String email, final String password){
        BasicUser user = BasicUser.findWithEmail(email);
        if (user != null && password.equals(user.password)) {
            setTimestamp(Http.Context.current());
            return user;
        }
        return null;
    }

    public static void setTimestamp(Http.Context ctx) {
        ctx.session().put(TIMESTAMP_SESSION_KEY, String.valueOf(DateTime.now().getMillis()));
    }

    public static DateTime getTimestamp(Http.Context ctx) {
        String timestamp = ctx.session().get(TIMESTAMP_SESSION_KEY);
        if (StringUtils.isEmpty(timestamp)) {
            return new DateTime(0);
        }
        try {
            return new DateTime(Long.parseLong(timestamp));
        } catch (NumberFormatException e) {
            return new DateTime(0);
        }
    }

    public static void checkAndUpdateTimestamp(Http.Context ctx) {
        DateTime timestamp = AuthenticationProvider.getTimestamp(ctx);
        if (timestamp.isBefore(DateTime.now().minus(sessionMaxAge))) {
            ctx.session().remove(EMAIL_SESSION_KEY);
        } else {
            setTimestamp(ctx);
        }
    }

    public static String getSessionUserName() {
        checkAndUpdateTimestamp(Http.Context.current());
        return EncryptionHelper.decrypt(Http.Context.current().session().get(EMAIL_SESSION_KEY));
    }

    public static void setSessionUserName(String sessionUserName) {
        Controller.session(EMAIL_SESSION_KEY, EncryptionHelper.encrypt(sessionUserName));
    }
}

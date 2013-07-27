package main.origo.authentication.interceptors;

import be.objectify.deadbolt.core.models.Subject;
import main.origo.authentication.util.EncryptionUtils;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import models.origo.authentication.BasicUser;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import play.Logger;
import play.Play;
import play.mvc.Controller;
import play.mvc.Http;

@Interceptor
public class AuthenticationProvider {

    private static final String EMAIL_SESSION_KEY = "e%a";

    private static final String TIMESTAMP_SESSION_KEY = "t^s";

    private static Period sessionMaxAge;

    public static void register() {
        String maxAge = Play.application().configuration().getString("application.session.maxAge");
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

    @Provides(type = Core.Type.USER, with = Core.With.AUTH_CHECK)
    public static final isUserAuthenticated(Provides.Context context) {

    }

    public static Subject authenticate(final String email, final String password){
        BasicUser user = BasicUser.findWithEmail(email);
        if (user != null && password.equals(user.password)) {
            setTimestamp(Http.Context.current());
            return user;
        }
        return null;
    }

    public static BasicUser getCurrent() {
        String email = getSessionUserName();
        if (email != null) {
            return BasicUser.findWithEmail(email);
        } else {
            return null;
        }
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

    private static String formatIfNotZero(int value, String plural, String singleton) {
        if (value > 0) {
            if (value > 1) {
                return "" + value + " " + plural;
            }
            return "" + value + " " + singleton;
        }
        return "";
    }

    public static String getSessionUserName() {
        checkAndUpdateTimestamp(Http.Context.current());
        return EncryptionUtils.decrypt(Http.Context.current().session().get(EMAIL_SESSION_KEY));
    }

    public static void setSessionUserName(String sessionUserName) {
        Controller.session(EMAIL_SESSION_KEY, EncryptionUtils.encrypt(sessionUserName));
    }
}

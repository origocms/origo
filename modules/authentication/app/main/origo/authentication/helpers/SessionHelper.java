package main.origo.authentication.helpers;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import play.Logger;
import play.Play;
import play.mvc.Controller;
import play.mvc.Http;

public class SessionHelper {

    private static final String USERNAME_SESSION_KEY = "e%a";

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

    public static void setTimestamp() {
        Http.Context.current().session().put(TIMESTAMP_SESSION_KEY, String.valueOf(DateTime.now().getMillis()));
    }

    public static DateTime getTimestamp() {
        String timestamp = Http.Context.current().session().get(TIMESTAMP_SESSION_KEY);
        if (StringUtils.isEmpty(timestamp)) {
            return new DateTime(0);
        }
        try {
            return new DateTime(Long.parseLong(timestamp));
        } catch (NumberFormatException e) {
            return new DateTime(0);
        }
    }

    public static void checkAndUpdateTimestamp() {
        DateTime timestamp = getTimestamp();
        if (timestamp.isBefore(DateTime.now().minus(sessionMaxAge))) {
            Http.Context.current().session().remove(USERNAME_SESSION_KEY);
        } else {
            setTimestamp();
        }
    }

    public static String getSessionUserName() {
        checkAndUpdateTimestamp();
        return EncryptionHelper.decrypt(Http.Context.current().session().get(USERNAME_SESSION_KEY));
    }

    public static void setSessionUserName(String sessionUserName) {
        Controller.session(USERNAME_SESSION_KEY, EncryptionHelper.encrypt(sessionUserName));
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

}

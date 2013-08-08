package main.origo.core.helpers;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import play.Logger;
import play.Play;
import play.mvc.Http;

public class SessionHelper {

    private static String encryptedTimestampSessionKey;

    private static Period sessionMaxAge;

    public static void register() {
        String maxAge = Play.application().configuration().getString("application.session.maxAge", "30m");
        sessionMaxAge = DateHelper.parsePeriod(maxAge);
        Logger.debug("Session maxAge is: " +
                formatIfNotZero(sessionMaxAge.getDays(), "days", "day") +
                formatIfNotZero(sessionMaxAge.getHours(), "hours", "hour") +
                formatIfNotZero(sessionMaxAge.getMinutes(), "minutes", "minute")
        );
        encryptedTimestampSessionKey = EncryptionHelper.encrypt("origo.session-timestamp");
    }

    public static void setTimestamp() {
        Http.Context.current().session().put(encryptedTimestampSessionKey, String.valueOf(DateTime.now().getMillis()));
    }

    public static DateTime getTimestamp() {
        String timestamp = Http.Context.current().session().get(encryptedTimestampSessionKey);
        if (StringUtils.isNotEmpty(timestamp)) {
            try {
                return new DateTime(Long.parseLong(timestamp));
            } catch (NumberFormatException ignored) {
            }
        }
        return new DateTime(0);
    }

    public static void set(String key, String value) {
        Http.Context.current().session().put(EncryptionHelper.encrypt(key), EncryptionHelper.encrypt(value));
    }

    public static String get(String key) {
        return Http.Context.current().session().get(EncryptionHelper.decrypt(key));
    }

    public static boolean checkAndUpdateTimestamp() {
        DateTime timestamp = getTimestamp();
        if (timestamp.isAfter(DateTime.now().minus(sessionMaxAge))) {
            setTimestamp();
            return true;
        }
        return false;
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

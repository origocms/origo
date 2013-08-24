package main.origo.authentication.util;

import main.origo.core.helpers.SessionHelper;
import play.mvc.Http;

public class AuthenticationSessionUtils {

    private static final String USERNAME_SESSION_KEY = "username";

    public static String getSessionUserName() {
        if (SessionHelper.checkAndUpdateTimestamp()) {
            return SessionHelper.get(USERNAME_SESSION_KEY);
        } else {
            Http.Context.current().session().remove(USERNAME_SESSION_KEY);
            return null;
        }
    }

    public static void setSessionUserName(String sessionUserName) {
        SessionHelper.set(USERNAME_SESSION_KEY, sessionUserName);
    }

}

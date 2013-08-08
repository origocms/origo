package main.origo.authentication.util;

import main.origo.core.helpers.EncryptionHelper;
import main.origo.core.helpers.SessionHelper;
import play.mvc.Controller;
import play.mvc.Http;

public class AuthenticationSessionUtils {

    private static final String USERNAME_SESSION_KEY = "e%a";

    public static String getSessionUserName() {
        if (SessionHelper.checkAndUpdateTimestamp()) {
            return EncryptionHelper.decrypt(Http.Context.current().session().get(USERNAME_SESSION_KEY));
        } else {
            Http.Context.current().session().remove(USERNAME_SESSION_KEY);
            return null;
        }
    }

    public static void setSessionUserName(String sessionUserName) {
        Controller.session(USERNAME_SESSION_KEY, EncryptionHelper.encrypt(sessionUserName));
    }

}

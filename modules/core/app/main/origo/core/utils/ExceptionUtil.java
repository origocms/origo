package main.origo.core.utils;

import play.Logger;
import play.Play;

public class ExceptionUtil {

    /**
     * Transforms a nested exception into a simple runtime exception and throws it if in DEV mode.
     * Only logs the exception if in PROD mode.
     * @param e
     * @return
     */
    public static void assertExceptionHandling(Exception e) {
        if (Play.isDev()) {
            Throwable thrown = e;
            while(thrown instanceof RuntimeException) {
                thrown = e.getCause();
            }
            throw new RuntimeException(thrown);
        }
        Logger.error("An exception occurred while loading the page: " + e.getMessage(), e);
    }

}

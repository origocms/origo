package main.origo.core.utils;

import main.origo.core.InitializationException;
import main.origo.core.ModuleException;
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
            while(thrown instanceof RuntimeException && thrown.getCause() != null) {
                thrown = thrown.getCause();
            }
            if (thrown instanceof RuntimeException) {
                throw (RuntimeException)thrown;
            }
            throw new RuntimeException(thrown);
        }
        Logger.error("An exception occurred while loading: " + e.getMessage(), e);
    }

    public static RuntimeException getCause(Throwable e) {
        Throwable thrown = e;
        while(thrown.getCause() != null &&
                (thrown instanceof InitializationException ||
                        thrown instanceof ModuleException ||
                        thrown instanceof RuntimeException)) {
            thrown = thrown.getCause();
        }
        if (thrown instanceof RuntimeException) {
            throw (RuntimeException)thrown;
        } else {
            throw new RuntimeException(thrown);
        }
    }
}

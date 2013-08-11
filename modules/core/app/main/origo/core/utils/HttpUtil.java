package main.origo.core.utils;

import org.apache.commons.lang3.StringUtils;
import play.mvc.Controller;
import play.mvc.Http;

public class HttpUtil {

    private static final String X_REQUESTED_WITH_HEADER = "X-Requested-With";

    public static boolean isAjax() {
        return isAjax(Http.Context.current().request());
    }

    public static boolean isAjax(Http.Request request) {
        return "XMLHttpRequest".equals(request.getHeader(X_REQUESTED_WITH_HEADER));
    }


    public static boolean isAcceptsJson() {
        return isAcceptsJson(Http.Context.current().request());
    }

    public static boolean isAcceptsJson(Http.Request request) {
        String acceptString = request.getHeader(Controller.ACCEPT);
        return StringUtils.isNotBlank(acceptString) && acceptString.contains("application/json");
    }

}

package main.origo.core.actions;

import main.origo.core.CoreLoader;
import main.origo.core.event.NodeContext;
import play.libs.F;
import play.mvc.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

@With(ComponentWrapper.ComponentWrapperAction.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentWrapper {

    public static class ComponentWrapperAction extends Action.Simple {

        @Override
        public F.Promise<SimpleResult> call(Http.Context context) throws Throwable {

            F.Promise<SimpleResult> result = delegate.call(context);

            return result;
/*

            final int statusCode = JavaResultExtractor.getStatus(result);
            final Map<String,String> headers = JavaResultExtractor.getHeaders(result);
            final String body = new String(JavaResultExtractor.getBody(result));

            switch(statusCode) {
                // Codes that should be handled by error pages
                case 404: // File Not Found
                {
                    return CoreLoader.redirectToPageNotFoundPage();
                }
                case 500: // Internal Server Error
                {
                    return CoreLoader.redirectToPageLoadErrorPage();
                }
                // Codes which should be sent directly to the browser
                case 204: // No Content
                case 205: // Reset Content
                case 301: // Moved Permanently
                case 302: // Found
                case 303: // See Other
                case 304: // Not Modified
                case 307: // Temporary Redirect
                case 405: // Method Not Allowed
                case 406: // Not Acceptable
                case 409: // Gone
                case 501: // Not Implemented
                case 503: // Service Unavailable
                    return result;

                // Codes that can be used with content or without
                case 400: // Bad Request
                case 401: // Unauthorized
                case 403: // Forbidden
                {
                    if (body.isEmpty()) {
                        return result;
                    }
                    return decorate(headers, body);
                }

                    // Codes which mean that the body should be wrapped and sent to be decorated
                case 200: // OK
                case 201: // Created
                case 202: // Accepted
                {
                    return decorate(headers, body);
                }
                default:
                    Logger.error("Code '"+statusCode+"' is not set up to be handled");
            }

            return result;
*/
        }

        private Result decorate(Map<String, String> headers, String body) {

            try {
                NodeContext.set();
                Component.setWrappedComponent(new Component(headers, body));
                String path = Http.Context.current().request().path().substring(1);
                if (headers.containsKey(Component.PATH_ALIAS)) {
                    path = headers.get(Component.PATH_ALIAS);
                }
                return CoreLoader.loadPage(path);
            } finally {
                NodeContext.clear();
            }

        }
    }

}

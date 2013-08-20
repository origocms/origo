package main.origo.authentication.interceptors;

import controllers.origo.core.CoreLoader;
import main.origo.core.ModuleException;
import main.origo.core.NodeLoadException;
import main.origo.core.NodeNotFoundException;
import main.origo.core.User;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.core.security.AuthEventGenerator;
import main.origo.core.utils.ExceptionUtil;
import models.origo.core.Settings;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.mvc.Content;
import play.mvc.Controller;
import play.mvc.Result;

@Interceptor
public class AuthorizationProvider {

    @Provides(type = Core.Type.SECURITY, with = Core.With.AUTHORIZATION_CHECK)
    public static Boolean checkAuthorization(Provides.Context context) throws ModuleException, NodeLoadException {
        if(getCurrentSubject(context) == null) {
            return false;
        }

        // TODO: Add actual authorization check
        return true;
    }

    @Provides(type = Core.Type.SECURITY, with = Core.With.AUTHORIZATION_FAILURE)
    public static Result handleAuthFailure(Provides.Context context) throws NodeLoadException, ModuleException {

        User user = getCurrentSubject(context);
        AuthEventGenerator.triggerBeforeAuthorizationFailure(user);

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
                return CoreLoader.redirectToPageLoadErrorPage();
            }

            if (user != null) {
                Logger.warn("Using fallback forbidden handling, sending 403 with no content");
                return Controller.forbidden();
            } else {
                Logger.warn("Using fallback unauthorized handling, sending 401 with no content");
                return Controller.unauthorized();
            }
        } finally {
            AuthEventGenerator.triggerAfterAuthorizationFailure(user);
        }
    }


    @Provides(type = Core.Type.SECURITY, with = Core.With.AUTHORIZATION_SUBJECT)
    public static User getCurrentSubject(Provides.Context context) throws ModuleException, NodeLoadException {
        return AuthEventGenerator.triggerCurrentUserInterceptor();
    }

}

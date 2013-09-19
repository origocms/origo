package main.origo.authentication.interceptors;

import main.origo.core.*;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.core.security.SecurityEventGenerator;
import main.origo.core.utils.ExceptionUtil;
import models.origo.core.Settings;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.mvc.Content;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Map;

@Interceptor
public class AuthorizationProvider {

    @Provides(type = Core.Type.SECURITY, with = Core.With.AUTHORIZATION_FAILURE)
    public static Result handleAuthFailure(Node node, String withType, Map<String, Object> args) throws NodeLoadException, ModuleException {

        User user = SecurityEventGenerator.triggerCurrentUserInterceptor();
        SecurityEventGenerator.triggerBeforeAuthorizationFailure(user);

        try {
            String unauthorizedPage = Settings.load().getValue(CoreSettingsHelper.Keys.UNAUTHORIZED_PAGE);
            try {
                if (StringUtils.isNotBlank(unauthorizedPage)) {
                    Content content = CoreLoader.loadAndDecorateNode(unauthorizedPage, 0);
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
            SecurityEventGenerator.triggerAfterAuthorizationFailure(user);
        }
    }


}

package main.origo.authentication.interceptors;

import main.origo.core.CoreLoader;
import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.NodeNotFoundException;
import main.origo.core.User;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.core.security.SecurityEventGenerator;
import main.origo.core.utils.ExceptionUtil;
import models.origo.core.Settings;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.libs.F;
import play.mvc.Content;
import play.mvc.Controller;
import play.mvc.SimpleResult;

import java.util.Map;

@Interceptor
public class AuthorizationProvider {

    @Provides(type = Core.Type.SECURITY, with = Core.With.AUTHORIZATION_FAILURE)
    public static F.Promise<SimpleResult> handleAuthFailure(Node node, String withType, Map<String, Object> args) throws NodeLoadException, ModuleException {

        User user = SecurityEventGenerator.triggerCurrentUserInterceptor();
        SecurityEventGenerator.triggerBeforeAuthorizationFailure(user);

        try {
            String unauthorizedPage = Settings.load().getValue(CoreSettingsHelper.Keys.UNAUTHORIZED_PAGE);
            if (StringUtils.isNotBlank(unauthorizedPage)) {
                try {
                    Content content = CoreLoader.loadAndDecorateNode(unauthorizedPage, 0);
                    if (user != null) {
                        return F.Promise.<SimpleResult>pure(Controller.forbidden(content));
                    } else {
                        return F.Promise.<SimpleResult>pure(Controller.unauthorized(content));
                    }
                } catch (NodeNotFoundException | NodeLoadException | ModuleException e) {
                    ExceptionUtil.assertExceptionHandling(e);
                    return F.Promise.<SimpleResult>pure(CoreLoader.redirectToPageLoadErrorPage());
                }
            }

            if (user != null) {
                Logger.warn("Using fallback forbidden handling, sending 403 with no content");
                return F.Promise.<SimpleResult>pure(Controller.forbidden());
            } else {
                Logger.warn("Using fallback unauthorized handling, sending 401 with no content");
                return F.Promise.<SimpleResult>pure(Controller.unauthorized());
            }
        } finally {
            SecurityEventGenerator.triggerAfterAuthorizationFailure(user);
        }
    }


}

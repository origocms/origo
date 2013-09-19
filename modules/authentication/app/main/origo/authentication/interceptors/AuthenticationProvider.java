package main.origo.authentication.interceptors;

import controllers.origo.authentication.routes;
import main.origo.core.*;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.annotations.forms.SubmitState;
import main.origo.core.event.NodeContext;
import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.core.security.Security;
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
public class AuthenticationProvider {

    /**
     * Checks if there is a authenticated user and if not triggers the display of a login page.
     * If there is a login page set in the content that will be used, if not it will fall back to
     * creating a "blank" node with type Core.With.AUTHENTICATION_CHECK
     */
    @Provides(type = Core.Type.SECURITY, with = Core.With.AUTHENTICATION_CHECK)
    public static Result authenticateUser(Node node, String withType, Map<String, Object> args) throws ModuleException, NodeLoadException {
        User user = SecurityEventGenerator.triggerCurrentUserInterceptor();
        if (user != null) {
            return null;
        }
        String path = (String) NodeContext.current().attributes.get(Security.Params.AUTH_PATH);
        if (StringUtils.isNotBlank(path)) {
            String[] roles = SecurityEventGenerator.triggerProvidesAuthorizationRolesInterceptor(path);
            if (roles.length == 0) {
                return null;
            }
            return Controller.redirect(routes.Authentication.login(path));
        }
        return Controller.redirect(routes.Authentication.login(""));
    }

    /**
     * Handling the routing at the end of the submit process when the submit failed for some reason.
     */
    @SubmitState(state = SubmitState.FAILURE, with = Core.With.AUTHENTICATION_CHECK)
    public static Result handleFailure() {

        String unauthorizedPage = Settings.load().getValue(CoreSettingsHelper.Keys.UNAUTHORIZED_PAGE);
        try {
            if (StringUtils.isNotBlank(unauthorizedPage)) {
                Content content = CoreLoader.loadAndDecorateNode(unauthorizedPage, 0);
                return Controller.unauthorized(content);
            }
        } catch (NodeNotFoundException | NodeLoadException | ModuleException e) {
            ExceptionUtil.assertExceptionHandling(e);
            return CoreLoader.redirectToPageLoadErrorPage();
        }

        Logger.warn("Using fallback unauthorized handling, sending 401 with no content");
        return Controller.unauthorized();
    }

    /**
     * Handling the routing at the end of the submit process, it redirects to the original path the user tried to access
     * and if that is not set in the context it redirects to the start page from the settings.
     */
    @SubmitState(with = Core.With.AUTHENTICATION_CHECK)
    public static Result handleSuccess() {

        String originalPath = (String) NodeContext.current().attributes.get(Security.Params.AUTH_PATH);
        if (StringUtils.isBlank(originalPath)) {
            Logger.debug("No original path in context, defaulting to start page");
            return CoreLoader.redirectToStartPage();
        }
        return Controller.redirect(originalPath);
    }

}

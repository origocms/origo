package main.origo.core.security;

import be.objectify.deadbolt.core.models.Subject;
import com.google.common.collect.Maps;
import main.origo.core.ModuleException;
import main.origo.core.NodeLoadException;
import main.origo.core.User;
import main.origo.core.annotations.Core;
import main.origo.core.event.NodeContext;
import main.origo.core.event.OnLoadEventGenerator;
import main.origo.core.event.ProvidesEventGenerator;
import main.origo.core.helpers.CoreSettingsHelper;
import org.apache.commons.lang3.StringUtils;
import play.mvc.Result;

import java.util.Collections;
import java.util.Map;

public class AuthEventGenerator {

    public static Result triggerAuthenticationCheck(String path) throws ModuleException, NodeLoadException {
        NodeContext.current().attributes.put(Security.Params.AUTH_PATH, path);
        return ProvidesEventGenerator.triggerInterceptor(null, Core.Type.USER, Core.With.AUTHENTICATION_CHECK);
    }

    public static Boolean triggerAuthorizationCheck(String path, Map<String, Object> args) throws ModuleException, NodeLoadException {
        NodeContext.current().attributes.put(Security.Params.AUTH_PATH, path);
        String[] roles = AuthEventGenerator.triggerProvidesAuthorizationRolesInterceptor(path);
        if (roles.length == 0) {
            return true;
        }

        Subject subject = (Subject) NodeContext.current().attributes.get(Security.Params.AUTH_USER);
        if (subject == null) {
            subject = AuthEventGenerator.triggerCurrentUserInterceptor();
            if (subject == null) {
                throw new RuntimeException("No authenticated user");
            }
            NodeContext.current().attributes.put(Security.Params.AUTH_USER, subject);
        }
        return ProvidesEventGenerator.triggerInterceptor(null, Core.Type.USER, Core.With.AUTHORIZATION_CHECK, args);
    }

    public static String[] triggerProvidesAuthorizationRolesInterceptor(String path) throws ModuleException, NodeLoadException {
        return triggerProvidesAuthorizationRolesInterceptor(path, Maps.<String, Object>newHashMap());
    }

    public static String[] triggerProvidesAuthorizationRolesInterceptor(String path, Map<String, Object> args) throws ModuleException, NodeLoadException {
        NodeContext.current().attributes.put(Security.Params.AUTH_PATH, path);
        return ProvidesEventGenerator.triggerInterceptor(null, Core.Type.USER, Core.With.AUTHORIZATION_ROLES, args);
    }

    public static User triggerProvidesUserInterceptor(String username) throws ModuleException, NodeLoadException {
        String userType = getUserType();
        return ProvidesEventGenerator.triggerInterceptor(null, Core.Type.USER, userType, Collections.<String, Object>singletonMap("username", username));
    }

    public static User triggerCurrentUserInterceptor() throws ModuleException, NodeLoadException {
        return ProvidesEventGenerator.triggerInterceptor(null, Core.Type.USER, Core.With.AUTHENTICATION_CURRENT_USER);
    }

    public static void triggerBeforeUserLoaded() {
        triggerBeforeUserLoaded(Maps.<String, Object>newHashMap());
    }

    public static void triggerBeforeUserLoaded(Map<String, Object> args) {
        String userType = getUserType();
        OnLoadEventGenerator.triggerBeforeInterceptor(null, Core.Type.USER, userType, args);
    }

    public static void triggerAfterUserLoaded(User user) {
        triggerAfterUserLoaded(user, Maps.<String, Object>newHashMap());
    }

    public static void triggerAfterUserLoaded(User user, Map<String, Object> args) {
        args.put("user", user);
        OnLoadEventGenerator.triggerBeforeInterceptor(null, Core.Type.USER, user.type(), args);
    }

    private static String getUserType() {
        String userType = CoreSettingsHelper.getUserType();
        if (StringUtils.isNotBlank(userType)) {
            return userType;
        }
        // TODO Add fallback to static/hardcoded user provider with an Admin account only
        throw new RuntimeException("Unable to trigger user provider, no user type set.");
    }

    public static Result triggerProvidesAuthorizationFailure() throws ModuleException, NodeLoadException {
        return ProvidesEventGenerator.triggerInterceptor(null, Core.Type.USER, Core.With.AUTHORIZATION_FAILURE, Maps.<String, Object>newHashMap());
    }

    public static void triggerBeforeAuthorizationFailure(User user) {
        triggerBeforeAuthorizationFailure(user, Maps.<String, Object>newHashMap());
    }

    public static void triggerBeforeAuthorizationFailure(User user, Map<String, Object> args) {
        args.put(Security.Params.AUTH_USER, user);
        OnLoadEventGenerator.triggerBeforeInterceptor(null, Core.Type.USER, Core.With.AUTHORIZATION_FAILURE, args);
    }

    public static void triggerAfterAuthorizationFailure(User user) {
        triggerAfterAuthorizationFailure(user, Maps.<String, Object>newHashMap());
    }

    public static void triggerAfterAuthorizationFailure(User user, Map<String, Object> args) {
        args.put(Security.Params.AUTH_USER, user);
        OnLoadEventGenerator.triggerAfterInterceptor(null, Core.Type.USER, Core.With.AUTHORIZATION_FAILURE, args);
    }

    public static Subject triggerProvidesSubjectInterceptor() throws ModuleException, NodeLoadException {
        return triggerProvidesSubjectInterceptor(Maps.<String, Object>newHashMap());
    }

    public static Subject triggerProvidesSubjectInterceptor(Map<String, Object> args) throws ModuleException, NodeLoadException {
        return ProvidesEventGenerator.triggerInterceptor(null, Core.Type.USER, Core.With.AUTHORIZATION_SUBJECT, args);
    }

    public static Subject triggerValidateInterceptor(String username, String password) throws ModuleException, NodeLoadException {
        Map<String, Object> args = Maps.newHashMap();
        args.put(Security.Params.AUTH_USERNAME, username);
        args.put(Security.Params.AUTH_PASSWORD, password);
        return ProvidesEventGenerator.triggerInterceptor(null, Core.Type.USER, Core.With.AUTHENTICATION_VALIDATE, args);
    }

}

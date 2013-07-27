package main.origo.core.security;

import be.objectify.deadbolt.core.models.Subject;
import com.google.common.collect.Maps;
import main.origo.core.ModuleException;
import main.origo.core.NodeLoadException;
import main.origo.core.User;
import main.origo.core.annotations.Core;
import main.origo.core.event.OnLoadEventGenerator;
import main.origo.core.event.ProvidesEventGenerator;
import main.origo.core.helpers.CoreSettingsHelper;
import org.apache.commons.lang3.StringUtils;
import play.mvc.Result;

import java.util.Collections;
import java.util.Map;

public class AuthorizationEventGenerator {

    public static Subject triggerProvidesUserInterceptor(String username) throws ModuleException, NodeLoadException {
        String userType = getUserType();
        return ProvidesEventGenerator.triggerInterceptor(null, Core.Type.USER, userType, Collections.<String, Object>singletonMap("username", username));
    }

    public static void triggerBeforeUserLoaded(Map<String, Object> args) {
        String userType = getUserType();
        OnLoadEventGenerator.triggerBeforeInterceptor(null, Core.Type.USER, userType, args);
    }

    public static void triggerAfterUserLoaded(User user, Map<String, Object> args) {
        String userType = getUserType();
        args.put("user", user);
        OnLoadEventGenerator.triggerBeforeInterceptor(null, Core.Type.USER, userType, args);
    }

    private static String getUserType() {
        String userType = CoreSettingsHelper.getUserType();
        if (StringUtils.isBlank(userType)) {
            throw new RuntimeException("Unable to trigger user provider, no user type set.");
        }
        return userType;
    }

    public static Result triggerProvidesAuthFailure() throws ModuleException, NodeLoadException {
        return ProvidesEventGenerator.triggerInterceptor(null, Core.Type.USER, Core.With.AUTH_FAILURE, Maps.<String, Object>newHashMap());
    }

    public static void triggerBeforeAuthFailure() {

    }

    public static void triggerAfterAuthFailure() {

    }

    public static Subject triggerProvidesSubjectInterceptor() throws ModuleException, NodeLoadException {
        return ProvidesEventGenerator.triggerInterceptor(null, Core.Type.USER, Core.With.AUTH_SUBJECT, Maps.<String, Object>newHashMap());
    }
}

package main.origo.authentication.interceptors;

import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.JavaDeadboltAnalyzer;
import main.origo.core.ModuleException;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.security.AuthEventGenerator;
import main.origo.core.security.Security;
import models.origo.authentication.BasicAuthorization;
import models.origo.core.Alias;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Interceptor
public class BasicAuthorizationProvider {

    @Provides(type = Core.Type.USER, with = Core.With.AUTHORIZATION_CHECK)
    public static Boolean checkAuthorization(Provides.Context context) throws NodeLoadException, ModuleException {

        Subject subject = (Subject) context.attributes.get(Security.Params.AUTH_USER);
        String path = (String) context.attributes.get(Security.Params.AUTH_PATH);

        String[] roles = AuthEventGenerator.triggerProvidesAuthorizationRolesInterceptor(path);
        if (roles.length == 0) {
            // If no groups are set on the node, let anyone through
            return true;
        }

        return JavaDeadboltAnalyzer.checkRole(subject, roles);
    }

    @Provides(type = Core.Type.USER, with = Core.With.AUTHORIZATION_ROLES)
    public static String[] getAuthorizationRoles(Provides.Context context) {
        String path = (String) context.attributes.get(Security.Params.AUTH_PATH);

        BasicAuthorization basicAuthorization = getBasicAuthorization(path);
        if (basicAuthorization == null) {
            return new String[0];
        }
        return basicAuthorization.roles.toArray(new String[basicAuthorization.roles.size()]);
    }

    protected static BasicAuthorization getBasicAuthorization(String path) {
        BasicAuthorization basicAuthorization = getBasicAuthorizationForPath(path);
        if (basicAuthorization != null) {
            return basicAuthorization;
        }

        return getBasicAuthorizationForAlias(path);
    }

    protected static BasicAuthorization getBasicAuthorizationForPath(String path) {
        // Try this as a path
        BasicAuthorization basicAuthorization = selectLongestMatchingPath(path);
        if (basicAuthorization != null) {
            return basicAuthorization;
        }

        // Not a path, most likely a node id so strip leading '/' and try again
        if (path.startsWith("/")) {
            return BasicAuthorization.findWithPath(path.substring(1));
        }
        return null;
    }

    public static BasicAuthorization selectLongestMatchingPath(String path) {
        BasicAuthorization basicAuthorization = null;
        String currentPath = path;
        do {
            basicAuthorization = BasicAuthorization.findWithPath(currentPath);
            if (basicAuthorization != null) {
                return basicAuthorization;
            }
            currentPath = currentPath.substring(0, currentPath.lastIndexOf("/"));
        } while(StringUtils.isNotBlank(currentPath));
        return basicAuthorization;
    }

    private static boolean matches(String queryPath, String authPath) {
        Pattern pattern = Pattern.compile(authPath + ".*");
        Matcher matcher = pattern.matcher(queryPath);
        return matcher.matches();
    }

    protected static BasicAuthorization getBasicAuthorizationForAlias(String path) {
        Alias alias = getAlias(path);
        if (alias != null) {
            return BasicAuthorization.findWithPath(alias.pageId);
        }
        return null;
    }

    protected static Alias getAlias(String path) {
        Alias alias = Alias.findWithPath(path);
        if (alias != null ) {
            return alias;
        }
        if (path.startsWith("/")) {
            return Alias.findWithPath(path.substring(1));
        }
        return null;
    }

}

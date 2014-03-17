package main.origo.authentication.interceptors;

import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.JavaDeadboltAnalyzer;
import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.event.NodeContext;
import main.origo.core.security.Security;
import main.origo.core.security.SecurityEventGenerator;
import models.origo.authentication.BasicAuthorization;
import models.origo.core.Alias;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Interceptor
public class BasicAuthorizationProvider {

    @Provides(type = Core.Type.SECURITY, with = Core.With.AUTHORIZATION_CHECK)
    public static Boolean checkAuthorization(Node node, String withType, Map<String, Object> args) throws NodeLoadException, ModuleException {

        Subject subject = (Subject) NodeContext.current().attributes.get(Security.Params.AUTH_USER);
        String path = (String) NodeContext.current().attributes.get(Security.Params.AUTH_PATH);

        String[] roles = SecurityEventGenerator.triggerProvidesAuthorizationRolesInterceptor(path);
        return roles.length == 0 || JavaDeadboltAnalyzer.checkRole(subject, roles);

    }

    @Provides(type = Core.Type.SECURITY, with = Core.With.AUTHORIZATION_ROLES)
    public static String[] getAuthorizationRoles(Node node, String withType, Map<String, Object> args) {
        String path = (String) NodeContext.current().attributes.get(Security.Params.AUTH_PATH);

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
        BasicAuthorization basicAuthorization;
        String currentPath = path;
        do {
            basicAuthorization = BasicAuthorization.findWithPath(currentPath);
            if (basicAuthorization != null) {
                return basicAuthorization;
            }
            currentPath = currentPath.substring(0, currentPath.lastIndexOf("/"));
        } while(StringUtils.isNotBlank(currentPath));
        return null;
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

package main.origo.authentication.interceptors;

import be.objectify.deadbolt.core.models.Subject;
import main.origo.authentication.util.AuthenticationSessionUtils;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.helpers.SessionHelper;
import main.origo.core.security.Security;
import models.origo.authentication.BasicUser;

@Interceptor
public class BasicUserProvider {

/*
    @Provides(type = Core.Type.SECURITY, with = BasicUser.TYPE)
    public static Subject getUser(Provides.Context context) throws ModuleException, NodeLoadException {
        String username = (String) context.args.get("username");
        AuthEventGenerator.triggerBeforeUserLoaded(context.args);
        BasicUser user = BasicUser.findWithEmail(username);
        AuthEventGenerator.triggerAfterUserLoaded(user, context.args);
        return user;
    }
*/

    @Provides(type = Core.Type.SECURITY, with = Core.With.AUTHENTICATION_CURRENT_USER)
    public static BasicUser getCurrent(Provides.Context context) {
        String username = AuthenticationSessionUtils.getSessionUserName();
        if (username != null) {
            return BasicUser.findWithEmail(username);
        } else {
            return null;
        }
    }

    @Provides(type = Core.Type.SECURITY, with = Core.With.AUTHENTICATION_VALIDATE)
    public static Subject authenticate(Provides.Context context){
        String username = (String) context.args.get(Security.Params.AUTH_USERNAME);
        String password = (String) context.args.get(Security.Params.AUTH_PASSWORD);
        BasicUser user = BasicUser.findWithEmail(username);
        if (user != null && password.equals(user.password)) {
            SessionHelper.setTimestamp();
            return user;
        }
        return null;
    }
}

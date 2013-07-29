package main.origo.authentication.interceptors;

import be.objectify.deadbolt.core.models.Subject;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.security.AuthorizationEventGenerator;
import models.origo.authentication.BasicUser;

@Interceptor
public class BasicUserProvider {

    @Provides(type=Core.Type.USER, with = BasicUser.TYPE)
    public static Subject getUser(Provides.Context context) {
        String username = (String) context.args.get("username");
        AuthorizationEventGenerator.triggerBeforeUserLoaded(context.args);
        BasicUser user = BasicUser.findWithEmail(username);
        AuthorizationEventGenerator.triggerAfterUserLoaded(user, context.args);
        return user;
    }

}

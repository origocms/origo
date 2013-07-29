package main.origo.authentication.interceptors;

import be.objectify.deadbolt.core.models.Subject;
import com.google.common.collect.Maps;
import controllers.origo.core.CoreLoader;
import main.origo.core.ModuleException;
import main.origo.core.NodeLoadException;
import main.origo.core.NodeNotFoundException;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.core.security.AuthorizationEventGenerator;
import main.origo.core.utils.ExceptionUtil;
import models.origo.authentication.BasicUser;
import models.origo.core.Settings;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.mvc.Content;
import play.mvc.Controller;
import play.mvc.Result;

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

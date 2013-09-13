package controllers.origo.admin;

import be.objectify.deadbolt.java.actions.Dynamic;
import controllers.origo.core.CoreLoader;
import main.origo.core.actions.ContextAware;
import main.origo.core.event.forms.SubmitHandlerEventGenerator;
import main.origo.core.security.Security;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class Submit extends Controller {

    @Transactional
    @ContextAware
    @Dynamic(Security.Types.RESOURCE)
    public static Result submit() {
        final String postHandlerName = AdminSubmitHandlerEventGenerator.getRegisteredSubmitHandlerName();
        try {
            return SubmitHandlerEventGenerator.triggerSubmitHandler(postHandlerName);
        } catch (Exception e) {
            return CoreLoader.handleException(e);
        }
    }
}
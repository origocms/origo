package controllers.origo.core;

import main.origo.core.actions.ContextAware;
import main.origo.core.event.forms.SubmitHandlerEventGenerator;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class Submit extends Controller {

    @Transactional
    @ContextAware
    public static Result submit() {
        final String postHandlerName = SubmitHandlerEventGenerator.getRegisteredSubmitHandlerName();
        try {
            return SubmitHandlerEventGenerator.triggerSubmitHandler(postHandlerName);
        } catch (Exception e) {
            return CoreLoader.handleException(e);
        }
    }
}
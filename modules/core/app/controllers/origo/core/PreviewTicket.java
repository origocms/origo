package controllers.origo.core;

import be.objectify.deadbolt.java.actions.Dynamic;
import main.origo.core.actions.ContextAware;
import main.origo.core.security.Security;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class PreviewTicket extends Controller {

    @Transactional
    @Dynamic(Security.Types.RESOURCE)
    @ContextAware
    public static Result getTicket() {

        return TODO;

    }

}

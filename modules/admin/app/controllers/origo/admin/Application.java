package controllers.origo.admin;

import be.objectify.deadbolt.java.actions.Dynamic;
import main.origo.admin.AdminLoader;
import main.origo.core.actions.ContextAware;
import main.origo.core.security.Security;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

    @Transactional
    @ContextAware
    @Dynamic(Security.Types.RESOURCE)
    public static Result page(String identifier) {
        //TODO: Check if config !exists and redirect to wizard

        return AdminLoader.view(identifier);
    }

}

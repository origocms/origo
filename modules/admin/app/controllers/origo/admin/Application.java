package controllers.origo.admin;

import be.objectify.deadbolt.java.actions.Dynamic;
import main.origo.core.actions.ContextAware;
import main.origo.core.security.Security;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

    @Transactional
    @Dynamic(Security.Types.RESOURCE)
    @ContextAware
    public static Result view(String identifier) {
        //TODO: Check if config !exists and redirect to wizard

        return AdminLoader.view(identifier);
    }

    @Transactional
    @Dynamic(Security.Types.RESOURCE)
    @ContextAware
    public static Result create() {
        //TODO: Check if config !exists and redirect to wizard

        //return AdminLoader.create(type);
        return TODO;
    }

    @Transactional
    @Dynamic(Security.Types.RESOURCE)
    @ContextAware
    public static Result createWithType(String type) {
        //TODO: Check if config !exists and redirect to wizard

        return AdminLoader.create(type);
    }

    @Transactional
    @Dynamic(Security.Types.RESOURCE)
    @ContextAware
    public static Result edit(String identifier) {
        //TODO: Check if config !exists and redirect to wizard

        return AdminLoader.edit(identifier);
    }

    @Transactional
    @Dynamic(Security.Types.RESOURCE)
    @ContextAware
    public static Result delete(String identifier) {
        //TODO: Check if config !exists and redirect to wizard

        return AdminLoader.delete(identifier);
    }

}

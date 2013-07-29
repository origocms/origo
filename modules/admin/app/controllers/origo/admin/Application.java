package controllers.origo.admin;

import main.origo.core.actions.ContextAware;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

    @Transactional
    @ContextAware
    public static Result view(String identifier) {
        //TODO: Check if config !exists and redirect to wizard

        return AdminLoader.view(identifier);
    }

    @Transactional
    @ContextAware
    public static Result create() {
        //TODO: Check if config !exists and redirect to wizard

        //return AdminLoader.create(type);
        return TODO;
    }

    @Transactional
    @ContextAware
    public static Result createWithType(String type) {
        //TODO: Check if config !exists and redirect to wizard

        return AdminLoader.create(type);
    }

    @Transactional
    @ContextAware
    public static Result edit(String identifier) {
        //TODO: Check if config !exists and redirect to wizard

        return AdminLoader.edit(identifier);
    }

    @Transactional
    @ContextAware
    public static Result delete(String identifier) {
        //TODO: Check if config !exists and redirect to wizard

        return AdminLoader.delete(identifier);
    }

}

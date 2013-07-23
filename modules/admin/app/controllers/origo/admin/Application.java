package controllers.origo.admin;

import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

    @Transactional
    public static Result view(String identifier) {
        //TODO: Check if config !exists and redirect to wizard

        return AdminLoader.view(identifier);
    }

    @Transactional
    public static Result create() {
        //TODO: Check if config !exists and redirect to wizard

        //return AdminLoader.create(type);
        return TODO;
    }

    @Transactional
    public static Result createWithType(String type) {
        //TODO: Check if config !exists and redirect to wizard

        return AdminLoader.create(type);
    }

    @Transactional
    public static Result edit(String identifier) {
        //TODO: Check if config !exists and redirect to wizard

        return AdminLoader.edit(identifier);
    }

    @Transactional
    public static Result delete(String identifier) {
        //TODO: Check if config !exists and redirect to wizard

        return AdminLoader.delete(identifier);
    }

}

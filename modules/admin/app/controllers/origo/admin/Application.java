package controllers.origo.admin;

import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

    @Transactional
    public static Result viewWithType(String withType) {
        //TODO: Check if config !exists and redirect to wizard

        return AdminLoader.view(withType);
    }

    @Transactional
    public static Result viewWithTypeAndIdentifier(String type, String identifier) {
        //TODO: Check if config !exists and redirect to wizard

        return AdminLoader.view(type, identifier);
    }

    @Transactional
    public static Result createWithType(String type) {
        //TODO: Check if config !exists and redirect to wizard

        return AdminLoader.create(type);
    }

    @Transactional
    public static Result editWithTypeAndIdentifier(String type, String identifier) {
        //TODO: Check if config !exists and redirect to wizard

        return AdminLoader.edit(type, identifier);
    }

    @Transactional
    public static Result deleteWithTypeAndIdentifier(String type, String identifier) {
        //TODO: Check if config !exists and redirect to wizard

        return AdminLoader.delete(type, identifier);
    }

}

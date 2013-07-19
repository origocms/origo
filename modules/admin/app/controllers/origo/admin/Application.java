package controllers.origo.admin;

import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

    @Transactional
    public static Result pageWithType(String withType) {
        //TODO: Check if config !exists and redirect to wizard

        return AdminLoader.getPage(withType);
    }

    @Transactional
    public static Result pageWithTypeAndIdentifier(String type, String identifier) {
        //TODO: Check if config !exists and redirect to wizard

        return AdminLoader.getPage(type, identifier);
    }

}

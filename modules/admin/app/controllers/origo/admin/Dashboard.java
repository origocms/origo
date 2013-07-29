package controllers.origo.admin;

import be.objectify.deadbolt.java.actions.Dynamic;
import controllers.origo.core.CoreLoader;
import main.origo.core.security.OrigoDynamicResourceHandler;
import org.springframework.stereotype.Component;
import play.Logger;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class Dashboard extends Controller {

    @Transactional
    @Dynamic(OrigoDynamicResourceHandler.AUTH_HANDLER)
    public static Result index() {
        //TODO: Check if config !exists and redirect to wizard

        return AdminLoader.getFrontDashboard();
    }

    @Transactional
    @Dynamic(OrigoDynamicResourceHandler.AUTH_HANDLER)
    public static Result dashboard(String dashboard) {
        //TODO: Check if config !exists and redirect to wizard

        return AdminLoader.getDashboard(dashboard);
    }

    @Transactional
    @Dynamic(OrigoDynamicResourceHandler.AUTH_HANDLER)
    public static Result pageWithType(String dashboard, String withType) {
        //TODO: Check if config !exists and redirect to wizard

        return AdminLoader.getPage(withType);
    }

    @Transactional
    @Dynamic(OrigoDynamicResourceHandler.AUTH_HANDLER)
    public static Result pageWithTypeAndIdentifier(String dashboard, String type, String identifier) {
        //TODO: Check if config !exists and redirect to wizard

        return AdminLoader.getPage(type, identifier);
    }

}

package controllers.origo.admin;

import controllers.origo.core.CoreLoader;
import org.springframework.stereotype.Component;
import play.Logger;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class Dashboard extends Controller {

    @Transactional
    public static Result index() {
        //TODO: Check if config !exists and redirect to wizard

        return AdminLoader.getFrontDashboard();
    }

    @Transactional
    public static Result dashboard(String dashboard) {
        //TODO: Check if config !exists and redirect to wizard

        return AdminLoader.getDashboard(dashboard);
    }

    @Transactional
    public static Result pageWithType(String dashboard, String withType) {
        //TODO: Check if config !exists and redirect to wizard

        return AdminLoader.getPage(withType);
    }

    @Transactional
    public static Result pageWithTypeAndIdentifier(String dashboard, String type, String identifier) {
        //TODO: Check if config !exists and redirect to wizard

        return AdminLoader.getPage(type, identifier);
    }

}

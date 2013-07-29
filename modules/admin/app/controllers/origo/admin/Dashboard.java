package controllers.origo.admin;

import main.origo.core.actions.ContextAware;
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
    @ContextAware
    public static Result index() {
        //TODO: Check if config !exists and redirect to wizard

        return AdminLoader.getFrontDashboard();
    }

    @Transactional
    @Dynamic(OrigoDynamicResourceHandler.AUTH_HANDLER)
    @ContextAware
    public static Result dashboard(String dashboard) {
        //TODO: Check if config !exists and redirect to wizard

        return AdminLoader.getDashboard(dashboard);
    }

}

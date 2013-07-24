package controllers.origo.admin;

import main.origo.core.actions.ContextAware;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class Dashboard extends Controller {

    @Transactional
    @ContextAware
    public static Result index() {
        //TODO: Check if config !exists and redirect to wizard

        return AdminLoader.getFrontDashboard();
    }

    @Transactional
    @ContextAware
    public static Result dashboard(String dashboard) {
        //TODO: Check if config !exists and redirect to wizard

        return AdminLoader.getDashboard(dashboard);
    }

}

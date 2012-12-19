package controllers.origo.admin;

import controllers.origo.core.CoreLoader;
import org.springframework.stereotype.Component;
import play.Logger;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

@Component
public class Dashboard extends Controller {

    @Transactional
    public Result index() {
        //TODO: Check if config !exists and redirect to wizard

        try {
            return AdminLoader.getStartPage();
        } catch (Exception e) {
            Logger.error("Error: " + e.getMessage(), e);
            return CoreLoader.loadPageLoadErrorPage();
        }
    }

    @Transactional
    public Result pageWithType(String type) {
        //TODO: Check if config !exists and redirect to wizard

        try {
            return AdminLoader.getPage(type);
        } catch (Exception e) {
            Logger.error("Error: " + e.getMessage(), e);
            return CoreLoader.loadPageLoadErrorPage();
        }
    }

    @Transactional
    public Result pageWithTypeAndIdentifier(String type, String identifier) {
        //TODO: Check if config !exists and redirect to wizard

        try {
            return AdminLoader.getPage(type, identifier);
        } catch (Exception e) {
            Logger.error("Error: " + e.getMessage(), e);
            return CoreLoader.loadPageLoadErrorPage();
        }
    }

}

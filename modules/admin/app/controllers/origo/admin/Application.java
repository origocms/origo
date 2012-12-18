package controllers.origo.admin;

import controllers.origo.core.CoreLoader;
import main.origo.core.NodeNotFoundException;
import main.origo.core.helpers.ThemeHelper;
import main.origo.core.ui.NavigationElement;
import main.origo.core.ui.RenderedNode;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Collection;

public class Application extends Controller {

    public Result dashboard() {
        //TODO: Check if config !exists and redirect to wizard

        try {
            return AdminLoader.getStartPage();
        } catch (Exception e) {
            Logger.error("Error: " + e.getMessage(), e);
            return CoreLoader.loadPageLoadErrorPage();
        }
    }

    public Result pageWithType(String type) {
        //TODO: Check if config !exists and redirect to wizard

        try {
            return AdminLoader.getPage(type);
        } catch (Exception e) {
            Logger.error("Error: " + e.getMessage(), e);
            return CoreLoader.loadPageLoadErrorPage();
        }
    }

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

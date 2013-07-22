package controllers.origo;

import controllers.origo.core.CoreLoader;
import models.origo.core.Settings;
import org.springframework.stereotype.Component;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.origo.application.error;
import views.html.origo.application.not_found;

public class Application extends Controller {

    @Transactional
    public static Result index() throws Throwable {
        if (shouldRedirectToSetupPage()) {
            return redirect(routes.Setup.index());
        }
        return CoreLoader.getStartPage();
    }

    @Transactional
    public static Result page(String identifier) {
        if (shouldRedirectToSetupPage()) {
            return redirect(routes.Setup.index());
        }
        Result result = CoreLoader.getPage(identifier);
        return checkForFallBackPages(result);
    }

    @Transactional
    public static Result pageVersion(String identifier, int version) {
        if (shouldRedirectToSetupPage()) {
            return redirect(routes.Setup.index());
        }
        Result result = CoreLoader.getPage(identifier, version);
        return checkForFallBackPages(result);
    }

    private static boolean shouldRedirectToSetupPage() {
        return Settings.load().getValueAsBoolean("origo.basicdata.insert") == null;
    }

    private static Result checkForFallBackPages(Result result) {
        if (result.toString().contains("404")) {
            return notFound(not_found.render());
        } else if (result.toString().contains("500")) {
            return internalServerError(error.render());
        }
        return result;
    }

}

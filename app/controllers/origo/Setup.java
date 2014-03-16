package controllers.origo;

import main.origo.SampleDataCreator;
import models.origo.core.Settings;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.origo.setup.index;

public class Setup extends Controller {

    @Transactional
    public static Result index() {
        return ok(index.render());
    }

    @Transactional
    public static Result submit(boolean create) {
        if (create) {
            SampleDataCreator.create();
            Settings.load().setValue("origo.basicdata.insert", "false");
            return redirect(routes.Application.index());
        } else {
            Settings.load().setValue("origo.basicdata.insert", "false");
            return redirect(controllers.origo.admin.routes.Dashboard.index());
        }

    }
}

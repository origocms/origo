package controllers.origo;

import main.origo.SampleDataCreator;
import models.origo.core.Settings;
import org.springframework.stereotype.Component;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.origo.setup.index;

@Component
public class Setup extends Controller {

    @Transactional
    public Result index() {
        return ok(index.render());
    }

    @Transactional
    public Result submit(boolean create) {
        if (create) {
            SampleDataCreator.create();
            Settings.load().setValue("origo.basicdata.insert", "false");
            return redirect(routes.Application.index());
        } else {
            Settings settings = Settings.load();
            settings.setValue("origo.basicdata.insert", "false");
            Settings.save(settings);
            return redirect(controllers.origo.admin.routes.Dashboard.index());
        }

    }
}

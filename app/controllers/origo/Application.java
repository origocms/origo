package controllers.origo;

import controllers.origo.core.CoreLoader;
import org.springframework.stereotype.Component;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

@Component
public class Application extends Controller {

    @Transactional
    public Result index() {
        return CoreLoader.getStartPage();
    }

    @Transactional
    public Result page(String identifier) {
        return CoreLoader.getPage(identifier);
    }

    @Transactional
    public Result pageVersion(String identifier, int version) {
        return CoreLoader.getPage(identifier, version);
    }

}

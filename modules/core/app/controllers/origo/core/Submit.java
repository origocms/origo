package controllers.origo.core;

import main.origo.core.helpers.forms.SubmitHandlerHelper;
import org.springframework.stereotype.Component;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

@Component
public class Submit extends Controller {

    @Transactional
    public Result submit() {
        final String postHandlerName = SubmitHandlerHelper.getRegisteredSubmitHandlerName();
        return SubmitHandlerHelper.triggerSubmitHandler(postHandlerName);
    }

}

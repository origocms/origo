package controllers.origo.core;

import main.origo.core.event.forms.SubmitHandlerEventGenerator;
import org.springframework.stereotype.Component;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

@Component
public class Submit extends Controller {

    @Transactional
    public Result submit() {
        final String postHandlerName = SubmitHandlerEventGenerator.getRegisteredSubmitHandlerName();
        return SubmitHandlerEventGenerator.triggerSubmitHandler(postHandlerName);
    }

}

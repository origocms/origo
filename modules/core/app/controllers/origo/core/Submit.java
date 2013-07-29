package controllers.origo.core;

import main.origo.core.ModuleException;
import main.origo.core.NodeLoadException;
import main.origo.core.NodeNotFoundException;
import main.origo.core.event.forms.SubmitHandlerEventGenerator;
import main.origo.core.utils.ExceptionUtil;
import org.springframework.stereotype.Component;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class Submit extends Controller {

    @Transactional
    public static Result submit() {
        final String postHandlerName = SubmitHandlerEventGenerator.getRegisteredSubmitHandlerName();
        try {
            return SubmitHandlerEventGenerator.triggerSubmitHandler(postHandlerName);
        } catch (ModuleException e) {
            return CoreLoader.loadPageNotFoundPage();
        } catch (Exception e) {
            ExceptionUtil.assertExceptionHandling(e);
            return CoreLoader.loadPageLoadErrorPage();
        }
    }
}
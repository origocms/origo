package controllers.origo.core;

import main.origo.core.CoreLoader;
import main.origo.core.actions.ContextAware;
import main.origo.core.event.forms.SubmitHandlerEventGenerator;
import main.origo.core.helpers.CoreSettingsHelper;
import org.apache.commons.lang3.StringUtils;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class Submit extends Controller {

    @Transactional
    @ContextAware
    public static Result submit() {
        final String postHandler = CoreSettingsHelper.getSubmitHandler();
        if (StringUtils.isBlank(postHandler)) {
            throw new RuntimeException("No SubmitHandler defined in settings: "+CoreSettingsHelper.Keys.SUBMIT_HANDLER);
        }
        try {
            return SubmitHandlerEventGenerator.triggerSubmitHandler(postHandler);
        } catch (Exception e) {
            return CoreLoader.handleException(e);
        }
    }

}
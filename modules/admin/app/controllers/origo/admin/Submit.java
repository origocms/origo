package controllers.origo.admin;

import be.objectify.deadbolt.java.actions.Dynamic;
import main.origo.admin.helpers.AdminSettingsHelper;
import main.origo.core.CoreLoader;
import main.origo.core.actions.ContextAware;
import main.origo.core.event.forms.SubmitHandlerEventGenerator;
import main.origo.core.security.Security;
import org.apache.commons.lang3.StringUtils;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class Submit extends Controller {

    @Transactional
    @ContextAware
    @Dynamic(Security.Types.RESOURCE)
    public static Result submit() {
        final String postHandlerName = AdminSettingsHelper.getSubmitHandler();
        if (StringUtils.isBlank(postHandlerName)) {
            throw new RuntimeException("No SubmitHandler defined in settings: "+AdminSettingsHelper.Keys.SUBMIT_HANDLER);
        }
        try {
            return SubmitHandlerEventGenerator.triggerSubmitHandler(postHandlerName);
        } catch (Exception e) {
            return CoreLoader.handleException(e);
        }
    }

}
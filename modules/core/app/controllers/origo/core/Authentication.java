package controllers.origo.core;

import main.origo.core.CoreLoader;
import main.origo.core.ModuleException;
import main.origo.core.NodeLoadException;
import main.origo.core.actions.ContextAware;
import main.origo.core.event.NodeContext;
import main.origo.core.security.SecurityEventGenerator;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class Authentication extends Controller {

    @Transactional
    @ContextAware
    public static Result login() {
        try {
            return SecurityEventGenerator.triggerAuthenticationCheck(null);
        } catch (NodeLoadException e) {
            return CoreLoader.handleException(e);
        } catch (ModuleException e) {
            return CoreLoader.handleException(e);
        }
    }

    @Transactional
    @ContextAware
    public static Result logout(String path) {
        try {
            NodeContext.current().attributes.put(main.origo.core.security.Security.Params.AUTH_PATH, path);
            return SecurityEventGenerator.triggerSignout();
        } catch (NodeLoadException e) {
            return CoreLoader.handleException(e);
        } catch (ModuleException e) {
            return CoreLoader.handleException(e);
        }
    }

}

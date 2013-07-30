package controllers.origo.authentication;

import controllers.origo.core.CoreLoader;
import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.NodeNotFoundException;
import main.origo.core.actions.ContextAware;
import main.origo.core.annotations.Core;
import main.origo.core.event.NodeContext;
import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.core.helpers.NodeHelper;
import main.origo.core.helpers.ThemeHelper;
import main.origo.core.security.Security;
import main.origo.core.ui.RenderedNode;
import models.origo.core.RootNode;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class Authentication extends Controller {

    @ContextAware
    @Transactional(readOnly = true)
    public static Result login(String path) {

        if (StringUtils.isNotBlank(path)) {
            NodeContext.current().attributes.put(Security.Params.AUTH_PATH, path);
        }

        try {
            // Check if there is a specific page set up for login
            String loginPage = CoreSettingsHelper.getLoginPage();
            if (StringUtils.isNotBlank(loginPage)) {
                return ok(CoreLoader.loadAndDecoratePage(loginPage, 0));
            }

            Logger.debug("No login page defined, using fallback");

            // Fall back to empty root node and trigger loading
            RootNode rootNode = new RootNode("login-fallback", 0);
            rootNode.nodeType(Core.With.AUTHENTICATION_CHECK);
            Node node = NodeHelper.load(rootNode);

            // Render login page
            RenderedNode renderedNode = ThemeHelper.decorate(node, ThemeHelper.loadTheme(node, CoreSettingsHelper.getThemeVariant()));
            if (Logger.isDebugEnabled()) {
                Logger.debug("Decorated " + renderedNode);
            }
            return ok(ThemeHelper.render(renderedNode));
        } catch (NodeNotFoundException | NodeLoadException | ModuleException e) {
            return CoreLoader.handleException(e);
        }
    }

}
package controllers.origo.admin;

import controllers.origo.core.CoreLoader;
import main.origo.admin.helpers.AdminSettingsHelper;
import main.origo.admin.helpers.NavigationHelper;
import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.event.NodeContext;
import main.origo.core.helpers.NodeHelper;
import main.origo.core.helpers.ThemeHelper;
import main.origo.core.ui.NavigationElement;
import main.origo.core.ui.RenderedNode;
import main.origo.core.utils.ExceptionUtil;
import models.origo.core.RootNode;
import play.Logger;
import play.mvc.Content;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

public class AdminLoader {

    public static Result getFrontDashboard() {
        try {
            return Controller.ok(loadAndDecoratePage(AdminSettingsHelper.getHomeDashboard()));
        } catch (Exception e) {
            return handleException(e);
        }
    }

    public static Result getDashboard(String withType) {
        try {
            return Controller.ok(loadAndDecoratePage(withType));
        } catch (Exception e) {
            return handleException(e);
        }
    }

    public static Result getPage(String withType) {
        try {
            return Controller.ok(loadAndDecoratePage(withType));
        } catch (Exception e) {
            return handleException(e);
        }
    }

    public static Result getPage(String withType, String identifier) {
        try {
            return Controller.ok(loadAndDecoratePage(withType, identifier));
        } catch (Exception e) {
            return handleException(e);
        }
    }

    private static Result handleException(Exception e) {
        ExceptionUtil.assertExceptionHandling(e);
        return CoreLoader.loadPageLoadErrorPage();
    }

    public static Content loadAndDecoratePage(String withType) throws NodeLoadException, ModuleException {
        try {
            NodeContext.set();
            Logger.debug("Loading [" + withType + "] as type");
            RootNode rootNode = new RootNode(0);
            rootNode.nodeType(withType);
            Node node = NodeHelper.load(rootNode);
            return decorateNode(node);
        } finally {
            NodeContext.clear();
        }
    }

    public static Content loadAndDecoratePage(String withType, String identifier) throws NodeLoadException, ModuleException {
        try {
            NodeContext.set();
            Logger.debug("Loading [" + withType + "] as type and identifier [" + identifier + "]");
            RootNode rootNode = new RootNode(identifier, 0);
            rootNode.nodeType(withType);
            Node node = NodeHelper.load(rootNode);
            return decorateNode(node);
        } finally {
            NodeContext.clear();
        }
    }

    private static Content decorateNode(Node node) throws NodeLoadException, ModuleException {
        RenderedNode renderedNode = ThemeHelper.decorate(node, ThemeHelper.loadTheme(node, AdminSettingsHelper.getThemeVariant()));
        renderedNode.navigation(getNavigation(node));
        if (Logger.isDebugEnabled()) {
            Logger.debug("Decorated " + renderedNode);
        }
        return ThemeHelper.render(renderedNode);
    }

    public static List<NavigationElement> getNavigation(Node node) throws NodeLoadException, ModuleException {
        List<NavigationElement> navigationLinks = NavigationHelper.getNavigation(node);
        if (Logger.isDebugEnabled()) {
            Logger.debug("Navigation loaded " + navigationLinks);
        }
        return navigationLinks;
    }

}

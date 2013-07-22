package controllers.origo.admin;

import controllers.origo.core.CoreLoader;
import main.origo.admin.annotations.Admin;
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
import models.origo.core.RootNode;
import play.Logger;
import play.Play;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

public class AdminLoader {

    public static Result getFrontDashboard() {
        try {
            return loadAndDecoratePage(AdminSettingsHelper.getHomeDashboard());
        } catch (Exception e) {
            return CoreLoader.handleException(e);
        }
    }

    public static Result getDashboard(String withType) {
        try {
            return loadAndDecoratePage(withType);
        } catch (Exception e) {
            return CoreLoader.handleException(e);
        }
    }

    public static Result view(String withType) {
        try {
            return loadAndDecoratePage(withType);
        } catch (Exception e) {
            return CoreLoader.handleException(e);
        }
    }

    public static Result view(String withType, String identifier) {
        try {
            return loadAndDecoratePage(withType, identifier);
        } catch (Exception e) {
            return CoreLoader.handleException(e);
        }
    }

    public static Result create(String withType) {
        try {
            return loadAndDecoratePage(withType + Admin.Action.CREATE);
        } catch (Exception e) {
            return CoreLoader.handleException(e);
        }
    }

    public static Result edit(String withType, String identifier) {
        try {
            return loadAndDecoratePage(withType + Admin.Action.EDIT, identifier);
        } catch (Exception e) {
            return CoreLoader.handleException(e);
        }
    }

    public static Result delete(String withType, String identifier) {
        try {
            return loadAndDecoratePage(withType + Admin.Action.DELETE, identifier);
        } catch (Exception e) {
            return CoreLoader.handleException(e);
        }
    }

    private static Result loadAndDecoratePage(String withType) throws NodeLoadException, ModuleException {
        try {
            NodeContext.set();
            Node node = loadNode(withType);
            return decorateNode(node);
        } finally {
            NodeContext.clear();
        }
    }

    private static Result loadAndDecoratePage(String withType, String identifier) throws NodeLoadException, ModuleException {
        try {
            NodeContext.set();
            Node node = loadNode(withType, identifier);
            return decorateNode(node);
        } finally {
            NodeContext.clear();
        }
    }

    private static Node loadNode(String withType) throws NodeLoadException, ModuleException {
        Logger.debug("Loading [" + withType + "] as type");
        RootNode rootNode = new RootNode(0);
        rootNode.nodeType(withType);
        return NodeHelper.load(rootNode);
    }

    private static Node loadNode(String withType, String identifier) throws NodeLoadException, ModuleException {
        Logger.debug("Loading [" + withType + "] as type and identifier [" + identifier + "]");
        RootNode rootNode = new RootNode(identifier, 0);
        rootNode.nodeType(withType);
        return NodeHelper.load(rootNode);
    }

    private static Result decorateNode(Node node) throws NodeLoadException, ModuleException {
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

package controllers.origo.admin;

import controllers.origo.core.CoreLoader;
import main.origo.admin.annotations.Admin;
import main.origo.admin.helpers.AdminSettingsHelper;
import main.origo.admin.helpers.NavigationHelper;
import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.helpers.NodeHelper;
import main.origo.core.helpers.ThemeHelper;
import main.origo.core.ui.NavigationElement;
import main.origo.core.ui.RenderedNode;
import models.origo.core.RootNode;
import play.Logger;
import play.mvc.Content;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

public class AdminLoader {

    public static Result loadFrontDashboard() {
        try {
            return Controller.ok(loadAndDecoratePage(AdminSettingsHelper.getHomeDashboard()));
        } catch (Exception e) {
            return CoreLoader.handleException(e);
        }
    }

    public static Result loadDashboard(String withType) {
        try {
            return Controller.ok(loadAndDecoratePage(withType));
        } catch (Exception e) {
            return CoreLoader.handleException(e);
        }
    }

    public static Result view(String identifier) {
        try {
            return Controller.ok(loadAndDecoratePage(getType(identifier)));
        } catch (Exception e) {
            return CoreLoader.handleException(e);
        }
    }

    public static Result create(String withType) {
        try {
            return Controller.ok(loadAndDecoratePage(withType));
        } catch (Exception e) {
            return CoreLoader.handleException(e);
        }
    }

    public static Result edit(String identifier) {
        try {
            return Controller.ok(loadAndDecoratePage(getType(identifier) + Admin.Action.EDIT, identifier));
        } catch (Exception e) {
            return CoreLoader.handleException(e);
        }
    }

    public static Result delete(String identifier) {
        try {
            return Controller.ok(loadAndDecoratePage(getType(identifier) + Admin.Action.DELETE, identifier));
        } catch (Exception e) {
            return CoreLoader.handleException(e);
        }
    }

    private static Content loadAndDecoratePage(String withType) throws NodeLoadException, ModuleException {
        Logger.debug("Loading [" + withType + "] as type");
        RootNode rootNode = new RootNode(0);
        rootNode.nodeType(withType);
        Node node = NodeHelper.load(rootNode);
        return decorateNode(node);
    }

    private static Content loadAndDecoratePage(String withType, String identifier) throws NodeLoadException, ModuleException {
        Logger.debug("Loading [" + withType + "] as type and identifier [" + identifier + "]");
        RootNode rootNode = new RootNode(identifier, 0);
        rootNode.nodeType(withType);
        Node node = NodeHelper.load(rootNode);
        return decorateNode(node);
    }

    private static String getType(String identifier) throws NodeLoadException {
        try {
            RootNode node = RootNode.findLatestVersionWithNodeId(identifier);
            if (node != null) {
                return node.nodeType();
            }
        } catch (Exception ignored) {
        }
        throw new NodeLoadException(identifier, "No Node with id '"+identifier+"'");
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

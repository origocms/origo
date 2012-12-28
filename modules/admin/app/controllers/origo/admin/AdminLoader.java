package controllers.origo.admin;

import main.origo.admin.helpers.AdminSettingsHelper;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.ThemeRepository;
import main.origo.core.event.NodeContext;
import main.origo.core.helpers.NavigationHelper;
import main.origo.core.helpers.NodeHelper;
import main.origo.core.helpers.ThemeHelper;
import main.origo.core.internal.CachedThemeVariant;
import main.origo.core.ui.NavigationElement;
import main.origo.core.ui.RenderedNode;
import models.origo.core.RootNode;
import play.Logger;
import play.mvc.Result;

import java.util.List;

public class AdminLoader {

    public static Result getFrontDashboard() throws NodeLoadException {
        return loadAndDecoratePage(AdminSettingsHelper.getDashboardType());
    }

    public static Result getDashboard(String withType) throws NodeLoadException {
        return loadAndDecoratePage(withType);
    }

    public static Result getPage(String withType) throws NodeLoadException {
        return loadAndDecoratePage(withType);
    }

    public static Result getPage(String withType, String identifier) throws NodeLoadException {
        return loadAndDecoratePage(withType, identifier);
    }

    private static Result loadAndDecoratePage(String withType) throws NodeLoadException {
        try {
            NodeContext.set();
            Node node = loadNode(withType);
            return decorateNode(node);
        } finally {
            NodeContext.clear();
        }
    }

    private static Result loadAndDecoratePage(String withType, String identifier) throws NodeLoadException {
        try {
            NodeContext.set();
            Node node = loadNode(withType, identifier);
            return decorateNode(node);
        } finally {
            NodeContext.clear();
        }
    }

    private static Node loadNode(String withType) throws NodeLoadException {
        Logger.debug("Loading [" + withType + "] as type");
        return loadByType(withType);
    }

    private static Node loadNode(String withType, String identifier) throws NodeLoadException {
        Logger.debug("Loading [" + withType + "] as type and identifier [" + identifier + "]");
        return loadByType(withType, identifier);
    }

    private static Node loadByType(String withType) throws NodeLoadException {
        RootNode rootNode = loadRootNode(withType);
        // We'll set the root node for now, hopefully it will be overridden during load
        NodeContext.current().node = rootNode;
        return NodeHelper.load(rootNode);
    }

    private static Node loadByType(String withType, String identifier) throws NodeLoadException {
        RootNode rootNode = loadRootNode(withType, identifier);
        // We'll set the root node for now, hopefully it will be overridden during load
        NodeContext.current().node = rootNode;
        return NodeHelper.load(rootNode);
    }

    private static RootNode loadRootNode(String withType) {
        RootNode rootNode = new RootNode(0);
        rootNode.nodeType = withType;
        return rootNode;
    }

    private static RootNode loadRootNode(String withType, String identifier) {
        RootNode rootNode = loadRootNode(withType);
        rootNode.nodeId = identifier;
        return rootNode;
    }

    private static Result decorateNode(Node node) {
        CachedThemeVariant themeVariant = ThemeRepository.getThemeVariant(AdminSettingsHelper.getThemeVariant());
        RenderedNode renderedNode = ThemeHelper.decorate(node, themeVariant);
        renderedNode.navigation(getNavigation(node.getNodeId()));
        if (Logger.isDebugEnabled()) {
            Logger.debug("Decorated " + renderedNode);
        }
        return ThemeHelper.render(renderedNode);
    }

    public static List<NavigationElement> getNavigation(String withType) {
        Node node = loadRootNode(withType);
        List<NavigationElement> navigationLinks = NavigationHelper.getNavigation(node, NavigationElement.ADMIN);
        if (Logger.isDebugEnabled()) {
            Logger.debug("Navigation loaded " + navigationLinks);
        }
        return navigationLinks;
    }

}

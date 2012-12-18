package controllers.origo.admin;

import main.origo.core.CachedThemeVariant;
import main.origo.core.Node;
import main.origo.core.Themes;
import main.origo.core.helpers.NavigationHelper;
import main.origo.core.helpers.NodeHelper;
import main.origo.core.helpers.SettingsHelper;
import main.origo.core.helpers.ThemeHelper;
import main.origo.core.ui.NavigationElement;
import main.origo.core.ui.RenderedNode;
import models.origo.core.RootNode;
import play.Logger;
import play.mvc.Result;

import java.util.List;

public class AdminLoader {

    public static Result getStartPage() {
        return loadAndDecorateStartPage();
    }

    public static Result getPage(String withType) {
        return loadAndDecoratePage(withType);
    }

    public static Result getPage(String withType, String identifier) {
        return loadAndDecoratePage(withType, identifier);
    }

    private static Result loadAndDecorateStartPage() {
        return loadAndDecoratePage(SettingsHelper.Admin.getDashboardType());
    }

    private static Result loadAndDecoratePage(String withType) {
        Node node = loadNode(withType);
        return decorateNode(node);
    }

    private static Result loadAndDecoratePage(String withType, String identifier) {
        Node node = loadNode(withType, identifier);
        return decorateNode(node);
    }

    private static Node loadNode(String withType) {
        Logger.debug("Loading [" + withType + "] as type");
        return loadByType(withType);
    }

    private static Node loadNode(String withType, String identifier) {
        Logger.debug("Loading [" + withType + "] as type and identifier [" + identifier + "]");
        return loadByType(withType, identifier);
    }

    private static Node loadByType(final String withType) {
        RootNode rootNode = loadRootNode(withType);
        return NodeHelper.load(rootNode);
    }

    private static Node loadByType(String withType, String identifier) {
        RootNode rootNode = loadRootNode(withType, identifier);
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
        CachedThemeVariant themeVariant = Themes.getThemeVariant(SettingsHelper.Admin.getThemeVariant());
        RenderedNode renderedNode = ThemeHelper.decorate(node, themeVariant);
        renderedNode.setNavigation(getNavigation(node.getNodeId()));
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
package controllers.origo.core;

import main.origo.core.Node;
import main.origo.core.NodeNotFoundException;
import main.origo.core.helpers.NavigationHelper;
import main.origo.core.helpers.NodeHelper;
import main.origo.core.helpers.SettingsHelper;
import main.origo.core.helpers.ThemeHelper;
import models.origo.core.Alias;
import main.origo.core.ui.NavigationElement;
import main.origo.core.ui.RenderedNode;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.util.Collection;
import java.util.List;

public class CoreLoader {

    public static Result getStartPage() {
        String startPage = SettingsHelper.Core.getStartPage();
        try {
            return loadAndDecoratePage(startPage, 0);
        } catch (NodeNotFoundException e) {
            return loadPageNotFoundErrorPage();
        } catch (Exception e) {
            Logger.error("An exception occurred while loading the start page: " + e.getMessage(), e);
            return loadPageLoadErrorPage();
        }
    }

    public static Result getPage(String identifier) {
        try {
            return loadAndDecoratePage(identifier, 0);
        } catch (NodeNotFoundException e) {
            return loadPageNotFoundErrorPage();
        } catch (Exception e) {
            Logger.error("An exception occurred while loading the page [" + identifier + "]: " + e.getMessage(), e);
            return loadPageLoadErrorPage();
        }
    }

    public static Result getPage(String identifier, int version) {
        try {
            return loadAndDecoratePage(identifier, version);
        } catch (NodeNotFoundException e) {
            return loadPageNotFoundErrorPage();
        } catch (Exception e) {
            Logger.error("An exception occurred while loading the page [" + identifier + "] with version [" + version + "]: " + e.getMessage(), e);
            return loadPageLoadErrorPage();
        }
    }

    public static Result loadPageNotFoundErrorPage() {
        String pageNotFoundPage = SettingsHelper.Core.getPageNotFoundPage();
        Collection<Alias> aliases = Alias.findWithPageId(pageNotFoundPage);
        String url;
        if (aliases.iterator().hasNext()) {
            Alias alias = aliases.iterator().next();
            url = SettingsHelper.Core.getBaseUrl() + "" + alias.path;
        } else {
            // Defaulting to /page-not-found
            url = SettingsHelper.Core.getBaseUrl() + "page-not-found";
        }

        // TODO: Could this be done without using the request?
        if (url.equalsIgnoreCase(Http.Context.current().request().path())) {
            Logger.warn("No page-not-found page defined, sending 404");
            return Controller.notFound();
        }
        Logger.debug("Redirecting to Page-Not-Found Page");
        return Controller.redirect(url);
    }

    public static Result loadPageLoadErrorPage() {
        String internalServerErrorPage = SettingsHelper.Core.getInternalServerErrorPage();
        Collection<Alias> aliases = Alias.findWithPageId(internalServerErrorPage);
        String url;
        if (aliases.iterator().hasNext()) {
            Alias alias = aliases.iterator().next();
            url = SettingsHelper.Core.getBaseUrl() + "" + alias.path;
        } else {
            // Defaulting to /error
            url = SettingsHelper.Core.getBaseUrl() + "error";
        }
        // TODO: Could this be done without using the request?
        if (url.equalsIgnoreCase(Http.Context.current().request().path())) {
            Logger.warn("No page-load-error page defined, sending 500");
            return Controller.internalServerError();
        }
        Logger.debug("Redirecting to Internal Error Page");
        return Controller.redirect(url);
    }

    private static Result loadAndDecoratePage(String identifier, int version) throws NodeNotFoundException {
        Node node = loadNode(identifier, version);
        RenderedNode renderedNode = ThemeHelper.decorate(node, ThemeHelper.loadTheme(node));
        renderedNode.navigation(getNavigation(identifier));
        if (Logger.isDebugEnabled()) {
            Logger.debug("Decorated " + renderedNode);
        }
        return ThemeHelper.render(renderedNode);
    }

    private static Node loadNode(String identifier, int version) throws NodeNotFoundException {
        Logger.trace("Trying to find alias for [" + identifier + "]");

        Alias alias = Alias.findWithPath(identifier);
        if (alias != null) {
            Logger.debug("Found alias: " + alias.toString());
            return loadByNodeIdAndVersion(alias.pageId, version);
        } else {
            Logger.debug("No Alias found trying [" + identifier + "] as nodeId");
            return loadByNodeIdAndVersion(identifier, version);
        }
    }

    private static Node loadByNodeIdAndVersion(String identifier, int version) throws NodeNotFoundException {
        Node node;
        if (version != 0) {
            node = NodeHelper.load(identifier, version);
        } else {
            node = NodeHelper.load(identifier);
        }
        if(node == null) {
            throw new NodeNotFoundException(identifier);
        }
        if (Logger.isDebugEnabled()) {
            Logger.debug("Loaded " + node.toString());
        }
        return node;
    }

    public static List<NavigationElement> getNavigation(String identifier) throws NodeNotFoundException {
        return getNavigation(identifier, 0);
    }

    public static List<NavigationElement> getNavigation(String identifier, int version) throws NodeNotFoundException {
        Node node = loadNode(identifier, version);
        List<NavigationElement> navigationLinks = NavigationHelper.getNavigation(node, NavigationElement.FRONT);
        if (Logger.isDebugEnabled()) {
            Logger.debug("Navigation loaded " + navigationLinks);
        }
        return navigationLinks;
    }

}

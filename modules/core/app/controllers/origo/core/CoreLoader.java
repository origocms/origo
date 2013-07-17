package controllers.origo.core;

import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.NodeNotFoundException;
import main.origo.core.event.NodeContext;
import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.core.helpers.NavigationHelper;
import main.origo.core.helpers.NodeHelper;
import main.origo.core.helpers.ThemeHelper;
import main.origo.core.ui.NavigationElement;
import main.origo.core.ui.RenderedNode;
import models.origo.core.Alias;
import play.Logger;
import play.Play;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.util.Collection;
import java.util.List;

public class CoreLoader {

    public static Result getStartPage() {
        String startPage = CoreSettingsHelper.getStartPage();
        try {
            return loadAndDecoratePage(startPage, 0);
        } catch (NodeNotFoundException e) {
            return loadPageNotFoundPage();
        } catch (Exception e) {
            return handleException(e);
        }
    }

    public static Result getPage(String identifier) {
        try {
            return loadAndDecoratePage(identifier, 0);
        } catch (NodeNotFoundException e) {
            return loadPageNotFoundPage();
        } catch (ModuleException e) {
            return loadPageNotFoundPage();
        } catch (Exception e) {
            return handleException(e);
        }
    }

    public static Result getPage(String identifier, int version) {
        try {
            return loadAndDecoratePage(identifier, version);
        } catch (NodeNotFoundException e) {
            return loadPageNotFoundPage();
        } catch (ModuleException e) {
            return loadPageNotFoundPage();
        } catch (Exception e) {
            return handleException(e);
        }
    }

    private static Result handleException(Exception e) {
        if (Play.isDev()) {
            Throwable thrown = e;
            while(thrown instanceof RuntimeException) {
                thrown = e.getCause();
            }
            throw new RuntimeException(thrown);
        }
        Logger.error("An exception occurred while loading the start page: " + e.getMessage(), e);
        return loadPageLoadErrorPage();
    }

    public static Result loadPageNotFoundPage() {
        String pageNotFoundPage = CoreSettingsHelper.getPageNotFoundPage();
        Collection<Alias> aliases = Alias.findWithPageId(pageNotFoundPage);
        String url;
        if (aliases.iterator().hasNext()) {
            Alias alias = aliases.iterator().next();
            url = CoreSettingsHelper.getBaseUrl() + "" + alias.path;
        } else {
            // Defaulting to /page-not-found
            if (CoreSettingsHelper.getBaseUrl() != null) {
                url = CoreSettingsHelper.getBaseUrl() + "page-not-found";
            } else {
                url = "/page-not-found";
            }
        }

        if (url.equalsIgnoreCase(Http.Context.current().request().path())) {
            Logger.warn("Using fallback not-found handling, sending 404 with no content");
            return Controller.notFound();
        }
        Logger.debug("Redirecting to Page-Not-Found Page");
        return Controller.redirect(url);
    }

    public static Result loadPageLoadErrorPage() {
        String internalServerErrorPage = CoreSettingsHelper.getInternalServerErrorPage();
        Collection<Alias> aliases = Alias.findWithPageId(internalServerErrorPage);
        String url;
        if (aliases.iterator().hasNext()) {
            Alias alias = aliases.iterator().next();
            url = CoreSettingsHelper.getBaseUrl() + "" + alias.path;
        } else {
            // Defaulting to /error
            if (CoreSettingsHelper.getBaseUrl() != null) {
                url = CoreSettingsHelper.getBaseUrl() + "error";
            } else {
                url = "/error";
            }
        }

        if (url.equalsIgnoreCase(Http.Context.current().request().path())) {
            Logger.warn("Using fallback error handling, sending 500 with no content");
            return Controller.internalServerError();
        }
        Logger.debug("Redirecting to Internal Error Page");
        return Controller.redirect(url);
    }

    private static Result loadAndDecoratePage(String identifier, int version) throws NodeNotFoundException, NodeLoadException, ModuleException {
        try {
            NodeContext.set();
            Node node = loadNode(identifier, version);
            RenderedNode renderedNode = ThemeHelper.decorate(node, ThemeHelper.loadTheme(node, CoreSettingsHelper.getThemeVariant()));
            renderedNode.navigation(getNavigation(node));
            if (Logger.isDebugEnabled()) {
                Logger.debug("Decorated " + renderedNode);
            }
            return ThemeHelper.render(renderedNode);
        } finally {
            NodeContext.clear();
        }
    }

    private static Node loadNode(String identifier, int version) throws NodeNotFoundException, NodeLoadException, ModuleException {
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

    private static Node loadByNodeIdAndVersion(String identifier, int version) throws NodeNotFoundException, NodeLoadException, ModuleException {
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

    public static List<NavigationElement> getNavigation(Node rootNode) throws NodeNotFoundException, NodeLoadException, ModuleException {
        List<NavigationElement> navigationLinks = NavigationHelper.getNavigation(rootNode, NavigationElement.FRONT);
        if (Logger.isDebugEnabled()) {
            Logger.debug("Navigation loaded " + navigationLinks);
        }
        return navigationLinks;
    }

}

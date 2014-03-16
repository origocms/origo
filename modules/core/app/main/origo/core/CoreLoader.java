package main.origo.core;

import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.core.helpers.NavigationHelper;
import main.origo.core.helpers.NodeHelper;
import main.origo.core.helpers.ThemeHelper;
import main.origo.core.ui.DecoratedNode;
import main.origo.core.ui.NavigationElement;
import main.origo.core.utils.ExceptionUtil;
import models.origo.core.Alias;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.Play;
import play.mvc.Content;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.SimpleResult;

import java.util.Collection;
import java.util.List;

public class CoreLoader {

    public static SimpleResult loadStartPage() {
        String startPage = CoreSettingsHelper.getStartPage();
        try {
            return Controller.ok(loadAndDecorateNode(startPage, 0));
        } catch (NodeNotFoundException e) {
            return redirectToPageNotFoundPage();
        } catch (Exception e) {
            return handleException(e);
        }
    }

    public static SimpleResult loadPage(String identifier) {
        try {
            return Controller.ok(loadAndDecorateNode(identifier, 0));
        } catch (NodeNotFoundException e) {
            return redirectToPageNotFoundPage();
        } catch (ModuleException e) {
            return redirectToPageNotFoundPage();
        } catch (Exception e) {
            return handleException(e);
        }
    }

    public static SimpleResult loadPage(String identifier, int version) {
        try {
            return Controller.ok(loadAndDecorateNode(identifier, version));
        } catch (NodeNotFoundException e) {
            return redirectToPageNotFoundPage();
        } catch (ModuleException e) {
            return redirectToPageNotFoundPage();
        } catch (Exception e) {
            return handleException(e);
        }
    }

    public static SimpleResult handleException(Throwable e) {
        if (Play.isDev()) {
            throw ExceptionUtil.getCause(e);
        }
        Logger.error("An exception occurred while loading the page: " + e.getMessage(), e);
        return redirectToPageLoadErrorPage();
    }

    public static SimpleResult redirectToStartPage() {
        String startPage = CoreSettingsHelper.getStartPage();
        if (StringUtils.isBlank(startPage)) {
            return redirectToPageNotFoundPage();
        }
        return Controller.redirect(CoreSettingsHelper.getBaseUrl());
    }

    public static SimpleResult redirectToPageNotFoundPage() {
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

    public static SimpleResult redirectToPageLoadErrorPage() {
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

    public static Content loadAndDecorateNode(String identifier) throws NodeNotFoundException, NodeLoadException, ModuleException {
        return loadAndDecorateNode(identifier, 0);
    }

    public static Content loadAndDecorateNode(String identifier, int version) throws NodeNotFoundException, NodeLoadException, ModuleException {
        Node node = loadNode(identifier, version);
        return decorateNode(node);
    }

    public static Content decorateNode(Node node) throws NodeNotFoundException, NodeLoadException, ModuleException {
        DecoratedNode decoratedNode = ThemeHelper.decorate(node, ThemeHelper.loadTheme(node, CoreSettingsHelper.getThemeVariant()));
        decoratedNode.navigation(getNavigation(node));
        if (Logger.isDebugEnabled()) {
            Logger.debug("Decorated " + decoratedNode);
        }
        return ThemeHelper.render(decoratedNode);
    }

    public static Node loadNode(String identifier, int version) throws NodeNotFoundException, NodeLoadException, ModuleException {
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

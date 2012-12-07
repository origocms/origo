package com.origocms.core.controllers;

import com.origocms.core.Node;
import com.origocms.core.NodeLoadException;
import com.origocms.core.NodeNotFoundException;
import com.origocms.core.helpers.NavigationHelper;
import com.origocms.core.helpers.NodeHelper;
import com.origocms.core.helpers.SettingsHelper;
import com.origocms.core.helpers.ThemeHelper;
import com.origocms.core.models.Alias;
import com.origocms.core.ui.NavigationElement;
import com.origocms.core.ui.RenderedNode;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Collection;

public class CoreLoader {

    public static RenderedNode getStartPage() throws NodeLoadException {
        String startPage = SettingsHelper.Core.getStartPage();
        try {
            return loadAndDecoratePage(startPage, 0);
        } catch (Exception e) {
            Logger.error("An exception occurred while loading the start page: " + e.getMessage());
            throw new NodeLoadException(startPage, e.getMessage());
        }
    }

    public static RenderedNode getPage(String identifier) throws NodeLoadException {
        try {
            return loadAndDecoratePage(identifier, 0);
        } catch (Exception e) {
            Logger.error("An exception occurred while loading the page [" + identifier + "]: " + e.getMessage());
            throw new NodeLoadException(identifier, e.getMessage());
        }
    }

    public static RenderedNode getPage(String identifier, long version) throws NodeLoadException {
        try {
            return loadAndDecoratePage(identifier, version);
        } catch (Exception e) {
            Logger.error("An exception occurred while loading the page [" + identifier + "] with specific version [" + version + "]: " + e.getMessage());
            throw new NodeLoadException(identifier, e.getMessage());
        }
    }

    public static Result loadPageNotFoundErrorPage() {
        Logger.debug("Redirecting to Page-Not-Found Page");
        String pageNotFoundPage = SettingsHelper.Core.getPageNotFoundPage();
        Collection<Alias> aliases = Alias.findWithPageId(pageNotFoundPage);
        if (aliases.iterator().hasNext()) {
            Alias alias = aliases.iterator().next();
            return Controller.redirect(SettingsHelper.Core.getBaseUrl() + "" + alias.path);
        } else {
            // Defaulting to /page-not-found
            return Controller.redirect(SettingsHelper.Core.getBaseUrl() + "page-not-found");
        }
    }

    public static Result loadPageLoadErrorPage() {
        Logger.debug("Redirecting to Internal Error Page");
        String internalServerErrorPage = SettingsHelper.Core.getInternalServerErrorPage();
        Collection<Alias> aliases = Alias.findWithPageId(internalServerErrorPage);
        if (aliases.iterator().hasNext()) {
            Alias alias = aliases.iterator().next();
            return Controller.redirect(SettingsHelper.Core.getBaseUrl() + "" + alias.path);
        } else {
            // Defaulting to /error
            return Controller.redirect(SettingsHelper.Core.getBaseUrl() + "error");
        }
    }

    private static RenderedNode loadAndDecoratePage(String identifier, long version) throws NodeNotFoundException {
        Node node = loadNode(identifier, version);
        return decorateNode(node);
    }

    private static Node loadNode(String identifier, long version) throws NodeNotFoundException {
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

    private static Node loadByNodeIdAndVersion(String identifier, long version) throws NodeNotFoundException {
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

    private static RenderedNode decorateNode(Node node) {
        RenderedNode renderedNode = ThemeHelper.decorate(node);
        if (Logger.isDebugEnabled()) {
            Logger.debug("Decorated " + renderedNode);
        }
        return renderedNode;
    }

    public static Collection<NavigationElement> getNavigation(String identifier) throws NodeNotFoundException {
        return getNavigation(identifier, 0);
    }

    public static Collection<NavigationElement> getNavigation(String identifier, long version) throws NodeNotFoundException {
        Node node = loadNode(identifier, version);
        Collection<NavigationElement> navigationLinks = NavigationHelper.getNavigation(node, NavigationElement.FRONT);
        if (Logger.isDebugEnabled()) {
            Logger.debug("Navigation loaded " + navigationLinks);
        }
        return navigationLinks;
    }

}
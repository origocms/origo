package main.origo.admin.helpers;

import com.google.common.collect.Maps;
import main.origo.admin.annotations.Admin;
import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Core;
import main.origo.core.event.OnLoadEventGenerator;
import main.origo.core.ui.Element;

import java.util.Collections;

public class TabHelper {

    /**
     * Creates a TabBar element. Skips the normal Provides routine since this is a local type meant for Admin only.
     *
     * @param node
     * @return
     * @throws ModuleException
     * @throws NodeLoadException
     */
    public static Admin.TabBar createTabBar(Node node) throws ModuleException, NodeLoadException {

        OnLoadEventGenerator.triggerBeforeInterceptor(node, Core.Type.NODE, Admin.With.TAB_BAR, Maps.<String, Object>newHashMap());

        Admin.TabBar bar = new Admin.TabBar();

        OnLoadEventGenerator.triggerAfterInterceptor(node, Core.Type.NODE, Admin.With.TAB_BAR, bar, Collections.<String, Object>emptyMap());

        return bar;
    }

    /**
     * Creates a TabContent element. Skips the normal Provides routine since this is a local type meant for Admin only.
     *
     * @param node
     * @return
     * @throws ModuleException
     * @throws NodeLoadException
     */
    public static Admin.TabContent createTabContent(Node node) throws ModuleException, NodeLoadException {

        OnLoadEventGenerator.triggerBeforeInterceptor(node, Core.Type.NODE, Admin.With.TAB_CONTENT, Maps.<String, Object>newHashMap());

        Admin.TabContent content = new Admin.TabContent();

        OnLoadEventGenerator.triggerAfterInterceptor(node, Core.Type.NODE, Admin.With.TAB_CONTENT, content, Collections.<String, Object>emptyMap());
        return content;
    }

    public static void addTabScript(Node node, Element element) {
        node.addTailElement(
                new Element.Script().addAttribute("type", "text/javascript").setBody(
                        "$(document).ready(function () { $('#" + element.getId() + "').tab() })")
        );
    }

}

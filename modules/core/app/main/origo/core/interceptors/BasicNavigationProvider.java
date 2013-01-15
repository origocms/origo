package main.origo.core.interceptors;

import com.google.common.collect.Lists;
import main.origo.core.Node;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.event.NavigationEventGenerator;
import main.origo.core.event.ProvidesEventGenerator;
import main.origo.core.ui.NavigationElement;
import models.origo.core.Alias;
import models.origo.core.RootNode;
import models.origo.core.navigation.AliasNavigation;
import models.origo.core.navigation.BasicNavigation;
import models.origo.core.navigation.ExternalLinkNavigation;
import models.origo.core.navigation.PageIdNavigation;

import java.util.Date;
import java.util.List;

/**
 * Standard implementation of navigation. An alternate navigation provider can be used by changing the the navigation
 * type in the settings. This provider uses a standard tree based navigation stored in a database with parent->child
 * relationships. It provides the type BasicNavigation.
 *
 * @see models.origo.core.navigation.BasicNavigation
 * @see models.origo.core.navigation.AliasNavigation
 * @see models.origo.core.navigation.PageIdNavigation
 * @see models.origo.core.navigation.ExternalLinkNavigation
 */
@Interceptor
public class BasicNavigationProvider {

    @Provides(type = Core.Type.NAVIGATION, with = "models.origo.core.navigation.BasicNavigation")
    public static List<NavigationElement> createNavigation(Provides.Context context) {
        List<NavigationElement> navigationElements = Lists.newArrayList();
        String section = (String) context.args.get("section");
        NavigationEventGenerator.triggerBeforeNavigationLoaded(context.node, BasicNavigation.class.getName(), section);
        List<BasicNavigation> navigationModels = BasicNavigation.findWithSection(section);
        for (BasicNavigation navigationModel : navigationModels) {
            NavigationEventGenerator.triggerBeforeNavigationItemLoaded(context.node, navigationModel.type, navigationModel);
            NavigationElement navigationElement = NavigationEventGenerator.triggerProvidesNavigationItemInterceptor(context.node, navigationModel.type, navigationModel);
            NavigationEventGenerator.triggerAfterNavigationItemLoaded(context.node, navigationModel.type, navigationModel, navigationElement);
            List<NavigationElement> children = createNavigationChildren(context.node, section, navigationModel, navigationElement);
            navigationElement.children.addAll(children);
            navigationElements.add(navigationElement);
        }
        NavigationEventGenerator.triggerAfterNavigationLoaded(context.node, BasicNavigation.class.getName(), navigationElements, section);
        return navigationElements;
    }

    private static List<NavigationElement> createNavigationChildren(Node node, String section, BasicNavigation navigationModel, NavigationElement parentNavigationElement) {
        List<NavigationElement> navigationElements = Lists.newArrayList();
        List<BasicNavigation> navigationModels = BasicNavigation.findWithSection(section, navigationModel);
        for (BasicNavigation childNavigation : navigationModels) {
            NavigationEventGenerator.triggerBeforeNavigationItemLoaded(node, childNavigation.type, childNavigation);
            NavigationElement childNavigationElement = NavigationEventGenerator.triggerProvidesNavigationItemInterceptor(node, childNavigation.type, childNavigation, parentNavigationElement);
            if (childNavigationElement != null) {
                NavigationEventGenerator.triggerAfterNavigationItemLoaded(node, childNavigation.type, childNavigation, childNavigationElement);
                if (childNavigationElement.selected) {
                    parentNavigationElement.selected = true;
                }
                navigationElements.add(childNavigationElement);
            }
        }
        return navigationElements;
    }

    @Provides(type = Core.Type.NAVIGATION_ITEM, with = "models.origo.core.navigation.AliasNavigation")
    public static NavigationElement createAliasNavigation(Provides.Context context) {
        AliasNavigation navigationModel = AliasNavigation.findWithIdentifier(context.navigation.getReferenceId());
        Alias alias = Alias.findWithPath(navigationModel.alias);
        if (alias != null) {
            RootNode referencedRootNode = RootNode.findLatestPublishedVersionWithNodeId(alias.pageId, new Date());
            if (referencedRootNode != null) {
                Node referencedNode = ProvidesEventGenerator.triggerInterceptor(referencedRootNode, Core.Type.NODE, referencedRootNode.nodeType);
                boolean selected = context.node.getNodeId().equals(alias.pageId);
                return new NavigationElement(context.navigation.getSection(), referencedNode.getTitle(), navigationModel.getLink(), context.navigation.getWeight(), selected);
            } else {
                throw new RuntimeException("Page not found [" + alias.pageId + "]");
            }
        } else {
            throw new RuntimeException("Alias not found [" + navigationModel.alias + "]");
        }
    }

    @Provides(type = Core.Type.NAVIGATION_ITEM, with = "models.origo.core.navigation.PageIdNavigation")
    public static NavigationElement createPageIdNavigation(Provides.Context context) {
        PageIdNavigation navigationModel = PageIdNavigation.findWithIdentifier(context.navigation.getReferenceId());
        RootNode referencedRootNode = RootNode.findLatestPublishedVersionWithNodeId(navigationModel.pageId, new Date());
        if (referencedRootNode != null) {
            Node referencedNode = ProvidesEventGenerator.triggerInterceptor(referencedRootNode, Core.Type.NODE, referencedRootNode.nodeType);
            boolean selected = context.node.getNodeId().equals(referencedRootNode.getNodeId());
            return new NavigationElement(context.navigation.getSection(), referencedNode.getTitle(), navigationModel.getLink(), context.navigation.getWeight(), selected);
        } else {
            throw new RuntimeException("Page not found [" + navigationModel.pageId + "]");
        }
    }

    @Provides(type = Core.Type.NAVIGATION_ITEM, with = "models.origo.core.navigation.ExternalLinkNavigation")
    public static NavigationElement createExternalLinkNavigation(Provides.Context context) {
        ExternalLinkNavigation navigationModel = ExternalLinkNavigation.findWithIdentifier(context.navigation.getReferenceId());
        if (navigationModel != null) {
            return new NavigationElement(context.navigation.getSection(), navigationModel.title, navigationModel.getLink(), context.navigation.getWeight());
        }
        return null;
    }

}

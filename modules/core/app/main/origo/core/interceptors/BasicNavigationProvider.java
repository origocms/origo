package main.origo.core.interceptors;

import com.google.common.collect.Lists;
import main.origo.core.ModuleException;
import main.origo.core.Navigation;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.event.NavigationEventGenerator;
import main.origo.core.event.ProvidesEventGenerator;
import main.origo.core.ui.NavigationElement;
import models.origo.core.RootNode;
import models.origo.core.navigation.BasicNavigation;
import models.origo.core.navigation.ExternalLinkNavigation;
import models.origo.core.navigation.GroupHolderNavigation;
import models.origo.core.navigation.InternalPageIdNavigation;
import play.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Standard implementation of navigation. An alternate navigation provider can be used by changing the the navigation
 * type in the settings. This provider uses a standard tree based navigation stored in a database with parent->child
 * relationships. It provides the type BasicNavigation.
 *
 * @see models.origo.core.navigation.BasicNavigation
 * @see models.origo.core.navigation.InternalPageIdNavigation
 * @see models.origo.core.navigation.ExternalLinkNavigation
 */
@Interceptor
public class BasicNavigationProvider {

    @Provides(type = Core.Type.NAVIGATION, with = BasicNavigation.TYPE)
    public static List<NavigationElement> createNavigation(Node node, String withType, Map<String, Object> args) throws NodeLoadException, ModuleException {
        List<NavigationElement> navigationElements = Lists.newArrayList();
        String section = (String) args.get("section");
        NavigationEventGenerator.triggerBeforeNavigationLoaded(node, BasicNavigation.class.getName(), section);
        List<BasicNavigation> navigationModels = BasicNavigation.findWithSectionWithoutParent(section);
        for (BasicNavigation navigationModel : navigationModels) {
            NavigationEventGenerator.triggerBeforeNavigationItemLoaded(node, navigationModel.type, navigationModel);
            NavigationElement navigationElement = NavigationEventGenerator.triggerProvidesNavigationItemInterceptor(node, navigationModel.type, navigationModel);
            if (navigationElement != null) {
                NavigationEventGenerator.triggerAfterNavigationItemLoaded(node, navigationModel.type, navigationModel, navigationElement);
                List<NavigationElement> children = createNavigationChildren(node, section, navigationModel, navigationElement);
                Collections.sort(children);
                navigationElement.children.addAll(children);
                navigationElements.add(navigationElement);
            }
        }
        NavigationEventGenerator.triggerAfterNavigationLoaded(node, BasicNavigation.class.getName(), (Navigation) args.get("navigation"), navigationElements, section);
        return navigationElements;
    }

    private static List<NavigationElement> createNavigationChildren(Node node, String section, BasicNavigation navigationModel, NavigationElement parentNavigationElement) throws NodeLoadException, ModuleException {
        List<NavigationElement> navigationElements = Lists.newArrayList();
        List<BasicNavigation> navigationModels = BasicNavigation.findWithSection(section, navigationModel);
        for (BasicNavigation childNavigation : navigationModels) {
            NavigationEventGenerator.triggerBeforeNavigationItemLoaded(node, childNavigation.type, childNavigation);
            NavigationElement childNavigationElement = NavigationEventGenerator.triggerProvidesNavigationItemInterceptor(node, childNavigation.type, childNavigation, parentNavigationElement);
            if (childNavigationElement != null) {
                NavigationEventGenerator.triggerAfterNavigationItemLoaded(node, childNavigation.type, childNavigation, childNavigationElement);
                if (childNavigationElement.selected) {
                    parentNavigationElement.childSelected = true;
                }
                navigationElements.add(childNavigationElement);
            }
        }
        Collections.sort(navigationElements);
        return navigationElements;
    }

    @Provides(type = Core.Type.NAVIGATION_ITEM, with = InternalPageIdNavigation.TYPE)
    public static NavigationElement createPageIdNavigation(Node node, String withType, Navigation navigation, Map<String, Object> args) throws ModuleException, NodeLoadException {
        InternalPageIdNavigation navigationModel = InternalPageIdNavigation.findWithIdentifier(navigation.getReferenceId());
        RootNode referencedRootNode = RootNode.findLatestPublishedVersionWithNodeId(navigationModel.pageId);
        try {
            if (referencedRootNode != null) {
                Node referencedNode = ProvidesEventGenerator.triggerInterceptor(referencedRootNode, Core.Type.NODE, referencedRootNode.nodeType());
                if (referencedNode != null) {
                    boolean selected = node.nodeId().equals(referencedRootNode.nodeId());
                    NavigationElement ne = new NavigationElement();
                    ne.id = navigationModel.identifier;
                    ne.section = navigation.getSection();
                    ne.title = referencedNode.title();
                    ne.link = navigationModel.getLink();
                    ne.weight = navigation.getWeight();
                    ne.selected = selected;
                    return ne;
                } else {
                    throw new RuntimeException("Page referenced in navigation not found [" + navigationModel.pageId + "]");
                }
            } else {
                RootNode rootNode = RootNode.findLatestVersionWithNodeId(navigationModel.pageId);
                if (rootNode != null) {
                    Logger.info("Navigation with referenceId='"+navigation.getReferenceId()+"' refers to a non published page. Ignoring.");
                    return null;
                } else {
                    throw new RuntimeException("Referenced node not found [" + navigationModel.pageId + "]");
                }
            }
        } catch (ModuleException e) {
            if (e.cause == ModuleException.Cause.NOT_ENABLED) {
                Logger.info("Navigation with referenceId='"+navigation.getReferenceId()+"' refers to a module that is disabled. Ignoring.");
                return null;
            } else {
                throw e;
            }
        }
    }

    @Provides(type = Core.Type.NAVIGATION_ITEM, with = ExternalLinkNavigation.TYPE)
    public static NavigationElement createExternalLinkNavigation(Node node, String withType, Navigation navigation, Map<String, Object> args) {
        ExternalLinkNavigation navigationModel = ExternalLinkNavigation.findWithIdentifier(navigation.getReferenceId());
        if (navigationModel != null) {
            NavigationElement ne = new NavigationElement();
            ne.id = navigationModel.identifier;
            ne.section = navigation.getSection();
            ne.title = navigationModel.title;
            ne.link = navigationModel.getLink();
            ne.weight = navigation.getWeight();
            return ne;
        }
        return null;
    }

    @Provides(type = Core.Type.NAVIGATION_ITEM, with = GroupHolderNavigation.TYPE)
    public static NavigationElement createGroupHolderNavigation(Node node, String withType, Navigation navigation, Map<String, Object> args) {
        GroupHolderNavigation navigationModel = GroupHolderNavigation.findWithIdentifier(navigation.getReferenceId());
        if (navigationModel != null) {
            NavigationElement ne = new NavigationElement();
            ne.id = navigationModel.identifier;
            ne.section = navigation.getSection();
            ne.title = navigationModel.title;
            ne.weight = navigation.getWeight();
            return ne;
        }
        return null;
    }

}

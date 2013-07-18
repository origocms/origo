package main.origo.core.helpers;

import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.event.NavigationEventGenerator;
import main.origo.core.ui.NavigationElement;

import java.util.List;

public class NavigationHelper {

    public static List<NavigationElement> getNavigation(Node node, String section) throws NodeLoadException, ModuleException {
        String navigationType = CoreSettingsHelper.getNavigationType();
        return NavigationEventGenerator.triggerProvidesNavigationInterceptor(node, navigationType, section);
    }

}

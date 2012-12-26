package main.origo.core.helpers;

import main.origo.core.Node;
import main.origo.core.event.NavigationEventGenerator;
import main.origo.core.ui.NavigationElement;

import java.util.List;

public class NavigationHelper {

    public static List<NavigationElement> getNavigation(Node node, String section) {
        String navigationType = CoreSettingsHelper.getNavigationType();
        return NavigationEventGenerator.triggerProvidesNavigationInterceptor(navigationType, node, section);
    }

}

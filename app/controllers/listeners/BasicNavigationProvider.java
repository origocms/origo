package controllers.listeners;

import models.RootNode;
import controllers.annotations.Listener;
import controllers.annotations.Provides;
import controllers.annotations.Types;
import controllers.ui.NavigationElement;
import play.Logger;

import java.util.Collection;
import java.util.Collections;

@Listener
public class BasicNavigationProvider {

    @Provides(type = Types.NAVIGATION, with = "models.navigation.BasicNavigation")
    public static Collection<NavigationElement> provide(String type, RootNode rootNode) {
        Logger.debug("TADA");
        return Collections.emptyList();
    }

}
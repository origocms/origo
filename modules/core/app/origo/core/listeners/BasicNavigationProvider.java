package origo.core.listeners;

import controllers.origo.core.Types;
import models.origo.core.RootNode;
import origo.core.annotations.Listener;
import origo.core.annotations.Provides;
import origo.core.ui.NavigationElement;
import play.Logger;

import java.util.Collection;
import java.util.Collections;

@Listener
public class BasicNavigationProvider {

    @Provides(type = Types.NAVIGATION, with = "models.origo.core.navigation.BasicNavigation")
    public static Collection<NavigationElement> provide(String type, RootNode rootNode) {
        Logger.debug("TADA");
        return Collections.emptyList();
    }

}
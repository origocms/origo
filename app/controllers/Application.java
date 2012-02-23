package controllers;

import controllers.origo.core.CoreLoader;
import origo.core.ui.RenderedNode;
import play.db.ebean.Transactional;
import play.mvc.*;

import views.html.*;
import views.html.index;

import java.util.Collection;

public class Application extends Controller {

    @Transactional
    public static Result index() {
        //TODO: Check if config !exists and redirect to wizard

        //RenderedNode node = CoreLoader.getStartPage();
        //Collection<NavigationElement> navigation = CoreLoader.getNavigation(node.getId());
        //return ok("bla bla");
        //return ok(origo.core.html.defaultTheme.render(node.getTitle(), "test"));
        //origo.core.defaultTheme.render()
        //render(node.getTemplate(), node, navigation);
        //render(node.getTemplate(), node, navigation);

        return ok(index.render("Your new application is ready."));
    }

    /*
    public static void page(String identifier) {
        //TODO: Check if config !exists and redirect to wizard

        RenderedNode node = CoreLoader.getPage(identifier);
        Collection<NavigationElement> navigation = CoreLoader.getNavigation(identifier);
        render(node.getTemplate(), node, navigation);
    }

    public static void pageVersion(String identifier, long version) {
        //TODO: Check if config !exists and redirect to wizard

        RenderedNode node = CoreLoader.getPage(identifier, version);
        Collection<NavigationElement> navigation = CoreLoader.getNavigation(identifier, version);
        render(node.getTemplate(), node, navigation);
    }
    */
}
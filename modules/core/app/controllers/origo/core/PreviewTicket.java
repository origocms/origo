package controllers.origo.core;

import be.objectify.deadbolt.java.actions.Dynamic;
import main.origo.core.actions.ContextAware;
import main.origo.core.helpers.SessionHelper;
import main.origo.core.security.Security;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class PreviewTicket extends Controller {

    private static final String SESSION_PREVIEW_TICKET_KEY = "origo.session.preview_ticket";

    @Transactional
    @Dynamic(Security.Types.RESOURCE)
    @ContextAware
    public static Result get() {

        String previewTicket = SessionHelper.get(SESSION_PREVIEW_TICKET_KEY);

        return TODO;
    }

    @Transactional
    @Dynamic(Security.Types.RESOURCE)
    @ContextAware
    public static Result remove() {

        return TODO;

    }

}

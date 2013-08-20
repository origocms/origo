package controllers.origo.core;

import be.objectify.deadbolt.java.actions.Dynamic;
import main.origo.core.User;
import main.origo.core.actions.ContextAware;
import main.origo.core.event.NodeContext;
import main.origo.core.formatters.Formats;
import main.origo.core.helpers.SessionHelper;
import main.origo.core.security.Security;
import models.origo.core.PreviewTicket;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDateTime;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.origo.core.previewtoken.index;
import views.html.origo.core.previewtoken.results;

import static play.data.Form.form;

public class PreviewToken extends Controller {

    private static final String SESSION_PREVIEW_TICKET_KEY = "origo.session.preview_ticket";

    @Transactional
    @ContextAware
    @Dynamic(Security.Types.RESOURCE)
    public static Result index() {
        return ok(index.render(PreviewTokenForm.fill(new PreviewTicket())));
    }

    @Transactional
    @ContextAware
    @Dynamic(Security.Types.RESOURCE)
    public static Result get() {

        User user = (User) NodeContext.current().attributes.get(Security.Params.AUTH_USER);
        String previewToken = SessionHelper.get(SESSION_PREVIEW_TICKET_KEY);
        if (StringUtils.isNotBlank(previewToken)) {
            PreviewTicket ticket = PreviewTicket.getIfValid(user, previewToken);
            if (ticket != null) {
                return ok(results.render(ticket));
            }
        }

        return redirect(routes.PreviewToken.index());
    }

    @Transactional
    @ContextAware
    @Dynamic(Security.Types.RESOURCE)
    public static Result submit() {

        Form<PreviewTokenForm> previewTokenForm = form(PreviewTokenForm.class).bindFromRequest();

        if(previewTokenForm.hasErrors()) {
            return badRequest(index.render(previewTokenForm));
        }

        if (previewTokenForm.get().preview.isBefore(LocalDateTime.now())) {
            return badRequest(index.render(previewTokenForm));
        }

        User user = (User) NodeContext.current().attributes.get(Security.Params.AUTH_USER);
        String previewToken = SessionHelper.get(SESSION_PREVIEW_TICKET_KEY);
        if (StringUtils.isNotBlank(previewToken)) {
            PreviewTicket ticket = PreviewTicket.getIfValid(user, previewToken);
            if (ticket != null) {
                ticket.preview = previewTokenForm.get().preview;
                ticket.update();
                return ok(results.render(ticket));
            }
        }

        PreviewTicket ticket = PreviewTicket.createInstance(user, previewTokenForm.get().preview);
        SessionHelper.set(SESSION_PREVIEW_TICKET_KEY, ticket.token);
        return ok(results.render(ticket));
    }

    @Transactional
    @ContextAware
    @Dynamic(Security.Types.RESOURCE)
    public static Result remove() {

        return TODO;

    }

    public static class PreviewTokenForm {

        @Formats.DateTimePattern(key="date.format")
        public LocalDateTime preview;

        public static Form<PreviewTokenForm> fill(PreviewTicket ticket) {
            PreviewTokenForm form = new PreviewTokenForm();
            form.preview = ticket.preview;
            return new Form(PreviewTokenForm.class).fill(form);
        }

    }
}

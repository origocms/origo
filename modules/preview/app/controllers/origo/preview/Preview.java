package controllers.origo.preview;

import be.objectify.deadbolt.java.actions.Dynamic;
import forms.origo.preview.PreviewTokenForm;
import main.origo.core.ModuleException;
import main.origo.core.NodeLoadException;
import main.origo.core.actions.ComponentWrapper;
import main.origo.core.actions.ContextAware;
import main.origo.core.security.Security;
import main.origo.preview.helpers.PreviewTicketHelper;
import models.origo.preview.BasicTicket;
import org.joda.time.DateTime;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import views.html.origo.preview.index;
import views.html.origo.preview.results;

import static play.data.Form.form;

public class Preview extends Controller {


    @Transactional
    @ContextAware
    @Dynamic(Security.Types.RESOURCE)
    @ComponentWrapper
    public static Result index() {
        return ok(index.render(PreviewTokenForm.fill(new BasicTicket())));
    }

    @Transactional
    @ContextAware
    @Dynamic(Security.Types.RESOURCE)
    @ComponentWrapper
    public static Result get() throws ModuleException, NodeLoadException {

        BasicTicket basicTicket = PreviewTicketHelper.getCurrent();
        if (basicTicket != null) {
            return ok(results.render(basicTicket));
        }

        return Results.redirect(controllers.origo.preview.routes.Preview.index());
    }

    @Transactional
    @ContextAware
    @Dynamic(Security.Types.RESOURCE)
    @ComponentWrapper
    public static Result submit() throws NodeLoadException, ModuleException {

        Form<PreviewTokenForm> previewTokenForm = form(PreviewTokenForm.class).bindFromRequest();

        if(previewTokenForm.hasErrors()) {
            return badRequest(index.render(previewTokenForm));
        }

        if (previewTokenForm.get().preview.isBefore(DateTime.now())) {
            return badRequest(index.render(previewTokenForm));
        }

        BasicTicket basicTicket = PreviewTicketHelper.updateTicket(previewTokenForm.get().getPreview());
        if (basicTicket != null) {
            return ok(results.render(basicTicket));
        }

        basicTicket = PreviewTicketHelper.createNewTicket(previewTokenForm.get().getPreview());
        return ok(results.render(basicTicket));
    }

    @Transactional
    @ContextAware
    @Dynamic(Security.Types.RESOURCE)
    @ComponentWrapper
    public static Result remove() {

        return TODO;

    }

}

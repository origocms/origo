package main.origo.preview.interceptors;

import forms.origo.preview.PreviewTokenForm;
import main.origo.authentication.form.LoginForm;
import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnLoad;
import main.origo.core.annotations.Provides;
import main.origo.core.annotations.forms.OnSubmit;
import main.origo.core.event.NodeContext;
import main.origo.core.helpers.forms.FormHelper;
import main.origo.core.preview.Ticket;
import main.origo.core.ui.Element;
import main.origo.preview.helpers.PreviewTicketHelper;
import models.origo.preview.BasicTicket;
import play.data.Form;
import play.i18n.Messages;

import java.util.Map;

@Interceptor
public class PreviewTokenInterceptor {

    private static final String DATETIME_PARAM = "date";

    private static final String COMMENT_LOADED = "preview_comment_loaded";

    @OnLoad(with = Core.With.PREVIEW_CREATE)
    public static void addPreviewForm(Node node, String withType) throws ModuleException, NodeLoadException {

        Form<LoginForm> form = FormHelper.getValidationResult(LoginForm.class);

        FormHelper.createFormElement(node, withType, form);
    }

    @OnLoad(type = Core.Type.FORM, with = Core.With.PREVIEW_CREATE, after = true)
    public static void addPreviewForm(Node node, String withType, Form form, Element element, Map<String, Object> args) {

        element.setId("previewtokenform").addAttribute("class", "origo-previewtokenform, form");

        Element globalErrors = FormHelper.createGlobalErrorElement();
        if (globalErrors != null) {
            element.addChild(globalErrors);
        }

        Element previewFieldSet = new Element.FieldSet().setId("preview");
        element.addChild(previewFieldSet);

        previewFieldSet.addChild(new Element.Legend().setBody("Preview"));

        previewFieldSet.addChild(new Element.Container().
                addChild(FormHelper.createField(
                        form,
                        new Element.Label().setWeight(10).setBody("Date").addAttribute("for", DATETIME_PARAM),
                        new Element.InputText().setWeight(20).addAttribute("name", DATETIME_PARAM)).
                        setWeight(10).
                        addChild(
                                new Element.Help().setWeight(1000).setBody(Messages.get("date.time.format"))
                        )
                )
        );

        element.addChild(new Element.Container().setId("actions").setWeight(1000).
                addChild(new Element.Container().
                        addChild(new Element.InputSubmit().setWeight(10).addAttribute("class", "btn btn-primary").addAttribute("value", "Create"))
                ));
    }

    @OnSubmit(with = Core.With.PREVIEW_CREATE, validate = PreviewTokenForm.class)
    public static Boolean handlePreviewSubmit(Form<LoginForm> loginForm) throws NodeLoadException, ModuleException {

        Form<PreviewTokenForm> form = FormHelper.getValidationResult(PreviewTokenForm.class);

        BasicTicket basicTicket = PreviewTicketHelper.updateTicket(form.get().getPreview());
        if (basicTicket != null) {
            return true;
        }

        basicTicket = PreviewTicketHelper.createNewTicket(form.get().getPreview());
        return basicTicket != null;
    }

    @OnLoad(type = Core.Type.NODE)
    public static void loadNode(Node node, String withType, Map<String, Object> args) throws ModuleException, NodeLoadException {
        if (!NodeContext.current().attributes.containsKey(COMMENT_LOADED) && PreviewTicketHelper.hasTicket() && PreviewTicketHelper.verifyCurrent()) {
            node.addHeadElement(new Element.Comment().setBody(getComment()));
            NodeContext.current().attributes.put(COMMENT_LOADED, true);
        }
    }

    private static String getComment() throws ModuleException, NodeLoadException {
        BasicTicket basicTicket = PreviewTicketHelper.getCurrent();
        return "Preview-Ticket { token: \""+ basicTicket.token+"\", valid-until: \"\\/Date("+ basicTicket.validUntil().getMillis()+")\\/\" }";
    }

    @Provides(type = Core.Type.PREVIEW, with = Core.With.PREVIEW_TOKEN)
    public static Ticket getCurrentToken(Node node, String withType, Map<String, Object> args) throws ModuleException, NodeLoadException {
        return PreviewTicketHelper.getCurrent();
    }
}

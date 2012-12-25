package main.origo.core.interceptors.forms;

import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.forms.OnLoadForm;
import main.origo.core.annotations.forms.SubmitHandler;
import main.origo.core.annotations.forms.SubmitState;
import main.origo.core.event.forms.OnSubmitEventGenerator;
import main.origo.core.event.forms.SubmitHandlerEventGenerator;
import main.origo.core.event.forms.SubmitStateEventGenerator;
import main.origo.core.ui.Element;
import play.Logger;
import play.data.DynamicForm;
import play.mvc.Result;

/**
 * Default implementation of the submit handler. Alternate submit handlers can be used by changing the settings.
 * This submit handler is based on a 'type' which is added to the form using an \@OnLoad hook by this handler.
 */
@Interceptor
public class DefaultSubmitHandler {

    private static final String WITH_TYPE = "_core_with_type";

    @SubmitHandler
    public static Result handleSubmit(SubmitHandler.Context context) {

        DynamicForm form = DynamicForm.form().bindFromRequest();

        String withType = getWithType(form);
        if (withType == null) {
            Logger.error("DefaultSubmitHandler requires a request parameter  named \'" + WITH_TYPE + "\' to be present in the request");
        }

        // TODO: insert validation here

        OnSubmitEventGenerator.triggerInterceptors(withType, form);

        return SubmitStateEventGenerator.triggerInterceptor(SubmitState.SUCCESS, withType, form);
    }

    @OnLoadForm
    public static void addWithTypeField(OnLoadForm.Context context) {
        if (DefaultSubmitHandler.class.isAssignableFrom(SubmitHandlerEventGenerator.getActiveSubmitHandler())) {
            context.formElement.addChild(new Element.InputHidden().addAttribute("name", WITH_TYPE).addAttribute("value", context.withType));
        }
    }

    public static String getWithType(DynamicForm form) {
        return form.get(WITH_TYPE);
    }

}

package main.origo.core.interceptors.forms;

import com.google.common.collect.Maps;
import main.origo.core.InitializationException;
import main.origo.core.ModuleException;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnLoad;
import main.origo.core.annotations.forms.SubmitHandler;
import main.origo.core.annotations.forms.SubmitState;
import main.origo.core.annotations.forms.ValidationHandler;
import main.origo.core.event.forms.OnSubmitEventGenerator;
import main.origo.core.event.forms.SubmitHandlerEventGenerator;
import main.origo.core.event.forms.SubmitStateEventGenerator;
import main.origo.core.event.forms.ValidationHandlerEventGenerator;
import main.origo.core.ui.Element;
import play.Logger;
import play.data.DynamicForm;
import play.mvc.Result;

import java.util.Map;

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
            Logger.error("DefaultSubmitHandler requires a request parameter named '" + WITH_TYPE + "' to be present in the request");
        }

        ValidationHandler.Result validationResult;
        final String postHandlerName = ValidationHandlerEventGenerator.getRegisteredValidationHandlerName();
        try {
            validationResult = ValidationHandlerEventGenerator.triggerValidationHandler(postHandlerName, withType);
        } catch (Exception e) {
            Map<String, Object> args = Maps.newHashMap();
            args.put("reason", getReason(e));
            args.put("exception", e);
            return SubmitStateEventGenerator.triggerInterceptor(SubmitState.FAILURE, withType, args);
        }

        if (validationResult.hasErrors()) {
            return SubmitStateEventGenerator.triggerInterceptor(SubmitState.VALIDATION, withType);
        }
        try {
            if (OnSubmitEventGenerator.triggerInterceptors(withType)) {
                return SubmitStateEventGenerator.triggerInterceptor(SubmitState.SUCCESS, withType);
            } else {
                return SubmitStateEventGenerator.triggerInterceptor(SubmitState.FAILURE, withType);
            }
        } catch (Exception e) {
            Map<String, Object> args = Maps.newHashMap();
            args.put("reason", getReason(e));
            args.put("exception", e);
            return SubmitStateEventGenerator.triggerInterceptor(SubmitState.FAILURE, withType, args);
        }
    }

    private static String getReason(Exception e) {
        Throwable thrown = e;
        while(thrown.getCause() != null &&
                (thrown instanceof InitializationException ||
                        thrown instanceof ModuleException ||
                        thrown instanceof RuntimeException)) {
            thrown = e.getCause();
        }
        return thrown.getMessage();
    }

    @OnLoad(type = Core.Type.FORM)
    public static void addWithTypeField(OnLoad.Context context) {
        if (DefaultSubmitHandler.class.isAssignableFrom(SubmitHandlerEventGenerator.getActiveSubmitHandler())) {
            Element element = (Element) context.args.get("element");
            element.addChild(new Element.InputHidden().addAttribute("name", WITH_TYPE).addAttribute("value", context.withType));
        }
    }

    public static String getWithType(DynamicForm form) {
        return form.get(WITH_TYPE);
    }

}

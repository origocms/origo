package main.origo.core.interceptors.forms;

import controllers.origo.core.CoreLoader;
import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.NodeNotFoundException;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnLoad;
import main.origo.core.annotations.forms.SubmitHandler;
import main.origo.core.annotations.forms.SubmitState;
import main.origo.core.annotations.forms.Validation;
import main.origo.core.event.NodeContext;
import main.origo.core.event.forms.OnSubmitEventGenerator;
import main.origo.core.event.forms.SubmitStateEventGenerator;
import main.origo.core.event.forms.ValidationHandlerEventGenerator;
import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.core.ui.Element;
import models.origo.core.RootNode;
import org.apache.commons.lang3.StringUtils;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;

import java.lang.reflect.InvocationTargetException;

/**
 * Default implementation of the submit handler. Alternate submit handlers can be used by changing the settings.
 * This submit handler is based on a 'type' which is added to the form using an \@OnLoad hook by this handler.
 */
@Interceptor
public class DefaultSubmitHandler {

    protected static final String WITH_TYPE = "_core_with_type";

    @SubmitHandler
    public static Result handleSubmit(SubmitHandler.Context context) {

        String withType = getWithType();

        try {
            Validation.Result validationResult = runValidation(context, withType);

            if (validationResult.hasErrors()) {
                return handleValidationFailure(withType, validationResult);
            }
            return sendSubmitState(withType, validationResult);

        } catch (ModuleException | NodeLoadException | InvocationTargetException | IllegalAccessException | NodeNotFoundException e) {
            return CoreLoader.handleException(e);
        }
    }

    protected static String getWithType() {
        DynamicForm form = DynamicForm.form().bindFromRequest();

        String withType = getWithType(form);
        if (withType == null) {
            throw new RuntimeException("DefaultSubmitHandler requires a request parameter named '" + WITH_TYPE + "' to be present in the request");
        }
        return withType;
    }

    protected static Validation.Result runValidation(SubmitHandler.Context context, String withType) throws ModuleException, NodeLoadException, InvocationTargetException, IllegalAccessException {
        Validation.Result validationResult = ValidationHandlerEventGenerator.triggerValidationProcessingHandler(withType);
        context.attributes.put(Validation.Result.class.getCanonicalName(), validationResult);
        return validationResult;
    }

    protected static Result handleValidationFailure(String withType, Validation.Result validationResult) throws InvocationTargetException, IllegalAccessException, NodeNotFoundException, NodeLoadException, ModuleException {
        RootNode rootNode = new RootNode(0, withType);
        NodeContext.current().node = rootNode;
        Node node = ValidationHandlerEventGenerator.triggerValidationFailedHandler(rootNode, withType, validationResult);
        return Controller.badRequest(CoreLoader.decorateNode(node));
    }

    protected static Result sendSubmitState(String withType, Validation.Result validationResult) throws NodeLoadException, ModuleException {
        if (OnSubmitEventGenerator.triggerInterceptors(withType, validationResult)) {
            return SubmitStateEventGenerator.triggerInterceptor(SubmitState.SUCCESS, withType);
        } else {
            return SubmitStateEventGenerator.triggerInterceptor(SubmitState.FAILURE, withType);
        }
    }

    @OnLoad(type = Core.Type.FORM)
    public static void addWithTypeField(OnLoad.Context context) {
        final String postHandler = CoreSettingsHelper.getSubmitHandler();
        if (StringUtils.isBlank(postHandler)) {
            throw new RuntimeException("No SubmitHandler defined in settings: "+CoreSettingsHelper.Keys.SUBMIT_HANDLER);
        }
        if (DefaultSubmitHandler.class.getName().equals(postHandler)) {
            Element element = (Element) context.args.get("element");
            element.addChild(new Element.InputHidden().addAttribute("name", WITH_TYPE).addAttribute("value", context.withType));
        }
    }

    public static String getWithType(DynamicForm form) {
        return form.get(WITH_TYPE);
    }

}

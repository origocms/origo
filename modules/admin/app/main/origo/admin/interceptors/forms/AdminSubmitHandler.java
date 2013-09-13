package main.origo.admin.interceptors.forms;

import controllers.origo.admin.AdminLoader;
import controllers.origo.core.CoreLoader;
import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.NodeNotFoundException;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.forms.SubmitHandler;
import main.origo.core.annotations.forms.Validation;
import main.origo.core.event.forms.ValidationHandlerEventGenerator;
import main.origo.core.interceptors.forms.DefaultSubmitHandler;
import models.origo.core.RootNode;
import play.mvc.Controller;
import play.mvc.Result;

import java.lang.reflect.InvocationTargetException;

@Interceptor
public class AdminSubmitHandler extends DefaultSubmitHandler {

    @SubmitHandler
    public static Result handleSubmit(SubmitHandler.Context context) {

        String withType = getWithType();

        try {
            Validation.Result validationResult = runValidation(context, withType);

            if (validationResult.hasErrors()) {
                return doValidationFailure(withType, validationResult);
            }
            return sendSubmitState(withType, validationResult);

        } catch (ModuleException | NodeLoadException | InvocationTargetException | IllegalAccessException | NodeNotFoundException e) {
            return CoreLoader.handleException(e);
        }
    }

    protected static Result doValidationFailure(String withType, Validation.Result validationResult) throws InvocationTargetException, IllegalAccessException, NodeNotFoundException, NodeLoadException, ModuleException {
        Node node = ValidationHandlerEventGenerator.triggerValidationFailedHandler(new RootNode(0), withType, validationResult);
        return Controller.badRequest(AdminLoader.decorateNode(node));
    }
}

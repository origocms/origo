package main.origo.admin.interceptors.forms;

import main.origo.admin.AdminLoader;
import main.origo.core.*;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnLoad;
import main.origo.core.annotations.forms.SubmitHandler;
import main.origo.core.annotations.forms.Validation;
import main.origo.core.event.forms.ValidationHandlerEventGenerator;
import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.core.helpers.forms.FormHelper;
import main.origo.core.interceptors.forms.DefaultSubmitHandler;
import main.origo.core.ui.Element;
import models.origo.core.RootNode;
import org.apache.commons.lang3.StringUtils;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@Interceptor
public class AdminSubmitHandler extends DefaultSubmitHandler {

    @SubmitHandler
    public static Result handleSubmit() {

        DynamicForm form = DynamicForm.form().bindFromRequest();
        String withType = FormHelper.getWithType(form.data());
        String identifier = FormHelper.getNodeId(form.data());

        try {
            Validation.Result validationResult = runValidation(withType);

            if (validationResult.hasErrors()) {
                return handleValidationFailure(identifier, withType, validationResult);
            }
            return sendSubmitState(withType, validationResult);

        } catch (ModuleException | NodeLoadException | InvocationTargetException | IllegalAccessException | NodeNotFoundException e) {
            return CoreLoader.handleException(e);
        }
    }

    protected static Result handleValidationFailure(String identifier, String withType, Validation.Result validationResult) throws InvocationTargetException, IllegalAccessException, NodeNotFoundException, NodeLoadException, ModuleException {
        Node node = ValidationHandlerEventGenerator.triggerValidationFailedHandler(new RootNode(identifier, 0, withType), withType, validationResult);
        return Controller.badRequest(AdminLoader.decorateNode(node));
    }

    @OnLoad(type = Core.Type.FORM)
    public static void addNodeFields(Node node, String withType, Element element, Map<String, Object> args) {
        final String postHandler = CoreSettingsHelper.getSubmitHandler();
        if (StringUtils.isBlank(postHandler)) {
            throw new RuntimeException("No SubmitHandler defined in settings: "+CoreSettingsHelper.Keys.SUBMIT_HANDLER);
        }
        if (AdminSubmitHandler.class.getName().equals(postHandler)) {
            FormHelper.addNodeId(element, node.nodeId());
            FormHelper.addNodeVersion(element, node.version());
            FormHelper.addWithType(element, node.nodeType());
        }
    }

}

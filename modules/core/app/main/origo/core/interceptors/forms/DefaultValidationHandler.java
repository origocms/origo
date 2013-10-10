package main.origo.core.interceptors.forms;

import main.origo.core.CoreLoader;
import main.origo.core.ModuleException;
import main.origo.core.NodeLoadException;
import main.origo.core.NodeNotFoundException;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.forms.Validation;
import main.origo.core.event.forms.OnSubmitEventGenerator;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;
import java.util.Map;

@Interceptor
public class DefaultValidationHandler {

    @Validation.Processing
    public static Validation.Result validate(String with) {

        List<Class> classes = OnSubmitEventGenerator.getValidatedClasses(with);

        Validation.Result result = new Validation.Result();

        for (Class c : classes) {
            if (c != Object.class) {
                Form form = Form.form(c).bindFromRequest();
                result.validatedClasses.put(c, form);
                result.errors.putAll(form.errors());
                result.globalErrors.addAll(form.globalErrors());
            }
        }

        return result;
    }

    @Validation.Failure(with = Core.With.VALIDATION_DEFAULT_FAILURE)
    public static Result validationFailure(String withType, String identifier, Map<String, Object> args) throws NodeLoadException, ModuleException, NodeNotFoundException {
        return Controller.badRequest(CoreLoader.loadAndDecorateNode(identifier));
    }

}

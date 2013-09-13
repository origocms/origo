package main.origo.core.interceptors.forms;

import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.forms.ValidationHandler;
import main.origo.core.event.forms.OnSubmitEventGenerator;
import play.data.Form;

import java.util.List;

@Interceptor
public class DefaultValidationHandler {

    @ValidationHandler
    public static ValidationHandler.Result validate(ValidationHandler.Context context) {

        List<Class> classes = OnSubmitEventGenerator.getValidatedClasses(context.with);

        ValidationHandler.Result result = new ValidationHandler.Result();

        for (Class c : classes) {

            Form form = Form.form(c).bindFromRequest();
            result.validatedClasses.put(c, form);
            result.errors.putAll(form.errors());
            result.globalErrors.addAll(form.globalErrors());

        }

        return result;
    }

}

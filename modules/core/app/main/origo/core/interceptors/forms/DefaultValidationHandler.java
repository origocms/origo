package main.origo.core.interceptors.forms;

import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.forms.Validation;
import main.origo.core.event.forms.OnSubmitEventGenerator;
import play.data.Form;

import java.util.List;

@Interceptor
public class DefaultValidationHandler {

    @Validation.Processing
    public static Validation.Result validate(Validation.Processing.Context context) {

        List<Class> classes = OnSubmitEventGenerator.getValidatedClasses(context.with);

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
}

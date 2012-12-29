package main.origo.core.interceptors.forms;

import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.forms.ProvidesForm;
import main.origo.core.helpers.forms.FormHelper;
import main.origo.core.ui.Element;

/**
 * Default implementation for a form provider, a different form provider can be used at any time by creating a
 * different form (see FormHelper). A default form provider is set up in the settings but can be changed to any
 * other provider.
 *
 * @see FormHelper
 */
@Interceptor
public class DefaultFormProvider {

    public static final String TYPE = "origo.core.basicform";

    @ProvidesForm(with = TYPE)
    public static Element createBasicForm(ProvidesForm.Context context) {
        return new Element.Form().
                addAttribute("action", FormHelper.getPostURL().url()).
                addAttribute("method", "POST");
    }

}

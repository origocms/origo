package main.origo.admin.interceptors.forms;

import main.origo.admin.helpers.forms.AdminFormHelper;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.ui.Element;

/**
 * Default implementation for a form provider, a different form provider can be used at any time by creating a
 * different form (see FormHelper). A default form provider is set up in the settings but can be changed to any
 * other provider.
 *
 * @see main.origo.core.helpers.forms.FormHelper
 */
@Interceptor
public class AdminFormProvider {

    public static final String TYPE = "origo.admin.adminform";

    @Provides(type = Core.Type.FORM, with = TYPE)
    public static Element createBasicForm(Provides.Context context) {
        return new Element.Form().
                addAttribute("action", AdminFormHelper.getPostURL().url()).
                addAttribute("method", "POST");
    }

}

package main.origo.core.interceptors.forms;

import main.origo.core.Node;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.helpers.forms.FormHelper;
import main.origo.core.ui.Element;

import java.util.Map;

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

    @Provides(type = Core.Type.FORM, with = TYPE)
    public static Element createBasicForm(Node node, String withType, Map<String, Object> args) {
        return new Element.Form().
                addAttribute("action", FormHelper.getPostURL().url()).
                addAttribute("method", "POST");
    }

}

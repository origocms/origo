package main.origo.admin.helpers.forms;

import controllers.origo.core.routes;
import main.origo.admin.helpers.AdminSettingsHelper;
import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.helpers.forms.FormHelper;
import main.origo.core.ui.Element;
import play.mvc.Call;

public class AdminFormHelper extends FormHelper {

    public static Element createFormElement(Node node, String withType) throws NodeLoadException, ModuleException {
        return FormHelper.createFormElement(node, withType, AdminSettingsHelper.getDefaultFormType());
    }

    public static Call getPostURL() {
        return routes.Submit.submit();
    }
}

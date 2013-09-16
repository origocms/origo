package main.origo.core.helpers.forms;

import controllers.origo.core.routes;
import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.forms.Validation;
import main.origo.core.event.NodeContext;
import main.origo.core.event.OnLoadEventGenerator;
import main.origo.core.event.ProvidesEventGenerator;
import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.core.ui.Element;
import play.data.Form;
import play.mvc.Call;

import java.util.Map;

public class FormHelper {

    private static final String NODE_ID = "_core_node_id";
    private static final String NODE_VERSION = "_core_node_version";

    public static Element createFormElement(Node node, String withType) throws NodeLoadException, ModuleException {
        return createFormElement(node, withType, CoreSettingsHelper.getDefaultFormType());
    }

    public static Element createFormElement(Node node, String nodeType, String formType) throws ModuleException, NodeLoadException {
        OnLoadEventGenerator.triggerBeforeInterceptor(node, Core.Type.FORM, nodeType);
        Element formElement = ProvidesEventGenerator.triggerInterceptor(node, Core.Type.FORM, formType);
        addNodeIdAndVersion(formElement, node);
        OnLoadEventGenerator.triggerAfterInterceptor(node, Core.Type.FORM, nodeType, formElement);
        return formElement;
    }

    public static Validation.Result getValidationResult() {
        return (Validation.Result) NodeContext.current().attributes.get(Validation.Result.class.getCanonicalName());
    }

    public static <T> Form<T> getValidationResult(Class<T> validatedClass) {
        return (Form<T>)getValidationResult().validatedClasses.get(validatedClass);
    }

    public static Call getPostURL() {
        return routes.Submit.submit();
    }

    public static String getNodeIdParamName() {
        return NODE_ID;
    }

    public static String getNodeVersionParamName() {
        return NODE_VERSION;
    }

    public static String getNodeId(Map<String,String> data) {
        return data.get(NODE_ID);
    }

    public static Integer getNodeVersion(Map<String,String> data) {
        try {
            return Integer.parseInt(data.get(NODE_VERSION));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Version is not a number: " + e.getLocalizedMessage(), e);
        }
    }

    public static void addNodeIdAndVersion(Element form, Node node) {
        form.
                addChild(new Element.InputHidden().addAttribute("name", FormHelper.getNodeIdParamName()).addAttribute("value", node.nodeId())).
                addChild(new Element.InputHidden().addAttribute("name", FormHelper.getNodeVersionParamName()).addAttribute("value", String.valueOf(node.version())));
    }
}

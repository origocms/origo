package main.origo.core.helpers.forms;

import com.google.common.collect.Lists;
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
import play.data.validation.ValidationError;
import play.i18n.Messages;
import play.mvc.Call;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FormHelper {

    private static final String NODE_ID = "_core_node_id";
    private static final String NODE_VERSION = "_core_node_version";

    public static Element createFormElement(Node node, String withType) throws NodeLoadException, ModuleException {
        return createFormElement(node, withType, CoreSettingsHelper.getDefaultFormType());
    }

    public static Element createFormElement(Node node, String withType, Form form) throws NodeLoadException, ModuleException {
        return createFormElement(node, withType, CoreSettingsHelper.getDefaultFormType(), form);
    }

    public static Element createFormElement(Node node, String withType, String formType) throws ModuleException, NodeLoadException {
        OnLoadEventGenerator.triggerBeforeInterceptor(node, Core.Type.FORM, withType, Collections.<String, Object>emptyMap());
        Element formElement = ProvidesEventGenerator.triggerInterceptor(node, Core.Type.FORM, formType, Collections.<String, Object>singletonMap("with", withType));
        addNodeIdAndVersion(formElement, node);
        OnLoadEventGenerator.triggerAfterInterceptor(node, Core.Type.FORM, withType, formElement, Collections.<String, Object>emptyMap());
        return formElement;
    }

    public static Element createFormElement(Node node, String withType, String formType, Form form) throws ModuleException, NodeLoadException {
        OnLoadEventGenerator.triggerBeforeInterceptor(node, Core.Type.FORM, withType, form, Collections.<String, Object>emptyMap());
        Element formElement = ProvidesEventGenerator.triggerInterceptor(node, Core.Type.FORM, formType, Collections.<String, Object>singletonMap("with", withType));
        addNodeIdAndVersion(formElement, node);
        OnLoadEventGenerator.triggerAfterInterceptor(node, Core.Type.FORM, withType, form, formElement, Collections.<String, Object>emptyMap());
        return formElement;
    }

    public static Validation.Result getValidationResult() {
        return (Validation.Result) NodeContext.current().attributes.get(Validation.Result.class.getCanonicalName());
    }

    public static <T> Form<T> getValidationResult(Class<T> validatedClass) {
        Validation.Result validationResult = getValidationResult();
        if (validationResult == null) {
            return Form.form(validatedClass);
        }
        Map<Class, Form> validatedClasses = validationResult.validatedClasses;
        if (!validatedClasses.containsKey(validatedClass)) {
            return Form.form(validatedClass);
        }
        return (Form<T>) validatedClasses.get(validatedClass);
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

    public static List<Element> getGlobalErrors() {
        List<Element> elements = Lists.newArrayList();
        Validation.Result validationResult = getValidationResult();
        if (validationResult != null && validationResult.hasErrors()) {
            for (ValidationError validationError : validationResult.globalErrors) {
                elements.add(new Element.Error().setBody(validationError.message()));
            }
        }
        return elements;
    }

    public static Element createGlobalErrorElement() {
        List<Element> globalErrors = FormHelper.getGlobalErrors();
        if (!globalErrors.isEmpty()) {
            return new Element.Well().addChildren(globalErrors);
        }
        return null;
    }

    public static Element getFieldError(Form form, String name) {
        ValidationError validationError = form.error(name);
        if (validationError != null) {
            return new Element.Help().setWeight(1000).setBody(getFieldError(validationError));
        }

        return null;
    }

    public static String getFieldError(ValidationError validationError) {
        return Messages.get(validationError.message(), validationError.arguments());
    }

    public static Element createField(Form form, Element.Label label, Element element) {
        Element.Field field = new Element.Field().
                addChild(label).
                addChild(element);

        String name = (String) element.getAttributes().get("name");
        Element validationError = getFieldError(form, name);

        if (validationError != null) {
            field.addAttribute("class", "error");
            field.addChild(validationError);
        }

        if (!element.getAttributes().containsKey("value")) {
            if (!CoreSettingsHelper.isSuppressPasswordValues() || !new Element.InputPassword().getType().equals(element.getType())) {
                Form.Field formField = form.field(name);
                element.addAttribute("value", formField.value());
            }
        }

        return field;
    }

    public static String getFieldValue(Form form, String name) {
        return form.field(name).value();
    }
}

package main.origo.authentication.interceptors;

import be.objectify.deadbolt.core.models.Subject;
import main.origo.authentication.form.LoginForm;
import main.origo.authentication.util.AuthenticationSessionUtils;
import main.origo.core.*;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnLoad;
import main.origo.core.annotations.Provides;
import main.origo.core.annotations.forms.OnSubmit;
import main.origo.core.annotations.forms.Validation;
import main.origo.core.event.NodeContext;
import main.origo.core.helpers.SessionHelper;
import main.origo.core.helpers.forms.FormHelper;
import main.origo.core.security.Security;
import main.origo.core.security.SecurityEventGenerator;
import main.origo.core.ui.Element;
import models.origo.authentication.BasicUser;
import models.origo.core.BasicPage;
import models.origo.core.RootNode;
import org.apache.commons.lang3.StringUtils;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Map;

@Interceptor
public class BasicUserProvider {

    private static final String CURRENT_USER = "user";

    private static final String USERNAME_PARAM = "username";
    private static final String PASSWORD_PARAM = "password";
    private static final String PATH_PARAM = "path";

    /**
     * Provides the actual node (page) with type Core.With.AUTHENTICATION_CHECK for the login and adds a
     * form with type Core.With.AUTHENTICATION_CHECK
     */
    @Provides(with = Core.With.AUTHENTICATION_CHECK)
    public static Node createLoginPage(Node node, String withType, Map<String, Object> args) throws ModuleException, NodeLoadException {

        Form<LoginForm> form = FormHelper.getValidationResult(LoginForm.class);

        BasicPage page = new BasicPage();
        page.rootNode = (RootNode)node;
        page.nodeId = page.rootNode.nodeId();
        page.title = "Login";

        page.addElement(FormHelper.createFormElement(node, Core.With.AUTHENTICATION_CHECK, form));
        return page;
    }

    /**
     * Adds login elements to the form
     */
    @OnLoad(type = Core.Type.FORM, with = Core.With.AUTHENTICATION_CHECK, after = true)
    public static void addLoginForm(Node node, String withType, Form form, Element element, Map<String, Object> args) {

        element.setId("loginform").addAttribute("class", "origo-loginform, form");

        Element globalErrors = FormHelper.createGlobalErrorElement();
        if (globalErrors != null) {
            element.addChild(globalErrors);
        }

        Element basicFieldSet = new Element.FieldSet().setId("login");
        element.addChild(basicFieldSet);

        String path = (String) NodeContext.current().attributes.get(Security.Params.AUTH_PATH);
        if (StringUtils.isNotBlank(path)) {
            basicFieldSet.addChild(new Element.InputHidden().addAttribute("name", PATH_PARAM).addAttribute("value", path));
        }

        basicFieldSet.addChild(new Element.Panel().
                addChild(FormHelper.createField(
                        form,
                        new Element.Label().setWeight(10).setBody("Username").addAttribute("for", USERNAME_PARAM),
                        new Element.InputText().setWeight(20).addAttribute("name", USERNAME_PARAM)).
                        setWeight(10)
                ).
                addChild(FormHelper.createField(
                        form,
                        new Element.Label().setWeight(10).setBody("Password").addAttribute("for", PASSWORD_PARAM),
                        new Element.InputPassword().setWeight(20).addAttribute("name", PASSWORD_PARAM)).
                        setWeight(20)
                )
        );

        element.addChild(new Element.Panel().setId("actions").setWeight(1000).
                addChild(new Element.Panel().
                        addChild(new Element.InputSubmit().setWeight(10).addAttribute("class", "btn btn-primary").addAttribute("value", "Login"))
                ));
    }

    /**
     * Handles the authentication of the supplied username/password.
     */
    @OnSubmit(with = Core.With.AUTHENTICATION_CHECK, validate = LoginForm.class)
    public static Boolean authenticateFormUser(Form<LoginForm> loginForm) throws NodeLoadException, ModuleException {

        String username = loginForm.get().getUsername();
        String path = loginForm.get().getPath();
        if (StringUtils.isNotBlank(path)) {
            NodeContext.current().attributes.put(Security.Params.AUTH_PATH, path);
        }

        AuthenticationSessionUtils.setSessionUserName(username);
        return true;
    }

    @Provides(type = Core.Type.SECURITY, with = Core.With.AUTHENTICATION_SIGNOUT)
    public static Result signout(Node node, String withType) {
        AuthenticationSessionUtils.removeSessionUserName();
        String originalPath = (String) NodeContext.current().attributes.get(Security.Params.AUTH_PATH);
        if (StringUtils.isBlank(originalPath)) {
            return CoreLoader.redirectToStartPage();
        }
        return Controller.redirect(originalPath);
    }


    @Validation.Failure(with = Core.With.AUTHENTICATION_CHECK)
    public static Node validationFailure(Node node, Map<String, Object> args) throws NodeLoadException, ModuleException {
        return createLoginPage(node, Core.With.AUTHENTICATION_CHECK, args);
    }

    @Provides(type = Core.Type.SECURITY, with = Core.With.AUTHENTICATION_CURRENT_USER)
    public static User getCurrent(Node node, String withType, Map<String, Object> args) {
        User user = (User) NodeContext.current().attributes.get(CURRENT_USER);
        if (user == null) {
            String username = AuthenticationSessionUtils.getSessionUserName();
            if (username != null) {
                user = BasicUser.findWithEmail(username);
                NodeContext.current().attributes.put(CURRENT_USER, user);
                return user;
            }
        }

        return user;
    }

    @Provides(type = Core.Type.SECURITY, with = Core.With.AUTHENTICATION_VALIDATE)
    public static Subject authenticate(Node node, String withType, Map<String, Object> args) throws ModuleException, NodeLoadException {
        String username = (String) args.get(Security.Params.AUTH_USERNAME);
        String password = (String) args.get(Security.Params.AUTH_PASSWORD);
        SecurityEventGenerator.triggerBeforeUserLoaded();
        BasicUser user = BasicUser.findWithEmail(username);
        if (user != null && password.equals(user.password)) {
            SecurityEventGenerator.triggerAfterUserLoaded(user);
            SessionHelper.setTimestamp();
            return user;
        }
        return null;
    }
}

package main.origo.authentication.interceptors;

import be.objectify.deadbolt.core.models.Subject;
import controllers.origo.authentication.routes;
import controllers.origo.core.CoreLoader;
import main.origo.authentication.form.LoginForm;
import main.origo.authentication.util.AuthenticationSessionUtils;
import main.origo.core.*;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnLoad;
import main.origo.core.annotations.Provides;
import main.origo.core.annotations.forms.OnSubmit;
import main.origo.core.annotations.forms.SubmitState;
import main.origo.core.event.NodeContext;
import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.core.helpers.forms.FormHelper;
import main.origo.core.security.Security;
import main.origo.core.security.SecurityEventGenerator;
import main.origo.core.ui.Element;
import main.origo.core.utils.ExceptionUtil;
import models.origo.core.BasicPage;
import models.origo.core.RootNode;
import models.origo.core.Settings;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.data.Form;
import play.mvc.Content;
import play.mvc.Controller;
import play.mvc.Result;

@Interceptor
public class AuthenticationProvider {

    private static final String USERNAME_PARAM = "origo-authentication-username";
    private static final String PASSWORD_PARAM = "origo-authentication-password";
    private static final String PATH_PARAM = "origo-authentication-path";

    /**
     * Checks if there is a authenticated user and if not triggers the display of a login page.
     * If there is a login page set in the content that will be used, if not it will fall back to
     * creating a "blank" node with type Core.With.AUTHENTICATION_CHECK
     */
    @Provides(type = Core.Type.SECURITY, with = Core.With.AUTHENTICATION_CHECK)
    public static Result authenticateUser(Provides.Context context) throws ModuleException, NodeLoadException {
        User user = SecurityEventGenerator.triggerCurrentUserInterceptor();
        if (user != null) {
            return null;
        }
        String path = (String) context.attributes.get(Security.Params.AUTH_PATH);
        String[] roles = SecurityEventGenerator.triggerProvidesAuthorizationRolesInterceptor(path);
        if (roles.length == 0) {
            return null;
        }
        return Controller.redirect(routes.Authentication.login(path));
    }

    /**
     * Provides the actual node (page) with type Core.With.AUTHENTICATION_CHECK for the login and adds a
     * form with type Core.With.AUTHENTICATION_CHECK
     */
    @Provides(with = Core.With.AUTHENTICATION_CHECK)
    public static Node createLoginPage(Provides.Context context) throws ModuleException, NodeLoadException {

        BasicPage page = new BasicPage();
        page.rootNode = (RootNode)context.node;
        page.nodeId = page.rootNode.nodeId();
        page.title = "Login";
        page.addElement(FormHelper.createFormElement(context.node, Core.With.AUTHENTICATION_CHECK));
        return page;
    }

    /**
     * Adds login elements to the form
     */
    @OnLoad(type = Core.Type.FORM, with = Core.With.AUTHENTICATION_CHECK, after = true)
    public static void addLoginForm(OnLoad.Context context) {

        Element element = (Element) context.args.get("element");
        element.setId("loginform").addAttribute("class", "origo-loginform, form");

        Element basicFieldSet = new Element.FieldSet().setId("login");
        element.addChild(basicFieldSet);

        String path = (String) context.attributes.get(Security.Params.AUTH_PATH);
        if (StringUtils.isNotBlank(path)) {
            basicFieldSet.addChild(new Element.InputHidden().addAttribute("name", PATH_PARAM).addAttribute("value", path));
        }

        basicFieldSet.addChild(new Element.Panel().
                addChild(new Element.Panel().setWeight(10).
                        addChild(new Element.Label().setWeight(10).setBody("Username").addAttribute("for", USERNAME_PARAM)).
                        addChild(new Element.InputText().setWeight(20).addAttribute("name", USERNAME_PARAM))
                ).
                addChild(new Element.Panel().setWeight(20).
                        addChild(new Element.Label().setWeight(10).setBody("Password").addAttribute("for", PASSWORD_PARAM)).
                        addChild(new Element.InputPassword().setWeight(20).addAttribute("name", PASSWORD_PARAM))
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
    public static Boolean authenticateFormUser(OnSubmit.Context<LoginForm> context) throws NodeLoadException, ModuleException {

        Form<LoginForm> loginForm = context.form;

        String username = loginForm.get().username;
        String password = loginForm.get().password;
        String path = loginForm.get().path;
        if (StringUtils.isNotBlank(path)) {
            context.attributes.put(Security.Params.AUTH_PATH, path);
        }

        Subject subject = SecurityEventGenerator.triggerValidateInterceptor(username, password);
        if (subject == null) {
            context.attributes.put("reason", "Unable to authenticated user, incorrect username or password");
            return false;
        }
        AuthenticationSessionUtils.setSessionUserName(username);
        return true;
    }

    /**
     * Handling the routing at the end of the submit process when the submit failed for some reason.
     */
    @SubmitState(state = SubmitState.FAILURE, with = Core.With.AUTHENTICATION_CHECK)
    public static Result handleFailure(SubmitState.Context context) {

        String unauthorizedPage = Settings.load().getValue(CoreSettingsHelper.Keys.UNAUTHORIZED_PAGE);
        try {
            if (StringUtils.isNotBlank(unauthorizedPage)) {
                Content content = CoreLoader.loadAndDecoratePage(unauthorizedPage, 0);
                return Controller.unauthorized(content);
            }
        } catch (NodeNotFoundException | NodeLoadException | ModuleException e) {
            ExceptionUtil.assertExceptionHandling(e);
            return CoreLoader.redirectToPageLoadErrorPage();
        }

        Logger.warn("Using fallback unauthorized handling, sending 401 with no content");
        return Controller.unauthorized();

    }

    /**
     * Handling the routing at the end of the submit process, it redirects to the original path the user tried to access
     * and if that is not set in the context it redirects to the start page from the settings.
     */
    @SubmitState(with = Core.With.AUTHENTICATION_CHECK)
    public static Result handleSuccess(SubmitState.Context context) {

        String originalPath = (String) NodeContext.current().attributes.get(Security.Params.AUTH_PATH);
        if (StringUtils.isBlank(originalPath)) {
            Logger.debug("No original path in context, defaulting to start page");
            return CoreLoader.redirectToStartPage();
        }
        return Controller.redirect(originalPath);
    }

}

package main.origo.authentication.interceptors;

import be.objectify.deadbolt.core.models.Subject;
import controllers.origo.core.CoreLoader;
import main.origo.authentication.helpers.SessionHelper;
import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.NodeNotFoundException;
import main.origo.core.annotations.Core;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnLoad;
import main.origo.core.annotations.Provides;
import main.origo.core.annotations.forms.OnSubmit;
import main.origo.core.annotations.forms.SubmitState;
import main.origo.core.event.ProvidesEventGenerator;
import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.core.helpers.NodeHelper;
import main.origo.core.helpers.ThemeHelper;
import main.origo.core.helpers.forms.FormHelper;
import main.origo.core.security.AuthorizationEventGenerator;
import main.origo.core.ui.Element;
import main.origo.core.ui.RenderedNode;
import main.origo.core.utils.ExceptionUtil;
import models.origo.authentication.BasicUser;
import models.origo.core.BasicPage;
import models.origo.core.RootNode;
import models.origo.core.Settings;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Content;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.util.Map;

@Interceptor
public class AuthenticationProvider {

    private static final String EMAIL_PARAM = "origo-authentication-email";
    private static final String PASSWORD_PARAM = "origo-authentication-password";

    @Provides(type = Core.Type.USER, with = Core.With.AUTHENTICATION_CHECK)
    public static Result authenticateUser(Provides.Context context) {
        try {
            // Check if there is a specific page set up for login
            String loginPage = CoreSettingsHelper.getLoginPage();
            if (StringUtils.isNotBlank(loginPage)) {
                return Controller.ok(CoreLoader.loadAndDecoratePage(loginPage, 0));
            }

            // Fall back to empty root node and trigger loading
            RootNode rootNode = new RootNode(0);
            rootNode.nodeType(Core.With.AUTHENTICATION_CHECK);
            Node node = NodeHelper.load(rootNode);

            // Render login page
            RenderedNode renderedNode = ThemeHelper.decorate(node, ThemeHelper.loadTheme(node, CoreSettingsHelper.getThemeVariant()));
            if (Logger.isDebugEnabled()) {
                Logger.debug("Decorated " + renderedNode);
            }
            return Controller.ok(ThemeHelper.render(renderedNode));

        } catch (NodeNotFoundException e) {
            return CoreLoader.loadPageNotFoundPage();
        } catch (Exception e) {
            ExceptionUtil.assertExceptionHandling(e);
            return CoreLoader.loadPageLoadErrorPage();
        }
    }

    @Provides(with = Core.With.AUTHENTICATION_CHECK)
    public static Node createLoginPage(Provides.Context context) throws ModuleException, NodeLoadException {

        BasicPage page = new BasicPage();
        page.rootNode = (RootNode)context.node;
        page.addElement(FormHelper.createFormElement(context.node, Core.With.AUTHENTICATION_CHECK));
        return page;
    }

    @OnLoad(type = Core.Type.FORM, with = Core.With.AUTHENTICATION_CHECK, after = true)
    public static void addLoginForm(OnLoad.Context context) {

        Element element = (Element) context.args.get("element");
        element.setId("loginform").addAttribute("class", "origo-loginform, form");

        Element basicFieldSet = new Element.FieldSet().setId("login");
        element.addChild(basicFieldSet);

        basicFieldSet.addChild(new Element.Panel().
                addChild(new Element.Panel().setWeight(10).
                        addChild(new Element.Label().setWeight(10).setBody("Email").addAttribute("for", EMAIL_PARAM)).
                        addChild(new Element.InputText().setWeight(20).addAttribute("name", EMAIL_PARAM))
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

    @OnSubmit(with = Core.With.AUTHENTICATION_CHECK)
    public static void authenticateFormUser(OnSubmit.Context context) {

        Form form = DynamicForm.form().bindFromRequest();
        Map<String, String> data = form.data();

        String email = data.get(EMAIL_PARAM);
        String password = data.get(PASSWORD_PARAM);

        Subject subject = authenticate(email, password);
        if (subject == null) {
            throw new RuntimeException("Unable to authenticated user, incorrect username or password");
        }

    }

    /**
     * Handling the routing at the end of the submit process, it redirects to START_PAGE from config.
     */
    @SubmitState(with = Core.With.AUTHENTICATION_CHECK)
    public static Result handleSuccess(SubmitState.Context context) {
        return Controller.redirect(CoreSettingsHelper.getStartPage());
    }

    @Provides(type = Core.Type.USER, with = Core.With.AUTHORIZATION_CHECK)
    public static Result checkAuthenticatedUser(Provides.Context context) throws ModuleException, NodeLoadException {
        if(getCurrent() != null) {
            return null;
        }
        return ProvidesEventGenerator.triggerInterceptor(context.node, Core.Type.USER, Core.With.AUTHENTICATION_CHECK, context.args);
    }

    @Provides(type = Core.Type.USER, with = Core.With.AUTHORIZATION_FAILURE)
    public static Result handleAuthFailure(Provides.Context context) {

        BasicUser user = AuthenticationProvider.getCurrent();
        AuthorizationEventGenerator.triggerBeforeAuthorizationFailure(user);

        try {
            String unauthorizedPage = Settings.load().getValue(CoreSettingsHelper.Keys.UNAUTHORIZED_PAGE);
            try {
                if (StringUtils.isNotBlank(unauthorizedPage)) {
                    Content content = CoreLoader.loadAndDecoratePage(unauthorizedPage, 0);
                    if (user != null) {
                        return Controller.forbidden(content);
                    } else {
                        return Controller.unauthorized(content);
                    }
                }
            } catch (NodeNotFoundException | NodeLoadException | ModuleException e) {
                ExceptionUtil.assertExceptionHandling(e);
                return CoreLoader.loadPageLoadErrorPage();
            }

            if (user != null) {
                Logger.warn("Using fallback forbidden handling, sending 403 with no content");
                return Controller.forbidden();
            } else {
                Logger.warn("Using fallback unauthorized handling, sending 401 with no content");
                return Controller.unauthorized();
            }
        } finally {
            AuthorizationEventGenerator.triggerAfterAuthorizationFailure(user);
        }
    }


    @Provides(type = Core.Type.USER, with = Core.With.AUTHORIZATION_SUBJECT)
    public static BasicUser getCurrentSubject(Provides.Context context) {
        return getCurrent();
    }

    private static BasicUser getCurrent() {
        String email = SessionHelper.getSessionUserName();
        if (email != null) {
            return BasicUser.findWithEmail(email);
        } else {
            return null;
        }
    }

    public static Subject authenticate(final String email, final String password){
        BasicUser user = BasicUser.findWithEmail(email);
        if (user != null && password.equals(user.password)) {
            SessionHelper.setTimestamp(Http.Context.current());
            return user;
        }
        return null;
    }

}

package main.origo.admin.interceptors.settings;

import com.google.common.collect.Lists;
import main.origo.admin.annotations.Admin;
import main.origo.admin.helpers.AdminHelper;
import main.origo.admin.helpers.DashboardHelper;
import main.origo.core.Node;
import main.origo.core.annotations.*;
import main.origo.core.annotations.forms.OnSubmit;
import main.origo.core.annotations.forms.SubmitState;
import main.origo.core.event.ProvidesEventGenerator;
import main.origo.core.helpers.forms.FormHelper;
import main.origo.core.ui.Element;
import models.origo.admin.AdminPage;
import models.origo.core.EventHandler;
import models.origo.core.RootNode;
import models.origo.core.Settings;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Interceptor
public class EventHandlerAdminProvider {

    private static final String BASE_TYPE = Admin.With.SETTINGS_PAGE + ".event";
    private static final String EDIT_TYPE = BASE_TYPE + ".edit";

    /**
     * Dashboard element for the settings dashboard page.
     *
     * @return a Element that contains a dashboard element.
     */
    @Provides(type = Admin.Type.DASHBOARD_ITEM, with = BASE_TYPE)
    @Relationship(parent = Admin.With.SETTINGS_PAGE)
    public static Element createDashboardItem(Provides.Context context) {

        String url = AdminHelper.getURLForAdminAction(Admin.With.CONTENT_PAGE, EDIT_TYPE);

        return new Admin.DashboardItem().addAttribute("class", "item").
                addChild(new Element.Panel().setWeight(10).
                        addChild(new Element.Heading4().setWeight(10).setBody("Event Handlers").addAttribute("class", "title")).
                        addChild(new Element.Paragraph().setWeight(20).setBody("Select which event handler should be used for ").addAttribute("class", "description")).
                        addChild(new Element.Anchor().setWeight(30).setBody("List All").addAttribute("href", url).addAttribute("class", "link"))
                );
    }

    /**
     * Provides a type with the static name 'origo.admin.settings.event.edit'.
     *
     * @param context containing a root node with an node id
     * @return a node to be presented as part of the admin UI
     */
    @Provides(type = Core.Type.NODE, with = EDIT_TYPE)
    public static Node createEditPage(Provides.Context context) {
        AdminPage page = new AdminPage(context.node.getNodeId());
        page.setTitle("Event Handlers");
        page.rootNode = (RootNode) context.node;
        return page;
    }

    @OnLoad(type = Core.Type.NODE, with = EDIT_TYPE)
    public static void loadEditPage(OnLoad.Context context) {
        Element formElement = FormHelper.createFormElement(context.node, BASE_TYPE).addAttribute("class", "form-horizontal");
        context.node.addElement(formElement);

        Map<String, EventHandler> eventHandlers = EventHandler.findAll();
        List<String> keys = Lists.newArrayList(eventHandlers.keySet());
        Collections.sort(keys);

        List<String> providerTypes = Lists.newArrayList(ProvidesEventGenerator.getAllProviderTypes());
        Collections.sort(providerTypes);
        for (String providerType : providerTypes) {
            formElement.addChild(new Element.Legend().setBody(providerType));

            for (String key : keys) {

                Set<String> providers = ProvidesEventGenerator.getAllProviders(providerType, key);

                if (!providers.isEmpty()) {
                    Element inputSelect = new Element.InputSelect().setId("event-" + key).addAttribute("name", key);
                    for (String provider : providers) {
                        Element element = new Element.InputSelectOption().setBody(provider);
                        if (eventHandlers.get(key).handlerClass.equals(provider)) {
                            element.addAttribute("selected", "selected");
                        }
                        inputSelect.addChild(element);
                    }

                    formElement.
                            addChild(new Element.Panel().addAttribute("class", "field control-group").
                                    addChild(new Element.Label().addAttribute("class", "control-label").addAttribute("for", "event-" + key).setBody(key)).
                                    addChild(new Element.Panel().addAttribute("class", "controls").
                                            addChild(inputSelect))
                            );
                }
            }
        }
        formElement.addChild(
                new Element.Panel().setWeight(40).addAttribute("class", "field").
                        addChild(new Element.InputSubmit().setWeight(10).addAttribute("value", "Save")).
                        addChild(new Element.InputReset().setWeight(15).addAttribute("value", "Reset"))
        );

    }

    /**
     * Hooks in to the submit process and stores the event handlers when it is submitted.
     */
    @OnSubmit(with = BASE_TYPE)
    public static void storeEvents(OnSubmit.Context context) {

        Form form = DynamicForm.form().bindFromRequest();
        Map<String, String> data = form.data();

        Settings settings = Settings.load();
        for (String key : data.keySet()) {
            if (key.startsWith("event.")) {
                settings.setValue(key, data.get(key));
            }
        }
        settings.save();
    }

    /**
     * Handling the routing at the end of the submit process, it redirects to listing the handlers.
     */
    @SubmitState(with = BASE_TYPE)
    public static Result handleSuccess(SubmitState.Context context) {
        String endpointURL = DashboardHelper.getDashBoardURL(Admin.With.SETTINGS_PAGE);
        return Controller.redirect(endpointURL);
    }

}
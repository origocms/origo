package main.origo.admin.interceptors.settings;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import controllers.origo.admin.routes;
import main.origo.admin.annotations.Admin;
import main.origo.admin.helpers.DashboardHelper;
import main.origo.admin.themes.AdminTheme;
import main.origo.core.InterceptorRepository;
import main.origo.core.Node;
import main.origo.core.annotations.*;
import main.origo.core.annotations.forms.OnSubmit;
import main.origo.core.annotations.forms.SubmitState;
import main.origo.core.event.ProvidesEventGenerator;
import main.origo.core.helpers.forms.FormHelper;
import main.origo.core.internal.CachedAnnotation;
import main.origo.core.ui.Element;
import models.origo.admin.AdminPage;
import models.origo.core.EventHandler;
import models.origo.core.RootNode;
import org.apache.commons.lang3.StringUtils;
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

        String url = routes.Dashboard.pageWithType(Admin.With.CONTENT_PAGE, EDIT_TYPE).url();

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
        AdminPage page = new AdminPage((RootNode) context.node);
        page.setTitle("Event Handlers");
        page.setThemeVariant(AdminTheme.LEFT_AND_MAIN_COLUMNS_VARIANT_NAME);
        page.addElement(DashboardHelper.createBreadcrumb(BASE_TYPE), AdminTheme.topMeta());
        return page;
    }

    @OnLoad(type = Core.Type.NODE, with = EDIT_TYPE)
    public static void loadEditPage(OnLoad.Context context) {

        String selectedEventType = getSelectedEventType(context);

        addEventTypeElements(context.node, selectedEventType);

        addEventHandlerElements(context.node, selectedEventType);
    }

    private static String getSelectedEventType(OnLoad.Context context) {
        String selectedEventType = (String) context.args.get("type");
        if (StringUtils.isBlank(selectedEventType)) {
            selectedEventType = Provides.class.getName();
        }
        return selectedEventType;
    }

    private static void addEventTypeElements(Node node, String selectedEventType) {
        List<String> allEventTypes = EventHandler.findEventTypes();
        Element eventTypeListElement = new Element.ListBulleted().
                addAttribute("class", "nav nav-tabs nav-stacked");
        for(String eventType : allEventTypes) {
            String trimmedEventTypeName = eventType.substring(eventType.lastIndexOf(".")+1);
            Element eventTypeListItem = new Element.ListItem().
                    addChild(new Element.Anchor().
                            addAttribute("href", routes.Dashboard.pageWithType(Admin.With.CONTENT_PAGE, EDIT_TYPE).url()+"?type="+eventType).
                            setBody(trimmedEventTypeName));
            if (eventType.equals(selectedEventType)) {
                eventTypeListItem.addAttribute("class", "active");
            }
            eventTypeListElement.addChild(eventTypeListItem);
        }
        node.addElement(eventTypeListElement, AdminTheme.leftColumnMeta(), false);
    }

    private static void addEventHandlerElements(Node node, String selectedEventType) {

        Element formElement = null;

        Map<String, EventHandler> eventHandlers = getEventHandlers(selectedEventType);
        List<String> keys = getEventHandlerKeys(eventHandlers);

        List<String> providerTypes = Lists.newArrayList(getAllProviderTypes());
        Collections.sort(providerTypes);

        List<Element> fieldElements = Lists.newArrayList();
        for (String providerType : providerTypes) {

            fieldElements.add(new Element.Legend().setBody(providerType));
            for (String key : keys) {

                Set<String> providers = ProvidesEventGenerator.getAllProviders(providerType, key);

                if (!providers.isEmpty()) {
                    Element inputSelect = new Element.InputSelect().setId("event-" + key).
                            addAttribute("class", "span5").
                            addAttribute("name", "event."+key);
                    for (String provider : providers) {
                        Element element = new Element.InputSelectOption().setBody(provider);
                        if (eventHandlers.get(key).handlerClass.equals(provider)) {
                            element.addAttribute("selected", "selected");
                        }
                        inputSelect.addChild(element);
                    }

                    fieldElements.
                            add(new Element.Panel().addAttribute("class", "field control-group row").
                                    addChild(new Element.Label().addAttribute("class", "control-label span4").addAttribute("for", "event-" + key).setBody(key)).
                                    addChild(new Element.Panel().addAttribute("class", "controls span5").
                                            addChild(inputSelect))
                            );
                }
            }
        }
        if (!fieldElements.isEmpty()) {
            formElement = node.
                    addElement(FormHelper.createFormElement(node, BASE_TYPE).addAttribute("class", "form-horizontal")).
                    addChildren(fieldElements);
        }

        if (formElement != null) {
            Element actionPanel = new Element.Panel().setWeight(40).addAttribute("class", "well well-large").
                    addChild(new Element.Panel().
                            addAttribute("class", "pull-left").
                            addChild(new Element.Anchor().setWeight(20).
                                    addAttribute("class", "btn").
                                    addAttribute("href", routes.Dashboard.dashboard(Admin.With.SETTINGS_PAGE).url()).
                                    setBody("Cancel")
                            )
                    ).
                    addChild(new Element.Panel().
                            addAttribute("class", "pull-right").
                            addChild(new Element.InputSubmit().setWeight(10).addAttribute("class", "btn btn-primary").addAttribute("value", "Save")).
                            addChild(new Element.InputReset().setWeight(15).addAttribute("class", "btn").addAttribute("value", "Reset"))
                    );
            formElement.addChild(actionPanel);
        } else {
            node.addElement(new Element.Panel().
                    addAttribute("class", "well well-big").
                    setBody("No providers matching this type")
            );
        }

    }

    private static List<String> getEventHandlerKeys(Map<String, EventHandler> eventHandlers) {
        List<String> keys = Lists.newArrayList(eventHandlers.keySet());
        Collections.sort(keys);
        return keys;
    }

    private static Map<String, EventHandler> getEventHandlers(String selectedEventType) {
        Map<String, EventHandler> eventHandlers = Maps.newHashMap();
        for (EventHandler eventHandler : EventHandler.findAllWithEventType(selectedEventType)) {
            eventHandlers.put(eventHandler.withType, eventHandler);
        }
        return eventHandlers;
    }

    /**
     * Hooks in to the submit process and stores the event handlers when it is submitted.
     */
    @OnSubmit(with = BASE_TYPE)
    public static void storeEvents(OnSubmit.Context context) {

        for (String key : context.args.keySet()) {
            if (key.startsWith("event.")) {
                EventHandler handler = EventHandler.findWithWithType(key.substring("event.".length()));
                handler.handlerClass = (String) context.args.get(key);
            }
        }
    }

    /**
     * Handling the routing at the end of the submit process, it redirects to listing the handlers.
     */
    @SubmitState(with = BASE_TYPE)
    public static Result handleSuccess(SubmitState.Context context) {
        return Controller.redirect(routes.Dashboard.dashboard(Admin.With.SETTINGS_PAGE).url());
    }


    /**
     * Filters out the cached providers types.
     * @return a list of types that have a provider (NODE, NAVIGATION, NAVIGATION_ITEM, DASHBOARD_ITEM, etc)
     * @see main.origo.core.annotations.Core
     */
    private static Set<String> getAllProviderTypes() {
        Set<String> providedTypes = Sets.newHashSet();
        providedTypes.addAll(getAllProvides());
        //providedTypes.addAll(getAllFormProvides());
        return providedTypes;
    }

    private static Set<String> getAllProvides() {
        List<CachedAnnotation> interceptors = InterceptorRepository.getInterceptors(Provides.class);
        Set<String> providedTypes = Sets.newHashSet();
        for (CachedAnnotation cachedAnnotation : interceptors) {
            providedTypes.add(((Provides)cachedAnnotation.annotation).type());
        }
        return providedTypes;
    }


}
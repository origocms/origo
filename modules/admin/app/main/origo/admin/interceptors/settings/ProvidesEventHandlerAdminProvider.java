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
import views.html.origo.admin.dashboard_item;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Interceptor
public class ProvidesEventHandlerAdminProvider {

    private static final String BASE_TYPE = Admin.With.SETTINGS_PAGE + ".event.provides";
    private static final String EDIT_TYPE = BASE_TYPE + ".Admin";

    /**
     * Dashboard element for the settings dashboard page.
     *
     * @return a Element that contains a dashboard element.
     */
    @Provides(type = Admin.Type.DASHBOARD_ITEM, with = BASE_TYPE)
    @Relationship(parent = Admin.With.SETTINGS_PAGE)
    public static Element createDashboardItem(Provides.Context context) {

        return new Admin.DashboardItem().
                addChild(new Element.Raw().setBody(dashboard_item.render("Event Handlers", "Select which event handler should be used for each type", getProviderUrl(), "List All")));
    }

    @Admin.Navigation(alias = "/settings/provides", key = "breadcrumb.origo.admin.dashboard.settings.event.provides")
    public static String getProviderUrl() {
        return routes.Dashboard.pageWithType(Core.With.CONTENT_PAGE, EDIT_TYPE).url();
    }

    /**
     * Provides a type with the static name 'origo.admin.settings.event.edit'.
     *
     * @param context containing a root node with an node id
     * @return a node to be presented as part of the admin UI
     */
    @Provides(type = Core.Type.NODE, with = EDIT_TYPE)
    public static Node createEditPage(Provides.Context context) {
        AdminPage page = new AdminPage(EDIT_TYPE, (RootNode) context.node);
        page.setTitle("Event Handlers");
        page.setThemeVariant(AdminTheme.LEFT_AND_MAIN_COLUMNS_VARIANT_NAME);
        page.addElement(DashboardHelper.createBreadcrumb(BASE_TYPE), AdminTheme.topMeta());
        return page;
    }

    @OnLoad(type = Core.Type.NODE, with = EDIT_TYPE)
    public static void loadEditPage(OnLoad.Context context) {

        List<String> providerTypes = getAllProvides();

        if (!providerTypes.isEmpty()) {

            String selectedEventType = getSelectedEventType(context.args, providerTypes);
            addProviderTypeElements(context.node, providerTypes, selectedEventType);
            addProviderElements(context.node, selectedEventType);

        } else {
            context.node.addElement(new Element.Panel().
                    addAttribute("class", "well well-big").
                    setBody("No handlers found."));
        }

    }

    private static String getSelectedEventType(Map<String, Object> args, List<String> providerTypes) {
        String selectedEventType = (String) args.get("type");
        if (StringUtils.isBlank(selectedEventType)) {
            selectedEventType = providerTypes.iterator().next();
        }
        return selectedEventType;
    }

    private static void addProviderElements(Node node, String selectedEventType) {

        // Make sure EventHandlers for each type is stored in the DB
        Set<String> withTypes = getWithTypes(ProvidesEventGenerator.getAllProviders(selectedEventType));
        for (String withType : withTypes) {
            ProvidesEventGenerator.findInterceptor(selectedEventType, withType);
        }

        // Find all stored EventHandlers for the selected type
        Map<String, EventHandler> eventHandlers = getEventHandlers(selectedEventType);
        List<String> keys = getEventHandlerKeys(eventHandlers);

        List<Element> fieldElements = Lists.newArrayList();

        List<Element> providerElements = Lists.newArrayList();

        // Create a row with an input selector for each type
        for (String key : keys) {

            // Get all the classes annotated with the Provides for this type
            Set<String> providers = getClassnames(ProvidesEventGenerator.getAllProviders(selectedEventType, key));

            if (!providers.isEmpty()) {
                Element inputSelect = createInputSelect("event", key);
                for (String provider : providers) {
                    Element element = new Element.InputSelectOption().setBody(provider);
                    if (eventHandlers.get(key).handlerClass.equals(provider)) {
                        element.addAttribute("selected", "selected");
                    }
                    inputSelect.addChild(element);
                }

                providerElements.add(createRowElement("event", key, inputSelect));
            }
        }

        if (!providerElements.isEmpty()) {
            fieldElements.add(new Element.Legend().setBody(selectedEventType));
            fieldElements.addAll(providerElements);
        }

        createFormElement(node, selectedEventType, fieldElements);
    }

    private static Set<String> getClassnames(List<CachedAnnotation> interceptors) {
        Set<String> providedTypes = Sets.newHashSet();
        for (CachedAnnotation cachedAnnotation : interceptors) {
            providedTypes.add(cachedAnnotation.method.getDeclaringClass().getName());
        }
        return providedTypes;
    }

    private static Set<String> getWithTypes(List<CachedAnnotation> interceptors) {
        Set<String> providedTypes = Sets.newHashSet();
        for (CachedAnnotation cachedAnnotation : interceptors) {
            providedTypes.add(((Provides) cachedAnnotation.annotation).with());
        }
        return providedTypes;
    }

    private static void createFormElement(Node node, String selectedEventType, List<Element> fieldElements) {
        if (!fieldElements.isEmpty()) {
            node.addElement(FormHelper.createFormElement(node, BASE_TYPE).
                    addAttribute("class", "form-horizontal").
                    addChild(new Element.InputHidden().addAttribute("name", "type").addAttribute("value", selectedEventType)).
                    addChild(new Element.FieldSet().setId("handlers").addChildren(fieldElements)).
                    addChild(createButtonPanel()));
        } else {
            node.addElement(new Element.Panel().
                    addAttribute("class", "well well-big").
                    addChild(new Element.Text().setBody("No handlers matching type ")).
                    addChild(new Element.Emphasize().setBody(selectedEventType))).
                    addChild(new Element.Text().setBody("."));
        }
    }

    private static Element createButtonPanel() {
        return new Element.Panel().setWeight(40).addAttribute("class", "well well-large").
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
    }

    private static Element createRowElement(String prefix, String key, Element inputSelect) {
        return new Element.Panel().addAttribute("class", "field control-group row").
                addChild(new Element.Label().addAttribute("class", "control-label span4").addAttribute("for", prefix + "." + key).setBody(key)).
                addChild(new Element.Panel().addAttribute("class", "controls span5").
                        addChild(inputSelect));
    }

    private static Element createInputSelect(String prefix, String key) {
        return new Element.InputSelect().setId(prefix + "." + key).
                addAttribute("class", "span5").
                addAttribute("name", prefix + "." + key);
    }

    private static List<String> getEventHandlerKeys(Map<String, EventHandler> eventHandlers) {
        List<String> keys = Lists.newArrayList(eventHandlers.keySet());
        Collections.sort(keys);
        return keys;
    }

    private static boolean hasEventHandler(String selectedEventType) {
        return EventHandler.existsWithAnnotationAndNodeType(Provides.class.getName(), selectedEventType);
    }

    private static Map<String, EventHandler> getEventHandlers(String selectedEventType) {
        Map<String, EventHandler> eventHandlers = Maps.newHashMap();
        for (EventHandler eventHandler : EventHandler.findAllWithAnnotationAndNodeType(Provides.class.getName(), selectedEventType)) {
            eventHandlers.put(eventHandler.withType, eventHandler);
        }
        return eventHandlers;
    }

    /**
     * Hooks in to the submit process and stores the event handlers when it is submitted.
     */
    @OnSubmit(with = BASE_TYPE)
    public static void storeEvents(OnSubmit.Context context) {

        String selectedEventType = getSelectedEventType(context.args, getAllProvides());

        for (String key : context.args.keySet()) {
            if (key.startsWith("event.")) {
                EventHandler handler = EventHandler.findWithNodeTypeAndWithType(selectedEventType, key.substring("event.".length()));
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

    private static List<String> getAllProvides() {

        List<CachedAnnotation> interceptors = InterceptorRepository.getInterceptors(Provides.class);
        Set<String> providedTypes = Sets.newHashSet();
        for (CachedAnnotation cachedAnnotation : interceptors) {
            Provides annotation = (Provides) cachedAnnotation.annotation;
            providedTypes.add(annotation.type());
        }

        List<String> sortedProvidedTypes = Lists.newArrayList(providedTypes);
        Collections.sort(sortedProvidedTypes);
        return sortedProvidedTypes;
    }


    private static void addProviderTypeElements(Node node, List<String> providerTypes, String selectedEventType) {

        Element providerTypeElement = new Element.ListBulleted().
                addAttribute("class", "nav nav-tabs nav-stacked");
        for (String providerName : providerTypes) {
            Element eventTypeListItem = new Element.ListItem().
                    addChild(new Element.Anchor().
                            addAttribute("href", getProviderUrl() + "?type=" + providerName).
                            setBody(providerName));
            if (providerName.equals(selectedEventType)) {
                eventTypeListItem.addAttribute("class", "active");
            }
            providerTypeElement.addChild(eventTypeListItem);
        }
        node.addElement(providerTypeElement, AdminTheme.leftColumnMeta(), false);
    }

}
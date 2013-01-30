package main.origo.admin.interceptors.content;

import com.google.common.collect.Sets;
import main.origo.admin.interceptors.content.basicpage.BasicPageAdminProvider;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnInsertElement;
import main.origo.core.annotations.forms.OnCreate;
import main.origo.core.annotations.forms.OnDelete;
import main.origo.core.annotations.forms.OnSubmit;
import main.origo.core.event.forms.OnCreateEventGenerator;
import main.origo.core.event.forms.OnDeleteEventGenerator;
import main.origo.core.event.forms.OnUpdateEventGenerator;
import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.core.helpers.NavigationHelper;
import main.origo.core.helpers.forms.FormHelper;
import main.origo.core.interceptors.BasicPageProvider;
import main.origo.core.ui.Element;
import main.origo.core.ui.NavigationElement;
import models.origo.admin.AdminPage;
import models.origo.core.Alias;
import models.origo.core.navigation.AliasNavigation;
import models.origo.core.navigation.BasicNavigation;
import models.origo.core.navigation.PageIdNavigation;
import org.apache.commons.lang3.StringUtils;
import play.data.DynamicForm;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Interceptor
public class BasicNavigationAdminProvider {

    private static final String PARENT_PARAM = "nav_parent";
    private static final String TYPE_PARAM = "nav_type";
    private static final String NAVIGATION_ID_PARAM = "nav_id";
    private static final String USE_NAVIGATION_PARAM = "nav";

    // TODO: Hard coded for now, should be moved to configuration
    private static Set<String> types = Sets.newHashSet(
            BasicPageAdminProvider.EDIT_TYPE,
            BasicPageAdminProvider.NEW_TYPE
    );

    @OnInsertElement(with = Element.FieldSet.class, after = true)
    public static void addNavigationFieldSet(OnInsertElement.Context context) {
        if (!BasicNavigation.class.getName().equals(CoreSettingsHelper.getNavigationType())) {
            return;
        }

        AdminPage adminPage = (AdminPage) context.node();
        if (types.contains(adminPage.type) && context.element.getId().equals("content")) {

            List<NavigationElement> navigationElements = NavigationHelper.getNavigation(adminPage.rootNode, NavigationElement.FRONT);

            NavigationElement selectedNavigationElement = getSelectedNavigationId(navigationElements);
            BasicNavigation currentNavigation = null;
            if (selectedNavigationElement != null) {
                currentNavigation = BasicNavigation.findWithReferenceIdentifier(selectedNavigationElement.id);
            }

            // Setup parent drop down element
            Element.InputSelectOption noParentOption = new Element.InputSelectOption();
            Element parentInputSelect = new Element.InputSelect().
                    setId("text-" + PARENT_PARAM).
                    addAttribute("name", PARENT_PARAM).
                    addChild(noParentOption);
            addNavigationElement(parentInputSelect, navigationElements, noParentOption, "");


            // Setup navigation type elements
            Element.InputRadioButton aliasInputRadioButton = new Element.InputRadioButton().addAttribute("name", TYPE_PARAM).addAttribute("value", AliasNavigation.TYPE);
            if (currentNavigation != null && currentNavigation.type.equals(AliasNavigation.TYPE)) {
                aliasInputRadioButton.addAttribute("checked", "checked");
            }
            Element.InputRadioButton pageIdInputRadioButton = new Element.InputRadioButton().addAttribute("name", TYPE_PARAM).addAttribute("value", PageIdNavigation.TYPE);
            if (currentNavigation != null && currentNavigation.type.equals(PageIdNavigation.TYPE)) {
                aliasInputRadioButton.addAttribute("checked", "checked");
            }
            Element navigationTypeElement = new Element.Panel().
                    addChild(new Element.Label().addAttribute("class", "radio").
                            addChild(aliasInputRadioButton).
                            setBody("Alias")).
                    addChild(new Element.Label().addAttribute("class", "radio").
                            addChild(pageIdInputRadioButton).
                            setBody("Page Id"));

            // Setup the wrapper for the fieldset element
            context.parent.addChild(new Element.FieldSet().
                    addChild(new Element.Legend().setBody("Navigation")).
                    addChild(new Element.Panel().
                            addChild(new Element.Label().addAttribute("class", "checkbox").
                                    addChild(new Element.InputCheckbox(Boolean.class).
                                            addAttribute("name", USE_NAVIGATION_PARAM).
                                            addAttribute("value", "true").
                                            addAttribute("checked", currentNavigation != null ? "checked" : "")
                                    ).
                                    setBody("Add Navigation"))).
                    addChild(new Element.Panel().addAttribute("class", "row-fluid").
                            addChild(new Element.Panel().addAttribute("class", "field span6").
                                    addChild(new Element.Label().setBody("Parent").addAttribute("for", "text-" + PARENT_PARAM)).
                                    addChild(parentInputSelect)).
                            addChild(new Element.Panel().addAttribute("class", "field span6").
                                    addChild(new Element.Label().setBody("Type").addAttribute("for", "text-" + TYPE_PARAM)).
                                    addChild(navigationTypeElement))
                    )
            );
            context.parent.addChild(new Element.InputHidden().addAttribute("name", NAVIGATION_ID_PARAM).addAttribute("value", selectedNavigationElement != null ? selectedNavigationElement.id : ""));

        }
    }

    private static NavigationElement getSelectedNavigationId(List<NavigationElement> navigationElements) {
        for (NavigationElement navigationElement : navigationElements) {
            if (navigationElement.selected) {
                return navigationElement;
            }
            NavigationElement element = getSelectedNavigationId(navigationElement.children());
            if (element != null) {
                return element;
            }
        }
        return null;
    }

    private static void addNavigationElement(Element element, List<NavigationElement> navigationElements, Element.InputSelectOption parent, String prepend) {
        for (NavigationElement navigationElement : navigationElements) {
            Element.InputSelectOption option = new Element.InputSelectOption().
                    addAttribute("value", String.valueOf(navigationElement.id)).
                    setBody(prepend + " " + navigationElement.title);
            if (navigationElement.selected) {
                parent.addAttribute("selected", "selected");
            }
            element.addChild(option);
            addNavigationElement(element, navigationElement.children, option, prepend + "-");
        }
    }

    /**
     * Hooks in to the submit process and stores a an alias for a page when the page is submitted.
     */
    @OnSubmit(weight = 1100)
    public static void storeNavigation(OnSubmit.Context context) {

        if (!BasicNavigation.class.getName().equals(CoreSettingsHelper.getNavigationType())) {
            return;
        }

        DynamicForm form = DynamicForm.form().bindFromRequest();
        Map<String, String> data = form.data();

        if (!StringUtils.isEmpty(data.get(USE_NAVIGATION_PARAM))) {

            String parentReferenceId = data.get(PARENT_PARAM);
            BasicNavigation newParent = null;
            if (!StringUtils.isEmpty(parentReferenceId)) {
                newParent = BasicNavigation.findWithReferenceIdentifier(parentReferenceId);
                if (newParent == null) {
                    throw new RuntimeException("Unable to find selected navigation from id [" + parentReferenceId + "]");
                }
            }

            String referenceId = data.get(NAVIGATION_ID_PARAM);
            BasicNavigation currentNavigation = BasicNavigation.findWithReferenceIdentifier(referenceId);
            if (currentNavigation != null) {
                updateNavigation(currentNavigation, newParent, data);
            } else {
                createNewNavigation(newParent, data);
            }
        } else {
            String referenceId = data.get(NAVIGATION_ID_PARAM);
            BasicNavigation currentNavigation = BasicNavigation.findWithReferenceIdentifier(referenceId);
            if (currentNavigation != null) {
                OnDeleteEventGenerator.triggerBeforeInterceptors(BasicPageProvider.TYPE, currentNavigation);
                deleteType(currentNavigation);
                currentNavigation.delete();
                OnDeleteEventGenerator.triggerAfterInterceptors(BasicPageProvider.TYPE, currentNavigation);
            }
        }

    }

    private static void updateNavigation(BasicNavigation navigation, BasicNavigation newParent, Map<String, String> data) {
        OnUpdateEventGenerator.triggerBeforeInterceptors(BasicPageProvider.TYPE, navigation);

        String type = data.get(TYPE_PARAM);
        if (!navigation.type.equals(type) || (newParent == null || !newParent.equals(navigation.parent))) {
            deleteType(navigation);
            OnUpdateEventGenerator.triggerBeforeInterceptors(type, navigation);
            navigation.type = type;
            navigation.parent = newParent;
            OnUpdateEventGenerator.triggerAfterInterceptors(type, navigation);
        }

        OnUpdateEventGenerator.triggerAfterInterceptors(BasicPageProvider.TYPE, navigation);
    }

    private static void createNewNavigation(BasicNavigation newParent, Map<String, String> data) {

        OnCreateEventGenerator.triggerBeforeInterceptors(BasicPageProvider.TYPE);

        String type = data.get(TYPE_PARAM);
        String referenceId = data.get(NAVIGATION_ID_PARAM);

        OnCreateEventGenerator.triggerBeforeInterceptors(type);
        BasicNavigation navigation = new BasicNavigation();
        navigation.type = type;
        navigation.parent = newParent;
        navigation.referenceId = referenceId;
        OnCreateEventGenerator.triggerAfterInterceptors(type, navigation);

        OnCreateEventGenerator.triggerAfterInterceptors(BasicPageProvider.TYPE, navigation);

    }

    public static void deleteType(BasicNavigation navigation) {
        OnDeleteEventGenerator.triggerBeforeInterceptors(navigation.type, navigation);
        // Nuthin to do here?
        OnDeleteEventGenerator.triggerAfterInterceptors(navigation.type, navigation);
    }

    @OnCreate(with = AliasNavigation.TYPE)
    public void storeAliasNavigation(OnCreate.NavigationContext context) {

        DynamicForm form = DynamicForm.form().bindFromRequest();
        Map<String, String> data = form.data();

        String nodeId = FormHelper.getNodeId(data);
        String referenceId = data.get(NAVIGATION_ID_PARAM);

        Alias alias = Alias.findFirstAliasForPageId(nodeId);
        if (alias != null) {
            AliasNavigation aliasNavigation = new AliasNavigation();
            aliasNavigation.identifier = referenceId;
            aliasNavigation.aliasId = alias.id;
        } else {
            throw new RuntimeException("Unable to find the alias for node [" + nodeId + "]");
        }

    }

    @OnCreate(with = PageIdNavigation.TYPE)
    public void storePageIdNavigation(OnCreate.NavigationContext context) {

        DynamicForm form = DynamicForm.form().bindFromRequest();
        Map<String, String> data = form.data();

        String nodeId = FormHelper.getNodeId(data);
        String referenceId = data.get(NAVIGATION_ID_PARAM);

        PageIdNavigation navigation = new PageIdNavigation();
        navigation.identifier = referenceId;
        navigation.pageId = nodeId;
    }

    @OnDelete(with = PageIdNavigation.TYPE)
    public void removeOldPageIdNavigation(OnDelete.NavigationContext context) {
        PageIdNavigation navigation = PageIdNavigation.findWithIdentifier(context.navigation.getReferenceId());
        navigation.delete();
    }

    @OnDelete(with = AliasNavigation.TYPE)
    public void removeOldAliasNavigation(OnDelete.NavigationContext context) {
        AliasNavigation navigation = AliasNavigation.findWithIdentifier(context.navigation.getReferenceId());
        navigation.delete();
    }

}

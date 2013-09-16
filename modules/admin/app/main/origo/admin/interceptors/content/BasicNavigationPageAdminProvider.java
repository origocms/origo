package main.origo.admin.interceptors.content;

import main.origo.core.ModuleException;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnInsertElement;
import main.origo.core.annotations.forms.OnSubmit;
import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.core.helpers.NavigationHelper;
import main.origo.core.helpers.forms.FormHelper;
import main.origo.core.ui.Element;
import main.origo.core.ui.NavigationElement;
import models.origo.admin.AdminPage;
import models.origo.core.navigation.BasicNavigation;
import models.origo.core.navigation.InternalPageIdNavigation;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.data.DynamicForm;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Interceptor
public class BasicNavigationPageAdminProvider {

    private static final String PARENT_PARAM = "nav_parent";
    private static final String NAVIGATION_ID_PARAM = "nav_id";
    private static final String USE_NAVIGATION_PARAM = "nav";

    @OnInsertElement(with = Element.FieldSet.class, after = true)
    public static void addNavigationFieldSet(OnInsertElement.Context context) {
        if (!BasicNavigation.TYPE.equals(CoreSettingsHelper.getNavigationType())) {
            // Not the active navigation type
            return;
        }

        // TODO: Hard coded for now, should be moved to configuration
        if (BasicPageAdminProvider.TYPE.equals(context.node.nodeType()) && context.element.getId().equals("content")) {

            AdminPage adminPage = (AdminPage) context.node;
            try {
                List<NavigationElement> navigationElements = NavigationHelper.getNavigation(adminPage.rootNode, NavigationElement.FRONT);

                NavigationElement selectedNavigationElement = NavigationHelper.getSelectedNavigation(navigationElements);
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

                // Setup the wrapper for the fieldset element
                Element.InputCheckbox useNavigationCheckBox = new Element.InputCheckbox(Boolean.class).
                        addAttribute("name", USE_NAVIGATION_PARAM).
                        addAttribute("value", "true");
                if (currentNavigation != null) {
                    useNavigationCheckBox.addAttribute("checked", "checked");
                }
                context.parent.addChild(new Element.FieldSet().setWeight(200).
                        addChild(new Element.Legend().setBody("Navigation")).
                        addChild(new Element.Panel().
                                addChild(new Element.Label().addAttribute("class", "checkbox").
                                        addChild(useNavigationCheckBox).
                                        setBody("Add Navigation"))).
                        addChild(new Element.Panel().addAttribute("class", "row-fluid").
                                addChild(new Element.Panel().addAttribute("class", "field span6").
                                        addChild(new Element.Label().setBody("Parent").addAttribute("for", "text-" + PARENT_PARAM)).
                                        addChild(parentInputSelect))
                        )
                );
                context.parent.addChild(new Element.InputHidden().addAttribute("name", NAVIGATION_ID_PARAM).addAttribute("value", selectedNavigationElement != null ? selectedNavigationElement.id : ""));
            } catch (NodeLoadException e) {
                // TODO: recover somehow?
                Logger.error("Unable to load node", e);
            } catch (ModuleException e) {
                // TODO: recover somehow?
                Logger.error("Unable to load node", e);
            }
        }
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
     * Hooks in to the submit process and stores an alias for a page when the page is submitted.
     */
    @OnSubmit(weight = 1100)
    public static Boolean storeNavigation(OnSubmit.Context context) {

        if (!BasicNavigation.TYPE.equals(CoreSettingsHelper.getNavigationType())) {
            // Not the active navigation type
            return true;
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

                // There is a navigation already, update the parent if needed
                if (currentNavigation.parent != null && newParent == null || newParent != null && !newParent.equals(currentNavigation.parent)) {
                    currentNavigation.parent = newParent;
                    currentNavigation.update();

                    InternalPageIdNavigation internalPageIdNavigation = InternalPageIdNavigation.findWithIdentifier(referenceId);
                    internalPageIdNavigation.pageId = FormHelper.getNodeId(data);
                    internalPageIdNavigation.update();
                }
            } else {

                // If there is no old navigation we create a new one
                BasicNavigation basicNavigation = new BasicNavigation();
                basicNavigation.type = InternalPageIdNavigation.TYPE;
                basicNavigation.parent = newParent;
                basicNavigation.referenceId = UUID.randomUUID().toString();
                // TODO: Should not be hardcoded, fix this
                basicNavigation.section = "front";
                basicNavigation.create();

                InternalPageIdNavigation internalPageIdNavigation = new InternalPageIdNavigation();
                internalPageIdNavigation.identifier = basicNavigation.referenceId;
                internalPageIdNavigation.pageId = FormHelper.getNodeId(data);
                internalPageIdNavigation.create();

            }
        } else {
            // If this page shouldn't be part of the navigation we delete any navigation objects that might exist
            String referenceId = data.get(NAVIGATION_ID_PARAM);
            BasicNavigation currentNavigation = BasicNavigation.findWithReferenceIdentifier(referenceId);
            if (currentNavigation != null) {
                currentNavigation.delete();
            }
        }

        return true;
    }

}

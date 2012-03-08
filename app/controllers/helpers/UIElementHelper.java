package controllers.helpers;

import controllers.core.Node;
import controllers.ui.UIElement;
import models.Meta;

import java.util.*;

/**
 * Helper methods to organize child UIElements in each UIElement.
 */
public class UIElementHelper {

    public static UIElement addHeadUIElement(Map<String, UIElement> headElements, Map<String, List<UIElement>> uiElements, UIElement uiElement, boolean reorderElementsBelow) {
        String elementKey = String.valueOf(uiElement.hashCode());
        if (headElements.containsKey(elementKey)) {
            return headElements.get(elementKey);
        } else {
            headElements.put(elementKey, uiElement);
            return addUIElementToRegion(uiElements, uiElement, reorderElementsBelow, Node.HEAD, uiElement.getWeight());
        }
    }

    public static UIElement addUIElementUsingMeta(Map<String, List<UIElement>> uiElements, UIElement uiElement, boolean reorderElementsBelow, String nodeId, Integer version) {
        Meta meta = Meta.findWithNodeIdAndSpecificVersion(nodeId, version, uiElement.id);
        if (meta == null) {
            meta = Meta.defaultMeta();
        }

        return addUIElementToRegion(uiElements, uiElement, reorderElementsBelow, meta.region.toLowerCase(), meta.weight);
    }

    
    public static UIElement addUIElementToRegion(Map<String, List<UIElement>> uiElements, UIElement uiElement, boolean reorderElementsBelow, String regionKey, int weight) {
        if (!uiElements.containsKey(regionKey)) {
            uiElements.put(regionKey, new ArrayList<UIElement>());
        }
        uiElement.setWeight(weight);
        uiElements.get(regionKey).add(uiElement);
        if (reorderElementsBelow) {
            repositionUIElements(uiElements.get(regionKey), uiElement);
        }
        reorderUIElements(uiElements.get(regionKey));
        return uiElement;
    }

    public static boolean removeHeadUIElement(Map<String, List<UIElement>> uiElements, UIElement uiElement) {
        return removeUIElement(uiElements, uiElement, Node.HEAD);
    }

    public static boolean removeUIElement(Map<String, List<UIElement>> uiElements, UIElement uiElement, String nodeId, Integer version) {
        Meta meta = Meta.findWithNodeIdAndSpecificVersion(nodeId, version, uiElement.id);
        if (meta == null) {
            meta = Meta.defaultMeta();
        }
        String regionKey = meta.region.toLowerCase();
        return removeUIElement(uiElements, uiElement, regionKey);
    }

    private static boolean removeUIElement(Map<String, List<UIElement>> uiElements, UIElement uiElement, String regionKey) {
        if (uiElements.get(regionKey).remove(uiElement)) {
            UIElementHelper.reorderUIElements(uiElements.get(regionKey));
            return true;
        }
        return false;
    }

    public static void reorderUIElements(List<UIElement> elements) {
        Collections.sort(elements, new Comparator<UIElement>() {
            @Override
            public int compare(UIElement uiElement, UIElement uiElement1) {
                return (uiElement.getWeight() >= uiElement1.getWeight()) ? 1 : 0;
            }
        });
    }

    public static void repositionUIElements(List<UIElement> elements, UIElement element) {
        for (UIElement elem : elements) {
            if (elem.getWeight() >= element.getWeight()) {
                elem.setWeight(elem.getWeight() + 1);
            }
        }
    }

}

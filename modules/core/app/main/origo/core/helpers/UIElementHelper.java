package main.origo.core.helpers;

import main.origo.core.ui.Element;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Helper methods to organize child UIElements in each Element.
 */
public class UIElementHelper {

    public static void reorderUIElements(List<Element> elements) {
        Collections.sort(elements, new Comparator<Element>() {
            @Override
            public int compare(Element element, Element element1) {
                return (element.getWeight() >= element1.getWeight()) ? 1 : 0;
            }
        });
    }

    public static void repositionUIElements(List<Element> elements, Element element) {
        for (Element elem : elements) {
            if (elem.getWeight() >= element.getWeight()) {
                elem.setWeight(elem.getWeight() + 1);
            }
        }
    }

}

package main.origo.core.helpers;

import com.google.common.collect.Maps;
import main.origo.core.ui.Element;
import play.api.templates.Html;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ElementHelper {

    public static Html getHtmlFromBody(Element element) {
        if (element.hasBody()) {
            return element.getBody();
        } else {
            return Html.empty();
        }
    }

    public static Map<String, String> combineAttributes(Map<String, String> map1, Map<String, String> map2) {
        Map<String, String> result = Maps.newHashMap(map1);
        for (String name : map2.keySet()) {
            if (result.containsKey(name)) {
                result.put(name, result.get(name).concat(" ").concat(map2.get(name)));
            } else {
                result.put(name, map2.get(name));
            }
        }
        return result;
    }

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

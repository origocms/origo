package main.origo.core.ui;

import com.google.common.collect.Lists;

import java.util.List;

public class NavigationElement implements Comparable<NavigationElement> {

    // Plugins can define their own sections, for example an Intranet module could define a private navigation section
    public final static String FRONT = "front";
    public final static String INTRANET = "intranet";
    public final static String EXTRANET = "extranet";

    public String section;
    public String title;
    public String link;
    public int weight;
    public boolean selected;

    public List<NavigationElement> children = Lists.newArrayList();

    public NavigationElement() {
    }

    public NavigationElement setSection(String section) {
        this.section = section;
        return this;
    }

    public NavigationElement setTitle(String title) {
        this.title = title;
        return this;
    }

    public NavigationElement setLink(String link) {
        this.link = link;
        return this;
    }

    public NavigationElement setWeight(int weight) {
        this.weight = weight;
        return this;
    }

    public NavigationElement setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }

    public NavigationElement addChild(NavigationElement navigationElement) {
        this.children.add(navigationElement);
        return this;
    }

    @Override
    public int compareTo(NavigationElement that) {
        return new Integer(this.weight).compareTo(that.weight);
    }
}

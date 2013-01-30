package main.origo.core.ui;

import com.google.common.collect.Lists;

import java.util.List;

public class NavigationElement implements Comparable<NavigationElement> {

    // Plugins can define their own sections, for example an Intranet module could define a private navigation section
    public final static String FRONT = "front";
    public final static String INTRANET = "intranet";
    public final static String EXTRANET = "extranet";

    public String id;
    public String section;
    public String title;
    public String link;
    public int weight;
    public boolean selected;

    public List<NavigationElement> children = Lists.newArrayList();

    public NavigationElement() {
    }

    public List<NavigationElement> children() {
        return children;
    }

    @Override
    public int compareTo(NavigationElement that) {
        return new Integer(this.weight).compareTo(that.weight);
    }
}

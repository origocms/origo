package main.origo.core.ui;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

// TODO refactor to builder pattern
public class NavigationElement implements Comparable<NavigationElement> {

    // Plugins can define their own sections, for example an Intranet module could define a private navigation section
    public final static String FRONT = "front";
    public final static String INTRANET = "intranet";
    public final static String EXTRANET = "extranet";

    public String section;
    public String title;
    public String link;
    public int weight;

    public List<NavigationElement> children = Lists.newArrayList();

    public boolean selected;
    public Set<String> styleClasses = Sets.newHashSet();

    public NavigationElement(String section, String title, String link, int weight) {
        this.section = section;
        this.title = title;
        this.link = link;
        this.weight = weight;
    }

    public NavigationElement(String section, String title, String link, int weight, boolean selected) {
        this(section, title, link, weight);
        this.selected = selected;
    }

    @Override
    public int compareTo(NavigationElement that) {
        return new Integer(this.weight).compareTo(that.weight);
    }
}

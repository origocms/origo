package main.origo.core.ui;

import main.origo.core.CachedThemeVariant;
import play.api.templates.Html;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderedNode {

    private String id;

    private String title;

    private List<Html> meta;
    private List<Html> link;
    private List<Html> script;
    private List<Html> style;

    private CachedThemeVariant template;
    private Map<String, Html> regions;

    private Collection<NavigationElement> navigation;

    public RenderedNode(String id) {
        this.id = id;
        regions = new HashMap<String, Html>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Html> getMeta() {
        return meta;
    }

    public void setMeta(List<Html> meta) {
        this.meta = meta;
    }

    public List<Html> getLink() {
        return link;
    }

    public void setLink(List<Html> link) {
        this.link = link;
    }

    public List<Html> getStyle() {
        return style;
    }

    public void setStyle(List<Html> style) {
        this.style = style;
    }

    public List<Html> getScript() {
        return script;
    }

    public void setScript(List<Html> script) {
        this.script = script;
    }

    public CachedThemeVariant getTemplate() {
        return template;
    }

    public void setTemplate(CachedThemeVariant template) {
        this.template = template;
    }

    public Map<String, Html> getRegions() {
        return regions;
    }

    public void setRegions(Map<String, Html> regions) {
        this.regions = regions;
    }

    public Html get(String region) {
        return regions.get(region);
    }

    public Collection<NavigationElement> getNavigation() {
        return navigation;
    }

    public void setNavigation(Collection<NavigationElement> navigation) {
        this.navigation = navigation;
    }

    public void addMeta(Html additionalContent) {
        meta.add(additionalContent);
    }

    public void addLink(Html additionalContent) {
        link.add(additionalContent);
    }

    public void addScript(Html additionalContent) {
        script.add(additionalContent);
    }

    public void addStyle(Html additionalContent) {
        style.add(additionalContent);
    }

    public void add(String region, Html additionalContent) {
        regions.put(region, additionalContent);
    }

    @Override
    public String toString() {
        return "RenderedNode (" + id + ") - " + title;
    }
}

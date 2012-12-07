package com.origocms.core.ui;

import com.origocms.core.CachedThemeVariant;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RenderedNode {

    private String id;

    private String title;

    private String meta;
    private String link;
    private String script;
    private String style;

    private CachedThemeVariant template;
    private Map<String, String> regions;

    private Collection<NavigationElement> navigation;

    public RenderedNode(String id) {
        this.id = id;
        regions = new HashMap<String, String>();
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

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public CachedThemeVariant getTemplate() {
        return template;
    }

    public void setTemplate(CachedThemeVariant template) {
        this.template = template;
    }

    public Map<String, String> getRegions() {
        return regions;
    }

    public void setRegions(Map<String, String> regions) {
        this.regions = regions;
    }

    public String get(String region) {
        return regions.get(region);
    }

    public Collection<NavigationElement> getNavigation() {
        return navigation;
    }

    public void setNavigation(Collection<NavigationElement> navigation) {
        this.navigation = navigation;
    }

    public void addMeta(String additionalContent) {
        StringBuilder sb = new StringBuilder(meta != null ? meta : "");
        sb.append(additionalContent);
        meta = sb.toString();
    }

    public void addLink(String additionalContent) {
        StringBuilder sb = new StringBuilder(link != null ? link : "");
        sb.append(additionalContent);
        link = sb.toString();
    }

    public void addScript(String additionalContent) {
        StringBuilder sb = new StringBuilder(script != null ? script : "");
        sb.append(additionalContent);
        script = sb.toString();
    }

    public void addStyle(String additionalContent) {
        StringBuilder sb = new StringBuilder(style != null ? style : "");
        sb.append(additionalContent);
        style = sb.toString();
    }

    public void add(String region, String additionalContent) {
        StringBuilder sb = new StringBuilder(regions.get(region));
        sb.append(additionalContent);
        regions.put(region, sb.toString());
    }

    @Override
    public String toString() {
        return "RenderedNode (" + id + ") - " + title;
    }
}

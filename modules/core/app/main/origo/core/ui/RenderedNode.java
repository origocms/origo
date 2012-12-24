package main.origo.core.ui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import main.origo.core.CachedThemeVariant;
import play.api.templates.Html;

import java.util.List;
import java.util.Map;

public class RenderedNode {

    private String _id;

    private String _title;

    private List<Html> _head = Lists.newArrayList();
    private List<Html> _tail = Lists.newArrayList();

    private CachedThemeVariant _template;
    private Map<String, List<Html>> regions = Maps.newHashMap();

    private List<NavigationElement> _navigation = Lists.newArrayList();

    public RenderedNode(String id) {
        this._id = id;
    }

    public String id() {
        return _id;
    }

    public void id(String id) {
        this._id = id;
    }

    public String title() {
        return _title;
    }

    public void title(String title) {
        this._title = title;
    }

    public List<Html> head() {
        return _head;
    }

    public void head(List<Html> head) {
        this._head = head;
    }

    public List<Html> tail() {
        return _tail;
    }

    public void tail(List<Html> tail) {
        this._tail = tail;
    }

    public CachedThemeVariant template() {
        return _template;
    }

    public void template(CachedThemeVariant template) {
        this._template = template;
    }

    public Map<String, List<Html>> regions() {
        return regions;
    }

    public void regions(Map<String, List<Html>> regions) {
        this.regions = regions;
    }

    public List<Html> get(String region) {
        return regions.get(region);
    }

    public List<NavigationElement> navigation() {
        return _navigation;
    }

    public void navigation(List<NavigationElement> navigation) {
        this._navigation = navigation;
    }

    public void addHead(Html additionalContent) {
        _head.add(additionalContent);
    }

    public void addTail(Html additionalContent) {
        _tail.add(additionalContent);
    }

    public void add(String region, Html additionalContent) {
        if (!regions.containsKey(region)) {
            regions.put(region, Lists.<Html>newArrayList());
        }
        regions.get(region).add(additionalContent);
    }

    @Override
    public String toString() {
        return "RenderedNode (" + _id + ") - " + _title;
    }
}

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

    private List<Html> _meta = Lists.newArrayList();
    private List<Html> _link = Lists.newArrayList();
    private List<Html> _script = Lists.newArrayList();
    private List<Html> _style = Lists.newArrayList();

    private CachedThemeVariant _template;
    private Map<String, List<Html>> regions = Maps.newHashMap();

    private List<NavigationElement> _navigation = Lists.newArrayList();

    public RenderedNode(String id) {
        this._id = id;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String title() {
        return _title;
    }

    public void title(String title) {
        this._title = title;
    }

    public List<Html> meta() {
        return _meta;
    }

    public void meta(List<Html> meta) {
        this._meta = meta;
    }

    public List<Html> link() {
        return _link;
    }

    public void link(List<Html> link) {
        this._link = link;
    }

    public List<Html> style() {
        return _style;
    }

    public void style(List<Html> style) {
        this._style = style;
    }

    public List<Html> script() {
        return _script;
    }

    public void script(List<Html> script) {
        this._script = script;
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

    public void addMeta(Html additionalContent) {
        _meta.add(additionalContent);
    }

    public void addLink(Html additionalContent) {
        _link.add(additionalContent);
    }

    public void addScript(Html additionalContent) {
        _script.add(additionalContent);
    }

    public void addStyle(Html additionalContent) {
        _style.add(additionalContent);
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

package models.origo.admin;

import main.origo.core.Node;
import main.origo.core.ui.Element;
import models.origo.core.Meta;
import models.origo.core.RootNode;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class AdminPage implements Node {

    public String type;

    public RootNode rootNode;

    public String title;

    public String themeVariant;

    public AdminPage(String type, RootNode node) {
        this.type = type;
        this.rootNode = node;
    }

    @Override
    public String nodeId() {
        return this.rootNode.nodeId();
    }

    @Override
    public String nodeType() {
        return this.rootNode.nodeType();
    }

    @Override
    public Integer version() {
        return rootNode.version();
    }

    @Override
    public Date published() {
        return rootNode.published();
    }

    @Override
    public Date unpublished() {
        return rootNode.unpublished();
    }

    @Override
    public String title() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String themeVariant() {
        return rootNode.themeVariant();
    }

    public void setThemeVariant(String themeVariant) {
        this.themeVariant = themeVariant;
    }

    @Override
    public Set<String> regions() {
        return rootNode.regions();
    }

    /* Interface methods */
    @Override
    public List<Element> elements(String region) {
        return rootNode.elements(region);
    }

    @Override
    public Element addHeadElement(Element element) {
        return rootNode.addHeadElement(element);
    }

    @Override
    public Element addTailElement(Element element) {
        return rootNode.addTailElement(element);
    }

    @Override
    public Element addElement(Element element) {
        return rootNode.addElement(element);
    }

    @Override
    public Element addHeadElement(Element element, boolean reorderElementsBelow) {
        return rootNode.addElement(element, reorderElementsBelow);
    }

    @Override
    public Element addTailElement(Element element, boolean reorderElementsBelow) {
        return rootNode.addTailElement(element, reorderElementsBelow);
    }

    @Override
    public Element addElement(Element element, boolean reorderElementsBelow) {
        return rootNode.addElement(element, reorderElementsBelow);
    }

    @Override
    public Element addElement(Element element, Meta meta) {
        return rootNode.addElement(element, meta);
    }

    @Override
    public Element addElement(Element element, Meta meta, boolean reorderElementsBelow) {
        return rootNode.addElement(element, meta, reorderElementsBelow);
    }

    @Override
    public boolean removeHeadElement(Element element) {
        return rootNode.removeHeadElement(element);
    }

    @Override
    public boolean removeTailElement(Element element) {
        return rootNode.removeTailElement(element);
    }

    @Override
    public boolean removeElement(Element element) {
        return rootNode.removeElement(element);
    }

    @Override
    public boolean hasElements() {
        return rootNode.hasElements();
    }

    @Override
    public String toString() {
        return new StringBuilder().append("AdminPage {").
                append("type='").append(type).append("\', ").
                append("title='").append(title).append("\', ").
                append("themeVariant='").append(themeVariant()).append("\', ").
                toString();
    }
}

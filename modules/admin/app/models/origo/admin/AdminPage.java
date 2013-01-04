package models.origo.admin;

import main.origo.core.Node;
import main.origo.core.ui.Element;
import models.origo.core.Meta;
import models.origo.core.RootNode;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class AdminPage implements Node {

    public String nodeId;

    public String title;

    public RootNode rootNode;

    public AdminPage(RootNode node) {
        this.rootNode = node;
        this.nodeId = node.getNodeId();
    }

    @Override
    public String getNodeId() {
        return this.nodeId;
    }

    @Override
    public Integer getVersion() {
        return rootNode.getVersion();
    }

    @Override
    public Date getDatePublished() {
        return rootNode.getDatePublished();
    }

    @Override
    public Date getDateUnpublished() {
        return rootNode.getDateUnpublished();
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getThemeVariant() {
        return rootNode.getThemeVariant();
    }

    public void setThemeVariant(String themeVariant) {
        rootNode.themeVariant = themeVariant;
    }

    @Override
    public Set<String> getRegions() {
        return rootNode.getRegions();
    }

    /* Interface methods */
    @Override
    public List<Element> getElements(String region) {
        return rootNode.getElements(region);
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
                append("nodeId='").append(nodeId).append("\', ").
                append("title='").append(title).append("\', ").
                append("themeVariant='").append(getThemeVariant()).append("\', ").
                toString();
    }
}

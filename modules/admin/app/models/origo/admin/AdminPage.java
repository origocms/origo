package models.origo.admin;

import main.origo.core.Node;
import main.origo.core.ui.Element;
import models.origo.core.RootNode;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class AdminPage implements Node {

    public String nodeId;

    public String title;

    public RootNode rootNode;

    public AdminPage(String nodeId) {
        this.nodeId = nodeId;
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

    @Override
    public Set<String> getRegions() {
        return rootNode.getRegions();
    }

    /* Interface methods */
    @Override
    public List<Element> getUIElements(String region) {
        return rootNode.getUIElements(region);
    }

    @Override
    public Element addHeadUIElement(Element element) {
        return rootNode.addHeadUIElement(element);
    }

    @Override
    public Element addTailUIElement(Element element) {
        return rootNode.addTailUIElement(element);
    }

    @Override
    public Element addUIElement(Element element) {
        return rootNode.addUIElement(element);
    }

    @Override
    public Element addHeadUIElement(Element element, boolean reorderElementsBelow) {
        return rootNode.addUIElement(element, reorderElementsBelow);
    }

    @Override
    public Element addTailUIElement(Element element, boolean reorderElementsBelow) {
        return rootNode.addTailUIElement(element, reorderElementsBelow);
    }

    @Override
    public Element addUIElement(Element element, boolean reorderElementsBelow) {
        return rootNode.addUIElement(element, reorderElementsBelow);
    }

    @Override
    public boolean removeHeadUIElement(Element element) {
        return rootNode.removeHeadUIElement(element);
    }

    @Override
    public boolean removeTailUIElement(Element element) {
        return rootNode.removeTailUIElement(element);
    }

    @Override
    public boolean removeUIElement(Element element) {
        return rootNode.removeUIElement(element);
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

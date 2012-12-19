package models.origo.admin;

import main.origo.core.Node;
import main.origo.core.ui.UIElement;
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
    public List<UIElement> getUIElements(String region) {
        return rootNode.getUIElements(region);
    }

    @Override
    public UIElement addHeadUIElement(UIElement uiElement) {
        return rootNode.addHeadUIElement(uiElement);
    }

    @Override
    public UIElement addTailUIElement(UIElement uiElement) {
        return rootNode.addTailUIElement(uiElement);
    }

    @Override
    public UIElement addUIElement(UIElement uiElement) {
        return rootNode.addUIElement(uiElement);
    }

    @Override
    public UIElement addHeadUIElement(UIElement uiElement, boolean reorderElementsBelow) {
        return rootNode.addUIElement(uiElement, reorderElementsBelow);
    }

    @Override
    public UIElement addTailUIElement(UIElement uiElement, boolean reorderElementsBelow) {
        return rootNode.addTailUIElement(uiElement, reorderElementsBelow);
    }

    @Override
    public UIElement addUIElement(UIElement uiElement, boolean reorderElementsBelow) {
        return rootNode.addUIElement(uiElement, reorderElementsBelow);
    }

    @Override
    public boolean removeHeadUIElement(UIElement uiElement) {
        return rootNode.removeHeadUIElement(uiElement);
    }

    @Override
    public boolean removeTailUIElement(UIElement uiElement) {
        return rootNode.removeTailUIElement(uiElement);
    }

    @Override
    public boolean removeUIElement(UIElement uiElement) {
        return rootNode.removeUIElement(uiElement);
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

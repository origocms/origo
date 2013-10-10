package models.origo.admin;

import main.origo.core.Node;
import main.origo.core.ui.Element;
import models.origo.core.Meta;
import models.origo.core.Release;
import models.origo.core.RootNode;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;

public class AdminPage implements Node {

    public RootNode rootNode;

    public String title;

    public String themeVariant;

    private AdminPage(RootNode node) {
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
    public Release release() {
        return rootNode.release();
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
        return themeVariant;
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
                append("type='").append(rootNode.nodeType()).append("\', ").
                append("title='").append(title).append("\', ").
                append("themeVariant='").append(themeVariant()).append("\', ").
                toString();
    }

    public static AdminPage create(RootNode node, String type) {
        if (node == null) {
            return new AdminPage(new RootNode(0, type));
        } else {
            if (StringUtils.isNotBlank(node.nodeId())) {
                if (node.version() == null || node.version() == 0) {
                    return new AdminPage(RootNode.findLatestVersionWithNodeId(node.nodeId()).copy());
                } else {
                    return new AdminPage(RootNode.findLatestPublishedVersionWithNodeId(node.nodeId()));
                }
            } else {
                return new AdminPage(node);
            }
        }
    }
}

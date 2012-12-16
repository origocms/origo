package models.origo.structuredcontent;

import main.origo.core.Node;
import main.origo.core.ui.UIElement;
import models.origo.core.RootNode;
import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "pageVersion", columnNames = {"parentNodeId", "parentVersion"}))
public class StructuredPage implements Node {

    @Constraints.Required
    @Column(name = "parentNodeId")
    public String nodeId;

    @Constraints.Required
    @Column(name = "parentVersion")
    public Integer version;

    @Transient
    public RootNode rootNode;

    @Constraints.Required
    public String title;

    @Override
    public String getNodeId() {
        return this.nodeId;
    }

    @Override
    public Integer getVersion() {
        return this.version;
    }

    @Override
    public Date getDatePublished() {
        return this.rootNode.publish;
    }

    @Override
    public Date getDateUnpublished() {
        return this.rootNode.unPublish;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getThemeVariant() {
        return rootNode.themeVariant;
    }

    @Override
    public Set<String> getRegions() {
        return rootNode.getRegions();
    }

    @Override
    public List<UIElement> getUIElements(String region) {
        return rootNode.getUIElements(region);
    }

    @Override
    public UIElement addHeadUIElement(UIElement uiElement) {
        return rootNode.addHeadUIElement(uiElement);
    }

    @Override
    public UIElement addUIElement(UIElement uiElement) {
        return rootNode.addUIElement(uiElement, false);
    }

    @Override
    public UIElement addHeadUIElement(UIElement uiElement, boolean reorderElementsBelow) {
        return rootNode.addHeadUIElement(uiElement, reorderElementsBelow);
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
    public boolean removeUIElement(UIElement uiElement) {
        return rootNode.removeUIElement(uiElement);
    }

    @Override
    public String toString() {
        return new StringBuilder().
                append("StructuredPage {").
                append("nodeId='").append(nodeId).append("\', ").
                append("version=").append(version).append(", ").
                append("rootNode=").append(rootNode).append(", ").
                append("title='").append(title).append("\'").
                append("}").toString();
    }

    public static List<StructuredPage> findAllCurrentVersions(Date asOfDate) {
        return JPA.em().
                createQuery(
                        "select p from models.origo.structuredcontent.StructuredPage p " +
                                "where p.id in (" +
                                "select l.id from models.origo.core.RootNode l " +
                                "where l.version = (" +
                                "select max(l2.version) from models.origo.core.RootNode l2 " +
                                "where l2.nodeId = l.nodeId and " +
                                "(l2.publish = null or l2.publish < :today) and " +
                                "(l2.unPublish = null or l2.unPublish >= :today)" +
                                ")" +
                                ")").
                setParameter("today", asOfDate).
                getResultList();
    }

    public static StructuredPage findCurrentVersion(String nodeId, Date asOfDate) {
        return (StructuredPage) JPA.em().
                createQuery(
                        "select p from models.origo.structuredcontent.StructuredPage p " +
                                "where p.nodeId = :nodeId and p.id in (" +
                                "select l.id from models.origo.core.RootNode l " +
                                "where l.version = (" +
                                "select max(l2.version) from models.origo.core.RootNode l2 " +
                                "where l2.nodeId = l.nodeId and " +
                                "(l2.publish = null or l2.publish < :today) and " +
                                "(l2.unPublish = null or l2.unPublish >= :today)" +
                                ")" +
                                ")").
                setParameter("nodeId", nodeId).
                setParameter("today", asOfDate).
                getSingleResult();
    }

    public static StructuredPage findWithNodeIdAndSpecificVersion(String nodeId, Integer version) {
        return (StructuredPage) JPA.em().
                createQuery(
                        "select distinct p from models.origo.structuredcontent.StructuredPage p " +
                                "where p.nodeId = :nodeId and p.version = :version").
                setParameter("nodeId", nodeId).
                setParameter("version", version).
                getSingleResult();
    }

}

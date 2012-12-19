package models.origo.core;

import main.origo.core.Node;
import main.origo.core.ui.UIElement;
import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * The basic type for a page. Directly linked to a RootNode, both it's version and id.
 *
 * @see Node
 * @see RootNode
 * @see main.origo.core.interceptors.BasicPageProvider
 */
@Entity
@Table(name = "page_basic", uniqueConstraints = @UniqueConstraint(name = "pageVersion", columnNames = {"parentNodeId", "parentVersion"}))
public class BasicPage implements Node {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

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

    @Constraints.Required
    public String leadReferenceId;

    @Constraints.Required
    public String bodyReferenceId;

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
    public UIElement addTailUIElement(UIElement uiElement) {
        return rootNode.addTailUIElement(uiElement);
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

    public BasicPage copy() {
        BasicPage newPage = new BasicPage();
        RootNode rootNodeCopy = rootNode.copy(true);
        newPage.rootNode = rootNodeCopy;
        newPage.nodeId = nodeId;
        newPage.version = rootNodeCopy.version;
        newPage.title = title;
        newPage.leadReferenceId = leadReferenceId;
        newPage.bodyReferenceId = bodyReferenceId;
        return newPage;
    }

    @Override
    public String toString() {
        return new StringBuilder().
                append("BasicPage {").
                append("nodeId='").append(nodeId).append("\', ").
                append("version=").append(version).append(", ").
                append("rootNode=").append(rootNode).append(", ").
                append("title='").append(title).append("\', ").
                append("leadReferenceId='").append(leadReferenceId).append("\', ").
                append("bodyReferenceId='").append(bodyReferenceId).append("\', ").
                append('}').toString();
    }

    public static List<BasicPage> findAllCurrentVersions(Date asOfDate) {
        String queryString = "select p from models.origo.core.BasicPage p " +
                "where p.id in (" +
                "select n.id from models.origo.core.RootNode n where n.version = (" +
                "select max(n2.version) from models.origo.core.RootNode n2 " +
                "where n2.nodeId = n.nodeId and " +
                "(n2.publish = null or n2.publish < :today) and" +
                "(n2.unPublish = null or n2.unPublish >= :today)" +
                "))";
        final Query query = JPA.em().createQuery(queryString);
        query.setParameter("today", asOfDate);
        return query.getResultList();
    }

    public static BasicPage findCurrentVersion(String nodeId, Date asOfDate) {
        String queryString = "select p from models.origo.core.BasicPage p " +
                "where p.nodeId = :nodeId and p.id in (" +
                "select n.id from models.origo.core.RootNode n where n.version = (" +
                "select max(n2.version) from models.origo.core.RootNode n2 " +
                "where n2.nodeId = n.nodeId and " +
                "(n2.publish = null or n2.publish < :today) and" +
                "(n2.unPublish = null or n2.unPublish >= :today)" +
                "))";
        final Query query = JPA.em().createQuery(queryString);
        query.setParameter("nodeId", nodeId);
        query.setParameter("today", asOfDate);
        return (BasicPage) query.getSingleResult();
    }

    public static BasicPage findLatestVersion(String nodeId) {
        String queryString = "select p from models.origo.core.BasicPage p " +
                "where p.nodeId = :nodeId and p.version = (" +
                "select max(n.version) from models.origo.core.RootNode n " +
                "where n.nodeId = p.nodeId" +
                ")";
        final Query query = JPA.em().createQuery(queryString);
        query.setParameter("nodeId", nodeId);
        return (BasicPage) query.getSingleResult();
    }

    public static BasicPage findWithNodeIdAndSpecificVersion(String nodeId, Integer version) {
        String queryString = "select p from models.origo.core.BasicPage p " +
                "where p.nodeId = :nodeId and p.version = :version";
        final Query query = JPA.em().createQuery(queryString);
        query.setParameter("nodeId", nodeId);
        query.setParameter("version", version);
        return (BasicPage) query.getSingleResult();
    }

    public static List<BasicPage> findAllLatestVersions() {
        String queryString = "select p from models.origo.core.BasicPage p " +
                "where p.version = (" +
                "select max(n.version) from models.origo.core.RootNode n " +
                "where n.nodeId = p.nodeId" +
                ")";
        final Query query = JPA.em().createQuery(queryString);
        return query.getResultList();
    }

    public BasicPage save() {
        JPA.em().persist(this);
        return this;
    }

}


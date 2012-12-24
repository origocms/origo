package models.origo.core;

import main.origo.core.Node;
import main.origo.core.ui.Element;
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
        return rootNode.addUIElement(element, false);
    }

    @Override
    public Element addHeadUIElement(Element element, boolean reorderElementsBelow) {
        return rootNode.addHeadUIElement(element, reorderElementsBelow);
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


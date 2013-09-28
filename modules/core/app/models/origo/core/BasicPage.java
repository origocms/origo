package models.origo.core;

import main.origo.core.Node;
import main.origo.core.ui.Element;
import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.Collections;
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
public class BasicPage extends Model<BasicPage> implements Node {

    public static final String TYPE = "basicpage";

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

    public String themeVariant;

    public BasicPage() {
        super(TYPE);
        rootNode = new RootNode(0);
    }

    @Override
    public String nodeId() {
        return this.nodeId;
    }

    @Override
    public String nodeType() {
        return TYPE;
    }

    @Override
    public Integer version() {
        return this.version;
    }

    @Override
    public Date published() {
        return this.rootNode.published();
    }

    @Override
    public Date unpublished() {
        return this.rootNode.unpublished();
    }

    @Override
    public String title() {
        return this.title;
    }

    @Override
    public String themeVariant() {
        return this.themeVariant;
    }

    @Override
    public Set<String> regions() {
        return rootNode.regions();
    }

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
        return rootNode.addElement(element, false);
    }

    @Override
    public Element addHeadElement(Element element, boolean reorderElementsBelow) {
        return rootNode.addHeadElement(element, reorderElementsBelow);
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

    public BasicPage copy() {
        BasicPage newPage = new BasicPage();
        RootNode rootNodeCopy = rootNode.copy(true);
        newPage.rootNode = rootNodeCopy;
        newPage.nodeId = nodeId;
        newPage.title = title;
        newPage.version = rootNodeCopy.version();
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
                append("leadReferenceId='").append(leadReferenceId).append("\', ").
                append("bodyReferenceId='").append(bodyReferenceId).append("\', ").
                append('}').toString();
    }

    public static List<BasicPage> findAllCurrentVersions(Date asOfDate) {
        try {
            String queryString = "select p from " + BasicPage.class.getName() + " p " +
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
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    public static BasicPage findPublishedVersion(String nodeId, Date asOfDate) {
        try {
            return (BasicPage) JPA.em().createQuery(
                    "select p from " + BasicPage.class.getName() + " p " +
                            "where p.nodeId = :nodeId and p.id in (" +
                            "select n.id from models.origo.core.RootNode n where n.version = (" +
                            "select max(n2.version) from models.origo.core.RootNode n2 " +
                            "where n2.nodeId = n.nodeId and " +
                            "(n2.publish = null or n2.publish < :today) and" +
                            "(n2.unPublish = null or n2.unPublish >= :today)" +
                            "))"
            ).setParameter("nodeId", nodeId).setParameter("today", asOfDate).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static BasicPage findLatestPublishedVersion(String nodeId) {
        return findPublishedVersion(nodeId, new Date());
    }

    public static BasicPage findLatestVersion(String nodeId) {
        try {
            String queryString = "select p from " + BasicPage.class.getName() + " p " +
                    "where p.nodeId = :nodeId and p.version = (" +
                    "select max(n.version) from models.origo.core.RootNode n " +
                    "where n.nodeId = p.nodeId" +
                    ")";
            final Query query = JPA.em().createQuery(queryString);
            query.setParameter("nodeId", nodeId);
            return (BasicPage) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static BasicPage findWithNodeIdAndSpecificVersion(String nodeId, Integer version) {
        try {
            String queryString = "select p from " + BasicPage.class.getName() + " p " +
                    "where p.nodeId = :nodeId and p.version = :version";
            final Query query = JPA.em().createQuery(queryString);
            query.setParameter("nodeId", nodeId);
            query.setParameter("version", version);
            return (BasicPage) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static List<BasicPage> findAllLatestVersions() {
        try {
            String queryString = "select p from " + BasicPage.class.getName() + " p " +
                    "where p.version = (" +
                    "select max(n.version) from models.origo.core.RootNode n " +
                    "where n.nodeId = p.nodeId" +
                    ")";
            final Query query = JPA.em().createQuery(queryString);
            return query.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

}


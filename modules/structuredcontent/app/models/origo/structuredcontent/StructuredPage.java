package models.origo.structuredcontent;

import main.origo.core.Node;
import main.origo.core.annotations.Core;
import main.origo.core.event.forms.OnCreateEventGenerator;
import main.origo.core.event.forms.OnDeleteEventGenerator;
import main.origo.core.event.forms.OnUpdateEventGenerator;
import main.origo.core.ui.Element;
import models.origo.core.Meta;
import models.origo.core.Model;
import models.origo.core.RootNode;
import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="page_structured", uniqueConstraints = @UniqueConstraint(name = "pageVersion", columnNames = {"parentNodeId", "parentVersion"}))
public class StructuredPage extends Model<StructuredPage> implements Node {

    public static final String TYPE = Core.With.CONTENT_PAGE + ".structuredpage";

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

    public String themeVariant;

    public StructuredPage() {
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

    @Override
    public String toString() {
        return new StringBuilder().
                append("StructuredPage {").
                append("nodeId='").append(nodeId).append("\', ").
                append("version=").append(version).append(", ").
                append("rootNode=").append(rootNode).append(", ").
                append("}").toString();
    }

    public static List<StructuredPage> findAllCurrentVersions(Date asOfDate) {
        //noinspection unchecked
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

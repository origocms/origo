package models.origo.structuredcontent;

import main.origo.core.Node;
import main.origo.core.annotations.Core;
import main.origo.core.event.forms.OnCreateEventGenerator;
import main.origo.core.event.forms.OnDeleteEventGenerator;
import main.origo.core.event.forms.OnUpdateEventGenerator;
import main.origo.core.ui.Element;
import models.origo.core.Meta;
import models.origo.core.RootNode;
import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="page_structured", uniqueConstraints = @UniqueConstraint(name = "pageVersion", columnNames = {"parentNodeId", "parentVersion"}))
public class StructuredPage implements Node {

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
                append("title='").append(title).append("\'").
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

    public StructuredPage create() {
        OnCreateEventGenerator.triggerBeforeInterceptors(Core.With.CONTENT_PAGE+".structured", this);
        JPA.em().persist(this);
        OnCreateEventGenerator.triggerAfterInterceptors(Core.With.CONTENT_PAGE+".structured", this);
        return this;
    }

    public StructuredPage update() {
        OnUpdateEventGenerator.triggerBeforeInterceptors(Core.With.CONTENT_PAGE+".structured", this);
        JPA.em().merge(this);
        OnUpdateEventGenerator.triggerAfterInterceptors(Core.With.CONTENT_PAGE+".structured", this);
        return this;
    }

    public void delete() {
        OnDeleteEventGenerator.triggerBeforeInterceptors(Core.With.CONTENT_PAGE+".structured", this);
        JPA.em().remove(this);
        OnDeleteEventGenerator.triggerAfterInterceptors(Core.With.CONTENT_PAGE+".structured", this);
    }
}

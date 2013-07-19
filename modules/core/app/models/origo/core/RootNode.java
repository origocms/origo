package models.origo.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import main.origo.core.Node;
import main.origo.core.helpers.ElementHelper;
import main.origo.core.ui.Element;
import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "node", uniqueConstraints = @UniqueConstraint(name = "nodeVersion", columnNames = {"nodeId", "version"}))
public final class RootNode extends Model<RootNode> implements Node {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Constraints.Required
    private String nodeId;

    @Constraints.Required
    private Integer version;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date publish;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date unPublish;

    @Column(name = "type")
    private String nodeType;

    /**
     * Only kept to make sure all elements added to the HEAD region are unique (we don't want duplicate javascript or css resources).
     */
    @Transient
    private Map<String, Element> headElement = Maps.newHashMap();
    /**
     * Only kept to make sure all scripts added to the bottom of PAGE are unique (we don't want duplicate javascript here either).
     */
    @Transient
    private Map<String, Element> tailElement = Maps.newHashMap();
    /**
     * A list of Elements for each region on the page. The key is the region name.
     */
    @Transient
    private Map<String, ArrayList<Element>> elements = Maps.newHashMap();

    protected RootNode() {
        super("root");
    }

    public RootNode(Integer version) {
        this(UUID.randomUUID().toString(), version);
    }

    public RootNode(String nodeId, Integer version) {
        this();
        this.nodeId = nodeId;
        this.version = version;
    }

    @Override
    public String title() {
        return toString();
    }

    @Override
    public String nodeId() {
        return nodeId;
    }

    public void nodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String nodeType() {
        return nodeType;
    }

    public void nodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    @Override
    public Integer version() {
        return version;
    }

    public void version(Integer version) {
        this.version = version;
    }

    @Override
    public Date published() {
        return publish;
    }

    public void published(Date date) {
        this.publish = date;
    }

    @Override
    public Date unpublished() {
        return unPublish;
    }

    public void unpublished(Date date) {
        this.unPublish = date;
    }

    @Override
    public String themeVariant() {
        return null;
    }

    @Override
    public Set<String> regions() {
        return this.elements.keySet();
    }

    /* Interface methods */
    @Override
    public List<Element> elements(String region) {
        return this.elements.get(region.toLowerCase());
    }

    @Override
    public Element addHeadElement(Element element) {
        return addHeadElement(element, false);
    }

    @Override
    public Element addTailElement(Element element) {
        return addTailElement(element, false);
    }

    @Override
    public Element addElement(Element element) {
        return addElement(element, false);
    }

    @Override
    public Element addHeadElement(Element element, boolean reorderElementsBelow) {
        String elementKey = String.valueOf(element.hashCode());
        if (headElement.containsKey(elementKey)) {
            return headElement.get(elementKey);
        }
        headElement.put(elementKey, element);
        return addElement(element, reorderElementsBelow, HEAD, element.getWeight());
    }

    @Override
    public Element addTailElement(Element element, boolean reorderElementsBelow) {
        String elementKey = String.valueOf(element.hashCode());
        if (tailElement.containsKey(elementKey)) {
            return tailElement.get(elementKey);
        }
        tailElement.put(elementKey, element);
        return addElement(element, reorderElementsBelow, TAIL, element.getWeight());
    }

    @Override
    public Element addElement(Element element, boolean reorderElementsBelow) {
        Meta meta = Meta.findWithNodeIdAndSpecificVersion(nodeId, version, element.id);
        if (meta == null) {
            meta = Meta.defaultMeta();
        }

        return addElement(element, meta, reorderElementsBelow);
    }

    @Override
    public Element addElement(Element element, Meta meta) {
        return addElement(element, meta, false);
    }

    @Override
    public Element addElement(Element element, Meta meta, boolean reorderElementsBelow) {
        return addElement(element, reorderElementsBelow, meta.region.toLowerCase(), meta.weight);
    }

    private Element addElement(Element element, boolean reorderElementsBelow, String regionKey, int weight) {
        if (!elements.containsKey(regionKey)) {
            elements.put(regionKey, Lists.<Element>newArrayList());
        }
        element.setWeight(weight);
        elements.get(regionKey).add(element);
        if (reorderElementsBelow) {
            ElementHelper.repositionElements(elements.get(regionKey), element);
        }
        ElementHelper.reorderElements(elements.get(regionKey));
        return element;
    }

    @Override
    public boolean removeHeadElement(Element element) {
        return removeElement(element, HEAD);
    }

    @Override
    public boolean removeTailElement(Element element) {
        return removeElement(element, TAIL);
    }

    @Override
    public boolean removeElement(Element element) {
        Meta meta = Meta.findWithNodeIdAndSpecificVersion(nodeId, version, element.id);
        if (meta == null) {
            meta = Meta.defaultMeta();
        }
        String regionKey = meta.region.toLowerCase();
        return removeElement(element, regionKey);
    }

    private boolean removeElement(Element element, String regionKey) {
        if (elements.get(regionKey).remove(element)) {
            ElementHelper.reorderElements(elements.get(regionKey));
            return true;
        }
        return false;
    }

    @Override
    public boolean hasElements() {
        for (String region : regions()) {
            if (elements(region).size()>1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Node (" + nodeType + " - " + nodeId + "," + version + ")";
    }

    private static void initializeNode(RootNode node) {
        node.elements = Maps.newHashMap();
        node.elements.put(HEAD, Lists.<Element>newArrayList());
        node.headElement = Maps.newHashMap();
        node.tailElement = Maps.newHashMap();
    }

    private static void initializeNodes(Collection<RootNode> nodes) {
        for (RootNode node : nodes) {
            initializeNode(node);
        }
    }

    public static List<RootNode> findCurrentPublishedVersions() {
        return findCurrentPublishedVersionsWithDate(new Date());
    }

    public static List<RootNode> findCurrentVersions() {
        try {
            String queryString = "select distinct n from "+RootNode.class.getName()+" n " +
                    "order by n.version desc";
            final Query query = JPA.em().createQuery(queryString);
            List<RootNode> nodes = query.getResultList();
            initializeNodes(nodes);
            return nodes;
        } catch (NoResultException e) {
            return Collections.emptyList();
        }

    }

    public static List<RootNode> findCurrentPublishedVersionsWithDate(Date today) {
        try {
            String queryString = "select n from "+RootNode.class.getName()+" n " +
                    "where n.version = (" +
                    "select max(n2.version) from "+RootNode.class.getName()+" n2 " +
                    "where n2.nodeId = n.nodeId and " +
                    "(n2.publish = null or n2.publish < :today) and" +
                    "(n2.unPublish = null or n2.unPublish >= :today)" +
                    ")";
            final Query query = JPA.em().createQuery(queryString);
            query.setParameter("today", today);
            List<RootNode> nodes = query.getResultList();
            initializeNodes(nodes);
            return nodes;
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    public static RootNode findWithNodeIdAndSpecificVersion(String nodeId, Integer version) {
        try {
            String queryString = "select n from "+RootNode.class.getName()+" n " +
                    "where n.nodeId = :nodeId and n.version = :version";
            final Query query = JPA.em().createQuery(queryString);
            query.setParameter("nodeId", nodeId);
            query.setParameter("version", version);
            RootNode node = (RootNode) query.getSingleResult();
            if (node != null) {
                initializeNode(node);
            }
            return node;
        } catch (NoResultException e) {
            return null;
        }
    }

    public static RootNode findLatestPublishedVersionWithNodeId(String nodeId) {
        return findPublishedVersionWithNodeIdAndDate(nodeId, new Date());
    }

    public static RootNode findPublishedVersionWithNodeIdAndDate(String nodeId, Date today) {
        try {
            String queryString = "select distinct n from "+RootNode.class.getName()+" n " +
                    "where n.nodeId = :nodeId and " +
                    "(n.publish = null or n.publish < :today) and " +
                    "(n.unPublish = null or n.unPublish >= :today) " +
                    "order by n.version desc";
            final Query query = JPA.em().createQuery(queryString);
            query.setParameter("nodeId", nodeId);
            query.setParameter("today", today);
            List<RootNode> nodes = query.getResultList();
            if (nodes.isEmpty()) {
                return null;
            }
            RootNode node = nodes.get(0);
            initializeNode(node);
            return node;
        } catch (NoResultException e) {
            return null;
        }
    }

    public static RootNode findLatestVersionWithNodeId(String nodeId) {
        try {
            String queryString = "select distinct n from "+RootNode.class.getName()+" n " +
                    "where n.nodeId = :nodeId " +
                    "order by n.version desc";
            final Query query = JPA.em().createQuery(queryString);
            query.setParameter("nodeId", nodeId);
            List<RootNode> nodes = query.getResultList();
            if (nodes.isEmpty()) {
                return null;
            }
            RootNode node = nodes.get(0);
            initializeNode(node);
            return node;
        } catch (NoResultException e) {
            return null;
        }
    }

    public static List<RootNode> findAllVersionsWithNodeId(String nodeId) {
        try {
            String queryString = "select distinct n from "+RootNode.class.getName()+" n " +
                    "where n.nodeId = :nodeId " +
                    "order by n.version desc";
            final Query query = JPA.em().createQuery(queryString);
            query.setParameter("nodeId", nodeId);
            List<RootNode> nodes = query.getResultList();
            initializeNodes(nodes);
            return nodes;
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    public RootNode copy() {
        return copy(false);
    }

    public RootNode copy(boolean increaseVersion) {
        RootNode copy = new RootNode(nodeId, increaseVersion ? version + 1 : version);
        copy.publish = publish;
        copy.unPublish = unPublish;
        copy.nodeType = nodeType;
        return copy;
    }

}

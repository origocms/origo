package models.origo.core;

import controllers.origo.core.helpers.UIElementHelper;
import origo.core.Node;
import origo.core.ui.UIElement;

import java.util.*;

import play.data.validation.Constraints.*;
import play.db.ebean.Model;

import javax.persistence.*;

@Entity(name = "RootNode")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"nodeId", "version"}))
public final class RootNode extends Model implements Node {

    @Id
    public Long id;
    
    @Required
    @Column(name = "nodeId")
    public String nodeId;

    @Required
    @Column(name = "version")
    public Integer version;

    @Temporal(value = TemporalType.TIMESTAMP)
    public Date publish;

    @Temporal(value = TemporalType.TIMESTAMP)
    public Date unPublish;

    public String type;

    public String themeVariant;

    /**
     * Only kept to make sure all elements added to the HEAD region are unique (we don't want duplicate javascript or css resources.)
     */
    @Transient
    private transient Map<String, UIElement> headElements = new HashMap<String, UIElement>();
    /**
     * A list of UIElements for each region on the page. The key is the region name.
     */
    @Transient
    private transient Map<String, List<UIElement>> uiElements = new HashMap<String, List<UIElement>>();

    public RootNode(Integer version) {
        this(UUID.randomUUID().toString(), version);
    }

    public RootNode(String nodeId, Integer version) {
        this.nodeId = nodeId;
        this.version = version;
    }

    @Override
    public String getTitle() {
        return toString();
    }

    @Override
    public String getNodeId() {
        return nodeId;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public Date getDatePublished() {
        return publish;
    }

    @Override
    public Date getDateUnpublished() {
        return unPublish;
    }

    @Override
    public String getThemeVariant() {
        return themeVariant;
    }

    @Override
    public Set<String> getRegions() {
        return this.uiElements.keySet();
    }

    /* Interface methods */
    @Override
    public List<UIElement> getUIElements(String region) {
        return this.uiElements.get(region.toLowerCase());
    }

    @Override
    public UIElement addHeadUIElement(UIElement uiElement) {
        return addHeadUIElement(uiElement, false);
    }

    @Override
    public UIElement addUIElement(UIElement uiElement) {
        return addUIElement(uiElement, false);
    }

    @Override
    public UIElement addHeadUIElement(UIElement uiElement, boolean reorderElementsBelow) {
        return UIElementHelper.addHeadUIElement(headElements, uiElements, uiElement, reorderElementsBelow);
    }

    @Override
    public UIElement addUIElement(UIElement uiElement, boolean reorderElementsBelow) {
        return UIElementHelper.addUIElementUsingMeta(uiElements, uiElement, reorderElementsBelow, nodeId, version);
    }

    private UIElement addUIElement(UIElement uiElement, boolean reorderElementsBelow, String regionKey, int weight) {
        return UIElementHelper.addUIElementToRegion(uiElements, uiElement, reorderElementsBelow, regionKey, weight);
    }

    @Override
    public boolean removeHeadUIElement(UIElement uiElement) {
        return UIElementHelper.removeHeadUIElement(uiElements, uiElement);
    }

    @Override
    public boolean removeUIElement(UIElement uiElement) {
        return UIElementHelper.removeUIElement(uiElements, uiElement, nodeId, version);
    }


    public static Finder<Long, RootNode> find = new Finder<Long, RootNode>(
            Long.class, RootNode.class
    );

    /*
    public static List<RootNode> findAllCurrentVersions(Date today) {
        List<RootNode> leaves = RootNode.find(
                "select l from RootNode l " +
                        "where l.version = (" +
                        "select max(l2.version) from RootNode l2 " +
                        "where l2.nodeId = l.nodeId and " +
                        "(l2.publish = null or l2.publish < :today) and " +
                        "(l2.unPublish = null or l2.unPublish >= :today)" +
                        ")"
        ).bind("today", today).fetch();
        for (RootNode node : leaves) {
            initializeNode(node);
        }
        return leaves;
    }

    public static RootNode findWithNodeIdAndSpecificVersion(String nodeId, Long version) {
        RootNode node = RootNode.find(
                "select distinct n from RootNode n " +
                        "where n.nodeId = :nodeId and " +
                        "n.version = :version"
        ).bind("nodeId", nodeId).bind("version", version).first();
        if (node != null) {
            initializeNode(node);
        }
        return node;
    }

    public static RootNode findLatestPublishedVersionWithNodeId(String nodeId, Date today) {
        RootNode node = RootNode.find(
                "select distinct n from RootNode n " +
                        "where n.nodeId = :nodeId and " +
                        "(n.publish = null or n.publish < :today) and " +
                        "(n.unPublish = null OR n.unPublish >= :today)" +
                        "order by version desc"
        ).bind("nodeId", nodeId).bind("today", today).first();
        if (node != null) {
            initializeNode(node);
        }
        return node;
    }

    public static RootNode findLatestVersionWithNodeId(String nodeId) {
        RootNode node = RootNode.find(
                "select distinct n from RootNode n " +
                        "where n.nodeId = :nodeId " +
                        "order by version desc"
        ).bind("nodeId", nodeId).first();
        if (node != null) {
            initializeNode(node);
        }
        return node;
    }

    public static List<RootNode> findAllVersionsWithNodeId(String nodeId) {
        List<RootNode> leaves = RootNode.find(
                "select distinct n from RootNode n where n.nodeId = :nodeId"
        ).bind("nodeId", nodeId).fetch();
        for (RootNode node : leaves) {
            initializeNode(node);
        }
        return leaves;
    }
    */

    public static RootNode findLatestPublishedVersionWithNodeId(String nodeId, Date date) {
        RootNode node = find.setQuery("select distinct n from RootNode n " +
                        "where n.nodeId = :nodeId and " +
                        "(n.publish = null or n.publish < :today) and " +
                        "(n.unPublish = null OR n.unPublish >= :today)" +
                        "order by version desc"
        ).setParameter("nodeId", nodeId).setParameter("today", date).findUnique();
        if (node != null) {
            node.initialize();
        }
        return node;
    }

    public static RootNode findWithNodeIdAndSpecificVersion(String nodeId, long version) {
        RootNode rootNode = find.where().
                eq("nodeId", nodeId).
                eq("version", version).
                findUnique();
        if (rootNode != null) {
            return rootNode.initialize();
        }
        return null;
    }

    private RootNode initialize() {
        uiElements = new HashMap<String, List<UIElement>>();
        uiElements.put(HEAD, new ArrayList<UIElement>());
        headElements = new HashMap<String, UIElement>();
        return this;
    }

    @Override
    public String toString() {
        return "Node (" + nodeId + "," + version + ")";
    }

    public RootNode copy() {
        return copy(false);
    }

    public RootNode copy(boolean increaseVersion) {
        RootNode copy = new RootNode(nodeId, increaseVersion ? ++version : version);
        copy.publish = publish;
        copy.unPublish = unPublish;
        copy.type = type;
        copy.themeVariant = themeVariant;
        return copy;
    }
}

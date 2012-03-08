package models;

import controllers.core.Node;
import controllers.helpers.UIElementHelper;
import controllers.ui.UIElement;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.*;

import play.data.validation.Constraints.*;

/**
 * The basic type for a page. Directly linked to a RootNode, both it's version and id.
 *
 * @see Node
 * @see RootNode
 * @see controllers.listeners.BasicPageProvider
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"parentNodeId", "parentVersion"}))
public class BasicPage extends Model implements Node {

    @Id
    public Long id;

    @Required
    @Column(name = "parentNodeId")
    public String nodeId;

    @Required
    @Column(name = "parentVersion")
    public Integer version;

    @Temporal(value = TemporalType.TIMESTAMP)
    public Date publish;

    @Temporal(value = TemporalType.TIMESTAMP)
    public Date unPublish;

    public String themeVariant;

    @Required
    public String title;

    @Required
    public String leadReferenceId;

    @Required
    public String bodyReferenceId;

    /**
     * Only kept to make sure all elements added to the HEAD region are unique (we don't want duplicate javascript or css resources.)
     */
    private transient Map<String, UIElement> headElements = new HashMap<String, UIElement>();
    /**
     * A list of UIElements for each region on the page. The key is the region name.
     */
    private transient Map<String, List<UIElement>> uiElements = new HashMap<String, List<UIElement>>();

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
        return this.publish;
    }

    @Override
    public Date getDateUnpublished() {
        return this.unPublish;
    }

    @Override
    public String getTitle() {
        return title;
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

    @Override
    public String toString() {
        return new StringBuilder().
                append("BasicPage {").
                append("nodeId='").append(nodeId).append("\', ").
                append("version=").append(version).append(", ").
                append("title='").append(title).append("\', ").
                append("leadReferenceId='").append(leadReferenceId).append("\', ").
                append("bodyReferenceId='").append(bodyReferenceId).append("\', ").
                append('}').toString();
    }

    public static Finder<Long, BasicPage> find = new Finder<Long, BasicPage>(
            Long.class, BasicPage.class
    );

    /*
    public static List<BasicPage> findAllCurrentVersions(Date asOfDate) {
        return BasicPage.
                find(
                        "select p from BasicPage p " +
                                "where p.id in (" +
                                "select l.id from RootNode l " +
                                "where l.version = (" +
                                "select max(l2.version) from RootNode l2 " +
                                "where l2.nodeId = l.nodeId and " +
                                "(l2.publish = null or l2.publish < :today) and " +
                                "(l2.unPublish = null or l2.unPublish >= :today)" +
                                ")" +
                                ")").
                bind("today", asOfDate).
                fetch();
    }

    public static BasicPage findCurrentVersion(String nodeId, Date asOfDate) {
        return BasicPage.
                find(
                        "select p from BasicPage p " +
                                "where p.nodeId = :nodeId and p.id in (" +
                                "select l.id from RootNode l " +
                                "where l.version = (" +
                                "select max(l2.version) from RootNode l2 " +
                                "where l2.nodeId = l.nodeId and " +
                                "(l2.publish = null or l2.publish < :today) and " +
                                "(l2.unPublish = null or l2.unPublish >= :today)" +
                                ")" +
                                ")").
                bind("nodeId", nodeId).
                bind("today", asOfDate).
                first();
    }

    public static BasicPage findLatestVersion(String nodeId) {
        return BasicPage.
                find(
                        "select p from BasicPage p " +
                                "where p.nodeId = :nodeId and p.version = (" +
                                "select max(n.version) from RootNode n " +
                                "where p.nodeId = n.nodeId" +
                                ")").
                bind("nodeId", nodeId).
                first();
    }

    public static BasicPage findWithNodeIdAndSpecificVersion(String nodeId, Long version) {
        return RootNode.
                find("select distinct p from BasicPage p where p.nodeId = :nodeId and p.version = :version").
                bind("nodeId", nodeId).
                bind("version", version).
                first();
    }

    public static List<BasicPage> findAllLatestVersions() {
        return BasicPage.
                find(
                        "select p from BasicPage p " +
                                "where p.version = (" +
                                "select max(n.version) from RootNode n " +
                                "where p.nodeId = n.nodeId" +
                                ")").

                fetch();
    }
    */

    public static BasicPage findWithNodeIdAndSpecificVersion(String nodeId, Integer version) {
        return find.where().
                eq("nodeId", nodeId).
                eq("version", version).
                findUnique();
    }
}


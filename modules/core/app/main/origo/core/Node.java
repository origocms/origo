package main.origo.core;

import main.origo.core.ui.Element;
import models.origo.core.Meta;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * An interface that each module/add-on/plugin should implement if it adds a type with a \@Provides annotation.
 * While the modules/add-ons/plugins handle a rootNode and modify it, it will be of this type. When rendering
 * starts this will be turned into a RenderedNode.
 *
 * @see main.origo.core.annotations.Provides
 * @see main.origo.core.annotations.ThemeVariant
 * @see main.origo.core.ui.Element
 */
public interface Node {

    static final String HEAD = "head";
    static final String TAIL = "tail";

    /**
     * The node id (uuid for now) of the node
     *
     * @return a unique key for this node
     */
    String getNodeId();

    /**
     * The version of this node
     *
     * @return a version number
     */
    Integer getVersion();

    /**
     * A title for this node
     *
     * @return a title
     */
    String getTitle();

    /**
     * The date this version should be available for public viewing
     *
     * @return a date
     */
    Date getDatePublished();

    /**
     * The date this version should be removed from public viewing
     *
     * @return a date
     */
    Date getDateUnpublished();

    /**
     * The unique name of the theme variant used for this page
     *
     * @return theme variant name
     * @see main.origo.core.annotations.ThemeVariant
     */
    String getThemeVariant();

    /**
     * All the available regions stored on this node.
     *
     * @return a set of region names that can be used for showing content
     * @see main.origo.core.annotations.ThemeVariant
     */
    Set<String> getRegions();

    /**
     * A collection of UIElements that should be rendered on the screen. Regions are determined by the theme variant used.
     *
     * @param region the area of the screen where this element should be rendered
     * @return all uiElements for the region
     */
    List<Element>getElements(String region);

    /**
     * Add an element that should be rendered in the head region of the page. The head region always exists.
     *
     * @param element the element to be rendered
     * @return the newly added Element
     * @see main.origo.core.annotations.ThemeVariant
     */
    Element addHeadElement(Element element);

    /**
     * Add an element that should be rendered in the tail of the page. The head region always exists.
     *
     * @param element the element to be rendered
     * @return the newly added Element
     * @see main.origo.core.annotations.ThemeVariant
     */
    Element addTailElement(Element element);

    /**
     * Add an element that should be rendered on the page. Regions are determined by the theme variant used.
     *
     * @param element the element to be rendered
     * @return the newly added Element
     * @see main.origo.core.annotations.ThemeVariant
     */
    Element addElement(Element element);

    /**
     * Add an element that should be rendered in the head region of the page. The head region always exists.
     *
     * @param element            the element to be rendered
     * @param reorderElementsBelow if true then all elements below this new element will be reordered according to their individual weight
     * @return the newly added Element
     * @see main.origo.core.annotations.ThemeVariant
     */
    Element addHeadElement(Element element, boolean reorderElementsBelow);

    /**
     * Add an element that should be rendered in the tail of the page. The head region always exists.
     *
     * @param element            the element to be rendered
     * @param reorderElementsBelow if true then all elements below this new element will be reordered according to their individual weight
     * @return the newly added Element
     * @see main.origo.core.annotations.ThemeVariant
     */
    Element addTailElement(Element element, boolean reorderElementsBelow);

    /**
     * Add an element that should be rendered on the page. Meta information is loaded based on nodeId. Regions are determined by the theme variant used.
     *
     * @param element            the element to be rendered
     * @param reorderElementsBelow if true then all elements below this new element will be reordered according to their individual weight
     * @return the newly added Element
     * @see main.origo.core.annotations.ThemeVariant
     */
    Element addElement(Element element, boolean reorderElementsBelow);

    /**
     * Add an element that should be rendered on the page with preloaded meta information. Regions are determined by the theme variant used.
     *
     *
     * @param element            the element to be rendered
     * @param meta              preloaded/static meta to be used for placing the element in the page
     * @return the newly added Element
     * @see main.origo.core.annotations.ThemeVariant
     */
    Element addElement(Element element, Meta meta);

    /**
     * Add an element that should be rendered on the page with preloaded meta information. Regions are determined by the theme variant used.
     *
     *
     * @param element            the element to be rendered
     * @param meta              preloaded/static meta to be used for placing the element in the page
     * @param reorderElementsBelow if true then all elements below this new element will be reordered according to their individual weight
     * @return the newly added Element
     * @see main.origo.core.annotations.ThemeVariant
     */
    Element addElement(Element element, Meta meta, boolean reorderElementsBelow);

    /**
     * Removes an element so it is not rendered in the head region of the page. The head region always exists.
     *
     * @param element the element to be rendered
     * @return if an object matching the region and the element could be found and removed
     */
    boolean removeHeadElement(Element element);

    /**
     * Removes an element so it is not rendered in the tail of the page. The head region always exists.
     *
     * @param element the element to be rendered
     * @return if an object matching the region and the element could be found and removed
     */
    boolean removeTailElement(Element element);

    /**
     * Removes an element so it is not rendered. Will force a reordering of all elements below.
     *
     * @param element the element to be rendered
     * @return if an object matching the region and the element could be found and removed
     */
    boolean removeElement(Element element);

    /**
     * Checks if there are any elements added to the node
     */
    boolean hasElements();
}

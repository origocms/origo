package main.origo.core.ui;

import main.origo.core.CachedThemeVariant;
import main.origo.core.Node;

import java.util.Stack;

public class RenderingContext {

    private CachedThemeVariant themeVariant;
    private final Node rootNode;
    private final Stack<Element> parents;

    public RenderingContext(CachedThemeVariant themeVariant, Node rootNode) {
        this.themeVariant = themeVariant;
        this.rootNode = rootNode;
        this.parents = new Stack<Element>();
    }

    public CachedThemeVariant getThemeVariant() {
        return themeVariant;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public Element getParent() {
        return parents.peek();
    }

    public void nest(Element parent) {
        parents.push(parent);
    }

    public void unNest() {
        parents.pop();
    }
}

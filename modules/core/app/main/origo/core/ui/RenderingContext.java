package main.origo.core.ui;

import main.origo.core.Node;
import main.origo.core.event.NodeContext;
import main.origo.core.internal.CachedThemeVariant;

import java.util.Map;
import java.util.Stack;

public class RenderingContext {

    public CachedThemeVariant themeVariant;
    public Node rootNode;
    public RenderedNode renderedNode;
    private Stack<Element> parents;
    public Map<String, Object> attributes;

    public RenderingContext(CachedThemeVariant themeVariant, Node rootNode, RenderedNode renderedNode) {
        this.themeVariant = themeVariant;
        this.rootNode = rootNode;
        this.renderedNode = renderedNode;
        this.parents = new Stack<>();
        this.attributes = NodeContext.current().attributes;
    }

    public Element parent() {
        return parents.peek();
    }

    public void nest(Element parent) {
        parents.push(parent);
    }

    public void unNest() {
        parents.pop();
    }

}

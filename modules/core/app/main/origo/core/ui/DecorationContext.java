package main.origo.core.ui;

import main.origo.core.Node;
import main.origo.core.event.NodeContext;
import main.origo.core.internal.CachedTheme;
import main.origo.core.internal.CachedThemeVariant;

import java.util.Map;
import java.util.Stack;

public class DecorationContext {

    public CachedTheme theme;
    public CachedThemeVariant themeVariant;
    public Node rootNode;
    public DecoratedNode decoratedNode;
    private Stack<Element> parents;
    public Map<String, Object> attributes;

    public DecorationContext(CachedTheme theme, CachedThemeVariant themeVariant, Node rootNode, DecoratedNode decoratedNode) {
        this.theme = theme;
        this.themeVariant = themeVariant;
        this.rootNode = rootNode;
        this.decoratedNode = decoratedNode;
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

package com.origocms.core.ui;

import com.origocms.core.CachedThemeVariant;
import com.origocms.core.Node;

import java.util.Stack;

public class RenderingContext {

    private CachedThemeVariant themeVariant;
    private final Node rootNode;
    private final Stack<UIElement> parents;

    public RenderingContext(CachedThemeVariant themeVariant, Node rootNode) {
        this.themeVariant = themeVariant;
        this.rootNode = rootNode;
        this.parents = new Stack<UIElement>();
    }

    public CachedThemeVariant getThemeVariant() {
        return themeVariant;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public UIElement getParent() {
        return parents.peek();
    }

    public void nestle(UIElement parent) {
        parents.push(parent);
    }

    public void unNestle() {
        parents.pop();
    }
}
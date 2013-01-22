package main.origo.core.ui;

import com.google.common.collect.Lists;
import main.origo.core.event.ElementEventGenerator;
import main.origo.core.helpers.ElementHelper;
import main.origo.core.helpers.ThemeHelper;
import org.apache.commons.lang3.StringUtils;
import play.api.templates.Html;
import play.api.templates.HtmlFormat;
import views.html.origo.core.decorators.*;
import views.html.origo.core.decorators.forms.*;

import java.util.*;

public class Element {

    private static class Base extends Element {

        public String tagName;

        private Base(String tagName, String type) {
            super(type);
            this.tagName = tagName;
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            return base.render(tagName, this, ElementHelper.getHtmlFromBody(this), this.getAttributes());
        }

    }

    public static class Raw extends Element {

        public Raw() {
            super("raw");
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            return getBody();
        }
    }

    public static class Meta extends Element {

        public Meta() {
            super("meta");
        }

        @Override
        public boolean isAlwaysInHead() {
            return true;
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            //noinspection unchecked
            return meta.render(this, ElementHelper.getHtmlFromBody(this), getAttributes());
        }
    }

    public static class Script extends Element {

        public Script() {
            super("script");
        }

        @Override
        public boolean isAlwaysInHead() {
            return false;
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            //noinspection unchecked
            return script.render(this, ElementHelper.getHtmlFromBody(this), this.getAttributes());
        }
    }

    public static class Style extends Element {

        public Style() {
            super("style");
        }

        @Override
        public boolean isAlwaysInHead() {
            return true;
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            //noinspection unchecked
            return style.render(this, ElementHelper.getHtmlFromBody(this), this.getAttributes());
        }
    }

    public static class Link extends Element {

        public Link() {
            super("link");
        }

        @Override
        public boolean isAlwaysInHead() {
            return true;
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            //noinspection unchecked
            return link.render(this, ElementHelper.getHtmlFromBody(this), this.getAttributes());
        }
    }

    public static class Image extends Base {

        public Image() {
            super("img", "image");
        }

    }

    public static class Span extends Element {

        public Span() {
            super("span");
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Html body = ThemeHelper.decorateChildren(this, renderingContext);
            return span.render(this, body, this.getAttributes());
        }
    }

    public static class ListBulleted extends Element {

        public ListBulleted() {
            super("list_bulleted");
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Html body = ThemeHelper.decorateChildren(this, renderingContext);
            //noinspection unchecked
            return list.render("ul", this, body, this.getAttributes());
        }
    }

    public static class ListOrdered extends Element {

        public ListOrdered() {
            super("list_ordered");
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Html body = ThemeHelper.decorateChildren(this, renderingContext);
            //noinspection unchecked
            return list.render("ol", this, body, this.getAttributes());
        }
    }

    public static class ListItem extends Element {

        public ListItem() {
            super("list_item");
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Html body = ElementHelper.getHtmlFromBody(this);
            if (this.hasChildren()) {
                body = ThemeHelper.decorateChildren(this, renderingContext);
            }
            //noinspection unchecked
            return list_item.render(this, body, this.getAttributes());
        }
    }

    public static class Form extends Element {

        public Form() {
            super("form");
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Html body = ThemeHelper.decorateChildren(this, renderingContext);
            //noinspection unchecked
            return form.render(this, body, this.getAttributes());
        }
    }

    public static class Fieldset extends Element {

        public Fieldset() {
            super("fieldset");
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Html body = ThemeHelper.decorateChildren(this, renderingContext);
            //noinspection unchecked
            return fieldset.render(this, body, this.getAttributes());
        }
    }

    public static class Legend extends Base {
        public Legend() {
            super("legend", "legend");
        }
    }

    public static class Label extends Base {
        public Label() {
            super("label", "label");
        }
    }

    public static class InputHidden extends Element {

        public InputHidden() {
            super("input_hidden", String.class);
        }
        public InputHidden(Class c) {
            super("input_hidden", c);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Map<String, String> attributes = ElementHelper.combineAttributes(Collections.<String, String>singletonMap("type", "hidden"), this.getAttributes());
            //noinspection unchecked
            return input.render(this, null, attributes);
        }
    }

    public static class InputText extends Element {

        public InputText() {
            super("input_text", String.class);
        }
        public InputText(Class c) {
            super("input_text", c);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Map<String, String> attributes = ElementHelper.combineAttributes(Collections.<String, String>singletonMap("type", "text"), this.getAttributes());
            //noinspection unchecked
            return input.render(this, null, attributes);
        }
    }

    public static class InputTextArea extends Element {

        public InputTextArea() {
            super("input_textarea", String.class);
        }
        public InputTextArea(Class c) {
            super("input_textarea", c);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Html body = ElementHelper.getHtmlFromBody(this);
            if (this.hasChildren()) {
                body.$plus(ThemeHelper.decorateChildren(this, renderingContext));
            }
            //noinspection unchecked
            return textarea.render(this, body, this.getAttributes());
        }
    }

    public static class InputRadioButton extends Element {

        public InputRadioButton() {
            super("input_radiobutton", String.class);
        }
        public InputRadioButton(Class c) {
            super("input_radiobutton", c);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Map<String, String> attributes = ElementHelper.combineAttributes(Collections.<String, String>singletonMap("type", "radiobutton"), this.getAttributes());
            //noinspection unchecked
            return input.render(this, null, attributes);
        }
    }

    public static class InputSelect extends Element {

        public InputSelect() {
            super("input_select", String.class);
        }
        public InputSelect(Class c) {
            super("input_select", c);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Html body = ThemeHelper.decorateChildren(this, renderingContext);
            //noinspection unchecked
            return select.render(this, body, this.getAttributes());
        }
    }

    public static class InputSelectOption extends Element {

        public InputSelectOption() {
            super("input_select_option", String.class);
        }

        public InputSelectOption(Class c) {
            super("input_select_option", c);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Html body = ElementHelper.getHtmlFromBody(this);
            if (this.hasChildren()) {
                body.$plus(ThemeHelper.decorateChildren(this, renderingContext));
            }
            //noinspection unchecked
            return select_option.render(this, body, this.getAttributes());
        }
    }

    public static class InputButton extends Element {

        public InputButton() {
            super("input_button");
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Map<String, String> attributes = ElementHelper.combineAttributes(Collections.<String, String>singletonMap("type", "button"), this.getAttributes());
            //noinspection unchecked
            return input.render(this, null, attributes);
        }
    }

    public static class InputSubmit extends Element {

        public InputSubmit() {
            super("input_submit");
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Map<String, String> attributes = ElementHelper.combineAttributes(Collections.<String, String>singletonMap("type", "submit"), this.getAttributes());
            //noinspection unchecked
            return input.render(this, null, attributes);
        }
    }

    public static class InputReset extends Element {

        public InputReset() {
            super("input_reset");
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Map<String, String> attributes = ElementHelper.combineAttributes(Collections.<String, String>singletonMap("type", "reset"), this.getAttributes());
            //noinspection unchecked
            return input.render(this, null, attributes);
        }
    }

    public static class InputImage extends Element {

        public InputImage() {
            super("input_image", String.class);
        }
        public InputImage(Class c) {
            super("input_image", c);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Map<String, String> attributes = ElementHelper.combineAttributes(Collections.<String, String>singletonMap("type", "image"), this.getAttributes());
            //noinspection unchecked
            return input.render(this, null, attributes);
        }
    }

    public static class InputFile extends Element {

        public InputFile() {
            super("input_file", String.class);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Map<String, String> attributes = ElementHelper.combineAttributes(Collections.<String, String>singletonMap("type", "file"), this.getAttributes());
            //noinspection unchecked
            return input.render(this, null, attributes);
        }
    }

    public static class InputPassword extends Element {

        public InputPassword() {
            super("input_password", String.class);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Map<String, String> attributes = ElementHelper.combineAttributes(Collections.<String, String>singletonMap("type", "password"), this.getAttributes());
            //noinspection unchecked
            return input.render(this, null, attributes);
        }
    }

    public static class Panel extends Element {

        public Panel() {
            super("panel");
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Html body = ThemeHelper.decorateChildren(this, renderingContext);
            return panel.render(this, body, this.getAttributes());
        }
    }

    public static class Paragraph extends Element {

        public Paragraph() {
            super("paragaph");
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Html body = ThemeHelper.decorateChildren(this, renderingContext);
            //noinspection unchecked
            return paragraph.render(this, body, this.getAttributes());
        }
    }

    public static class Text extends Element {

        public Text() {
            super("text");
        }
        public Text(Class c) {
            super("text", c);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            //noinspection unchecked
            return text.render(this, ElementHelper.getHtmlFromBody(this), this.getAttributes());
        }
    }

    public static class Anchor extends Element {

        public Anchor() {
            super("anchor");
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Html body = ElementHelper.getHtmlFromBody(this);
            if (this.hasChildren()) {
                body = ThemeHelper.decorateChildren(this, renderingContext);
            }
            return anchor.render(this, body, this.getAttributes());
        }
    }

    private static class Heading extends Base {

        private Heading(String tagName, String type) {
            super(tagName, type);
        }

    }

    public static class Heading1 extends Heading {

        public Heading1() {
            //noinspection unchecked
            super("h1", "heading_1");
        }
    }

    public static class Heading2 extends Heading {

        public Heading2() {
            //noinspection unchecked
            super("h2", "heading_2");
        }
    }

    public static class Heading3 extends Heading {

        public Heading3() {
            //noinspection unchecked
            super("h3", "heading_3");
        }
    }

    public static class Heading4 extends Heading {

        public Heading4() {
            //noinspection unchecked
            super("h4", "heading_4");
        }
    }

    public static class Heading5 extends Heading {

        public Heading5() {
            //noinspection unchecked
            super("h5", "heading_5");
        }
    }

    public static class Heading6 extends Heading {

        public Heading6() {
            //noinspection unchecked
            super("h6", "heading_6");
        }
    }

    public static class Emphasize extends Base {
        public Emphasize() {
            super("em", "em");
        }
    }

    public static class Strong extends Base {
        public Strong() {
            super("strong", "strong");
        }
    }


    public String id;

    public String type;

    public Map<String, String> attributes = new WeakHashMap<>();

    private int weight;

    private Element parent;

    private List<Element> children = Lists.newLinkedList();

    private Html body;

    private Class inputType;

    protected Element(String type) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
    }

    protected Element(String type, Class t) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.inputType = t;
    }

    public String getId() {
        return id;
    }

    public Element setId(String id) {
        this.id = id;
        return this;
    }

    public String getType() {
        return type;
    }

    public Class getInputType() {
        return inputType;
    }

    public boolean hasAttributes() {
        return getAttributes() != null && !getAttributes().isEmpty();
    }

    public Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public Element setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
        return this;
    }

    public Element addAttribute(String name, String value) {
        if (this.attributes.containsKey(name)) {
            this.attributes.put(name, this.attributes.get(name).concat(" ").concat(value));
        } else {
            this.attributes.put(name, value);
        }
        return this;
    }

    public int getWeight() {
        return this.weight;
    }

    public Element setWeight(int weight) {
        this.weight = weight;
        return this;
    }

    public boolean hasChildren() {
        return getChildren() != null && !getChildren().isEmpty();
    }

    public List<Element> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public Element setChildren(List<Element> children) {
        for (Element child : children) {
            child.parent = this;
        }
        this.children = children;
        return this;
    }

    public Element addChild(Element element) {
        element.parent = this;
        ElementEventGenerator.triggerBeforeInsert(this, element);
        this.children.add(element);
        ElementEventGenerator.triggerAfterInsert(this, element);
        return this;
    }

    public Element addChildren(Collection<? extends Element> elements) {
        for (Element element : elements) {
            addChild(element);
        }
        return this;
    }

    public boolean removeChild(Element element) {
        ElementEventGenerator.triggerBeforeRemove(this, element);
        boolean ret = this.children.remove(element);
        ElementEventGenerator.triggerAfterRemove(this, element);
        return ret;
    }

    public boolean hasBody() {
        return getBody() != null && !StringUtils.isBlank(getBody().body());
    }

    public Html getBody() {
        return body;
    }

    public Element setBody(String body) {
        return setBody(HtmlFormat.raw(body));
    }

    public Element setBody(Html body) {
        this.body = body;
        return this;
    }

    public boolean isAlwaysInHead() {
        return false;
    }

    public boolean isAlwaysInBody() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Element element = (Element) o;

        return weight == element.weight &&
                !(attributes != null ? !attributes.equals(element.attributes) : element.attributes != null) &&
                !(body != null ? !body.equals(element.body) : element.body != null) &&
                !(children != null ? !children.equals(element.children) : element.children != null) &&
                !(id != null ? !id.equals(element.id) : element.id != null) &&
                !(type != null ? !type.equals(element.type) : element.type != null);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (attributes != null ? attributes.size() : 0);
        result = 31 * result + weight;
        result = 31 * result + (children != null ? children.size() : 0);
        result = 31 * result + (body != null ? body.body().length() : 0);
        return result;
    }

    public Html decorate(RenderingContext renderingContext) {
        return HtmlFormat.raw("Element ["+getClass().getName()+"] is not correctly decorated. Please add a decorator.");
    }

    @Override
    public String toString() {
        return "Element{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", attributes=" + attributes +
                ", weight=" + weight +
                ", body=" + body +
                ", inputType=" + inputType +
                '}';
    }

}

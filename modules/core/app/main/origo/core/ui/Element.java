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

public class Element<T extends Element> {

    private static class Base<T extends Element> extends Element<T> {

        public String tagName;

        private Base(String tagName, String type) {
            super(type);
            this.tagName = tagName;
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            return base.render(tagName, (Element)this, ElementHelper.getHtmlFromBody(this), this.getAttributes());
        }

    }

    public static class Comment extends Element<Comment> {

        public Comment() {
            super("comment");
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            return Html.apply("<!-- ").$plus(getBody()).$plus(Html.apply(" -->"));
        }
    }

    public static class Raw extends Element<Raw> {

        public Raw() {
            super("raw");
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            return getBody();
        }
    }

    public static class Meta extends Element<Meta> {

        public Meta() {
            super("meta");
        }

        @Override
        public boolean isAlwaysInHead() {
            return true;
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            return meta.render(this, ElementHelper.getHtmlFromBody(this), getAttributes());
        }
    }

    public static class Script extends Element<Script> {

        public Script() {
            super("script");
        }

        @Override
        public boolean isAlwaysInHead() {
            return false;
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            return script.render(this, ElementHelper.getHtmlFromBody(this), this.getAttributes());
        }
    }

    public static class Style extends Element<Style> {

        public Style() {
            super("style");
        }

        @Override
        public boolean isAlwaysInHead() {
            return true;
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            return style.render(this, ElementHelper.getHtmlFromBody(this), this.getAttributes());
        }
    }

    public static class Link extends Element<Link> {

        public Link() {
            super("link");
        }

        @Override
        public boolean isAlwaysInHead() {
            return true;
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            return link.render(this, ElementHelper.getHtmlFromBody(this), this.getAttributes());
        }
    }

    public static class Image extends Base<Image> {

        public Image() {
            super("img", "image");
        }

    }

    public static class Help extends Base<Help> {

        public Help() {
            super("p", "help");
        }

    }

    public static class Span extends Base<Span> {

        public Span() {
            super("span", "span");
        }

    }

    public static class ListUnordered extends Element<ListUnordered> {

        public ListUnordered() {
            super("list_bulleted");
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            Html body = ThemeHelper.decorateChildren(this, decorationContext);
            return list.render("ul", this, body, this.getAttributes());
        }
    }

    public static class ListOrdered extends Element<ListOrdered> {

        public ListOrdered() {
            super("list_ordered");
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            Html body = ThemeHelper.decorateChildren(this, decorationContext);
            return list.render("ol", this, body, this.getAttributes());
        }
    }

    public static class ListItem extends Element<ListItem> {

        public ListItem() {
            super("list_item");
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            if (this.hasChildren()) {
                return list_item.render(this, ThemeHelper.decorateChildren(this, decorationContext), this.getAttributes());
            } else {
                return list_item.render(this, ElementHelper.getHtmlFromBody(this), this.getAttributes());
            }
        }
    }

    public static class Form extends Element<Form> {

        public Form() {
            super("form");
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            Html body = ThemeHelper.decorateChildren(this, decorationContext);
            return views.html.origo.core.decorators.forms.form.render(this, body, this.getAttributes());
        }
    }

    public static class FieldSet extends Element<FieldSet> {

        public FieldSet() {
            super("fieldset");
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            Html body = ThemeHelper.decorateChildren(this, decorationContext);
            return fieldset.render(this, body, this.getAttributes());
        }
    }

    public static class Field extends Element<Field> {

        public Field() {
            super("form_group");
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            Html body = ThemeHelper.decorateChildren(this, decorationContext);
            return field.render(this, body, this.getAttributes());
        }
    }

    public static class Legend extends Base<Legend> {
        public Legend() {
            super("legend", "legend");
        }
    }

    public static class Label extends Base<Label> {
        public Label() {
            super("label", "label");
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            if (this.hasChildren()) {
                return label.render(this, ThemeHelper.decorateChildren(this, decorationContext), this.getAttributes());
            } else {
                return label.render(this, ElementHelper.getHtmlFromBody(this), this.getAttributes());
            }
        }
    }

    public static class InputHidden extends Element<InputHidden> {

        public InputHidden() {
            super("input_hidden", String.class);
        }
        public InputHidden(Class c) {
            super("input_hidden", c);
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            Map<String, String> attributes = ElementHelper.combineAttributes(Collections.<String, String>singletonMap("type", "hidden"), this.getAttributes());
            return input.render(this, null, attributes);
        }
    }

    public static class InputText extends Element<InputText> {

        public InputText() {
            super("input_text", String.class);
        }
        public InputText(Class c) {
            super("input_text", c);
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            Map<String, String> attributes = ElementHelper.combineAttributes(Collections.<String, String>singletonMap("type", "text"), this.getAttributes());

            return input.render(this, null, attributes);
        }
    }

    public static class InputTextArea extends Element<InputTextArea> {

        public InputTextArea() {
            super("input_textarea", String.class);
        }
        public InputTextArea(Class c) {
            super("input_textarea", c);
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            if (this.hasChildren()) {
                return textarea.render(this, ThemeHelper.decorateChildren(this, decorationContext), this.getAttributes());
            } else {
                return textarea.render(this, ElementHelper.getHtmlFromBody(this), this.getAttributes());
            }
        }
    }

    public static class InputRadioButton extends Element<InputRadioButton> {

        public InputRadioButton() {
            super("input_radiobutton", String.class);
        }
        public InputRadioButton(Class c) {
            super("input_radiobutton", c);
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            Map<String, String> attributes = ElementHelper.combineAttributes(Collections.<String, String>singletonMap("type", "radio"), this.getAttributes());
            return input.render(this, null, attributes);
        }
    }

    public static class InputCheckbox extends Element<InputCheckbox> {

        public InputCheckbox() {
            super("input_checkbox", String.class);
        }
        public InputCheckbox(Class c) {
            super("input_checkbox", c);
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            Map<String, String> attributes = ElementHelper.combineAttributes(Collections.<String, String>singletonMap("type", "checkbox"), this.getAttributes());
            return input.render(this, null, attributes);
        }
    }

    public static class InputSelect extends Element<InputSelect> {

        public InputSelect() {
            super("input_select", String.class);
        }
        public InputSelect(Class c) {
            super("input_select", c);
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            Html body = ThemeHelper.decorateChildren(this, decorationContext);
            return select.render(this, body, this.getAttributes());
        }
    }

    public static class InputSelectOption extends Element<InputSelectOption> {

        public InputSelectOption() {
            super("input_select_option", String.class);
        }

        public InputSelectOption(Class c) {
            super("input_select_option", c);
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            if (this.hasChildren()) {
                return select_option.render(this, ThemeHelper.decorateChildren(this, decorationContext), this.getAttributes());
            } else {
                return select_option.render(this, ElementHelper.getHtmlFromBody(this), this.getAttributes());
            }
        }
    }

    public static class Button extends Element<Button> {

        public Button() {
            super("button");
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            Map<String, String> combinedAttributes = ElementHelper.combineAttributes(Collections.singletonMap("type", "button"), this.getAttributes());
            Html body = ThemeHelper.decorateChildren(this, decorationContext);
            return button.render(this, body, attributes);
        }
    }

    public static class SubmitButton extends Element<SubmitButton> {

        public SubmitButton() {
            super("submit_button");
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            Map<String, String> combinedAttributes = ElementHelper.combineAttributes(Collections.singletonMap("type", "submit"), this.getAttributes());
            Html body = ThemeHelper.decorateChildren(this, decorationContext);
            return button.render(this, body, attributes);
        }
    }

    public static class ResetButton extends Element<ResetButton> {

        public ResetButton() {
            super("reset_button");
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            Map<String, String> combinedAttributes = ElementHelper.combineAttributes(Collections.singletonMap("type", "reset"), this.getAttributes());
            Html body = ThemeHelper.decorateChildren(this, decorationContext);
            return button.render(this, body, attributes);
        }
    }

    public static class ButtonGroup extends Element.Container {

        public enum Type {
            NORMAL, VERTICAL, JUSTIFIED
        }

        private Type type;

        public ButtonGroup() {
            this(Type.NORMAL);
        }

        public ButtonGroup(Type type) {
            this.type = type;
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            switch(type) {
                case NORMAL:
                    addAttribute("class", "btn-group");
                    break;
                case JUSTIFIED:
                    addAttribute("class", "btn-group btn-group-justified");
                    break;
                case VERTICAL:
                    addAttribute("class", "btn-group-vertical");
                    break;
            }
            return super.decorate(decorationContext);
        }
    }

    public static class DropDownButton extends Element.Container {

        private Button button;

        public DropDownButton(Button button) {
            super();
            this.button = button;

        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            button.addAttribute("class", "dropdown-toggle");
            button.addAttribute("data-toggle", "dropdown");

            ButtonGroup btnGroup = ElementHelper.copyBasicAttributes(this, ButtonGroup.class);
            ListUnordered list = new ListUnordered().
                    addAttribute("class", "dropdown-menu").addAttribute("role", "menu");
            for (Element element : getChildren()) {
                if (element instanceof Divider) {
                    list.addChild(new ListItem().addAttribute("class", "divider"));
                } else {
                    list.addChild(new ListItem().addChild(element));
                }
            }
            btnGroup.addChild(button).addChild(list);

            return btnGroup.decorate(decorationContext);
        }
    }

    public static class InputImage extends Element<InputImage> {

        public InputImage() {
            super("input_image", String.class);
        }
        public InputImage(Class c) {
            super("input_image", c);
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            Map<String, String> attributes = ElementHelper.combineAttributes(Collections.<String, String>singletonMap("type", "image"), this.getAttributes());
            return input.render(this, null, attributes);
        }
    }

    public static class InputFile extends Element<InputFile> {

        public InputFile() {
            super("input_file", String.class);
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            Map<String, String> attributes = ElementHelper.combineAttributes(Collections.<String, String>singletonMap("type", "file"), this.getAttributes());
            return input.render(this, null, attributes);
        }
    }

    public static class InputPassword extends Element<InputPassword> {

        public InputPassword() {
            super("input_password", String.class);
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            Map<String, String> attributes = ElementHelper.combineAttributes(Collections.<String, String>singletonMap("type", "password"), this.getAttributes());
            return input.render(this, null, attributes);
        }
    }

    public static class Divider extends Base<Divider> {
        public Divider() {
            super("hr", "divider");
        }
    }

    public static class Container extends Element<Container> {

        public Container() {
            super("container");
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            Html body = ThemeHelper.decorateChildren(this, decorationContext);
            return container.render(this, body, this.getAttributes());
        }
    }

    public static class Well extends Element<Well> {
        public static enum Size {

            SMALL("well well-sm"), NORMAL("well"), LARGE("well-lg");

            private String classes;
            Size(String classes) {
                this.classes = classes;
            }
        }

        private Size size;

        public Well() {
            super("well");
            size = Size.NORMAL;
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            Html body = ThemeHelper.decorateChildren(this, decorationContext);
            addAttribute("class", this.size.classes);

            return container.render(this, body, this.getAttributes());
        }
    }

    public static class Panel extends Element<Panel> {

        private Element header;
        private Element footer;

        public Panel() {
            super("panel");
        }

        public Panel(Element header) {
            super("panel");
            this.header = header;
        }

        public Panel(Element header, Element footer) {
            super("panel");
            this.header = header;
            this.footer = footer;
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            addAttribute("class", "panel panel-default");
            Container panel = ElementHelper.copyBasicAttributes(this, Container.class);
            if (header != null) {
                panel.addChild(
                        new Container().addAttribute("class", "panel-heading").
                                addChild(header.addAttribute("class", "panel-title"))
                );
            }
            panel.addChild(
                    new Container().addAttribute("class", "panel-body").
                            addChildren(getChildren()));
            if (footer != null) {
                panel.addChild(
                        new Container().addAttribute("class", "panel-footer").
                                addChild(footer)
                );
            }

            Html body = ThemeHelper.decorateChildren(panel, decorationContext);
            return container.render(panel, body, panel.getAttributes());
        }
    }

    public static class Paragraph extends Element<Paragraph> {

        public Paragraph() {
            super("paragaph");
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            Html body = ThemeHelper.decorateChildren(this, decorationContext);
            return paragraph.render(this, body, this.getAttributes());
        }
    }

    public static class Emphasis extends Base {

        public Type type;

        public static enum Type {
            // Just css (P tag)
            PRIMARY, SUCCESS, INFO, MUTED, WARNING, ERROR,
            // Different tags (em, bold, small)
            ITALICS, BOLD, SMALL,
        }

        public Emphasis() {
            super("em", "emphasis");
            this.type = Type.ITALICS;
        }

        public Emphasis(Type type) {
            super("p", "emphasis");
            switch (type) {
                case ITALICS:
                    super.type = "em";
                    break;
                case BOLD:
                    super.type = "bold";
                    break;
                case SMALL:
                    super.type = "small";
                    break;
            }
            this.type = type;
        }

    }

    public static class Alert extends Element {

        public Type type;

        public static enum Type {
            SUCCESS, INFO, WARNING, ERROR
        }

        public Alert() {
            this(Type.SUCCESS);
        }

        public Alert(Type type) {
            super("alert");
            this.type = type;
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            Html body = ThemeHelper.decorateChildren(this, decorationContext);
            return container.render(this, body, this.getAttributes());
        }
    }

    public static class Text extends Element<Text> {

        public Text() {
            super("text");
        }
        public Text(Class c) {
            super("text", c);
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            return text.render(this, ElementHelper.getHtmlFromBody(this), this.getAttributes());
        }
    }

    public static class Anchor extends Element<Anchor> {

        public Anchor() {
            super("anchor");
        }

        @Override
        public Html decorate(DecorationContext decorationContext) {
            Html body = ElementHelper.getHtmlFromBody(this);
            if (this.hasChildren()) {
                body = ThemeHelper.decorateChildren(this, decorationContext);
            }
            return anchor.render(this, body, this.getAttributes());
        }
    }

    public static class AnchorButton extends Anchor {

        public AnchorButton() {
            super();
        }

    }

    private static class Heading<T extends Element> extends Base<T> {

        private Heading(String tagName, String type) {
            super(tagName, type);
        }

    }

    public static class Heading1 extends Heading<Heading1> {

        public Heading1() {
            super("h1", "heading_1");
        }
    }

    public static class Heading2 extends Heading<Heading2> {

        public Heading2() {
            super("h2", "heading_2");
        }
    }

    public static class Heading3 extends Heading<Heading3> {

        public Heading3() {
            super("h3", "heading_3");
        }
    }

    public static class Heading4 extends Heading<Heading4> {

        public Heading4() {
            super("h4", "heading_4");
        }
    }

    public static class Heading5 extends Heading<Heading4> {

        public Heading5() {
            super("h5", "heading_5");
        }
    }

    public static class Heading6 extends Heading<Heading5> {

        public Heading6() {
            super("h6", "heading_6");
        }
    }

    public static class Strong extends Base<Strong> {
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

    protected <T> Element(String type) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
    }

    protected <T> Element(String type, Class t) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.inputType = t;
    }

    public String getId() {
        return id;
    }

    public T setId(String id) {
        this.id = id;
        return (T) this;
    }

    public String getType() {
        return type;
    }

    public Element parent() {
        return parent;
    }

    public boolean is(String type) {
        return getType().equalsIgnoreCase(type);
    }

    public boolean is(Class<? extends Element> elementType) {
        try {
            return is(elementType.newInstance().type);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Unable to instantiate instance: "+elementType.getName(), e);
        }
    }

    public Class getInputType() {
        return inputType;
    }

    public boolean hasAttributes() {
        return getAttributes() != null && !getAttributes().isEmpty();
    }

    public Map<String, String> getAttributes() {
        return Collections.<String, String>unmodifiableMap(attributes);
    }

    public T setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
        return (T) this;
    }

    public T addAttribute(String name, String value) {
        if (this.attributes.containsKey(name)) {
            this.attributes.put(name, this.attributes.get(name).concat(" ").concat(value));
        } else {
            this.attributes.put(name, value);
        }
        return (T) this;
    }

    public int getWeight() {
        return this.weight;
    }

    public T setWeight(int weight) {
        this.weight = weight;
        return (T) this;
    }

    public boolean hasChildren() {
        return getChildren() != null && !getChildren().isEmpty();
    }

    public List<Element> getChildren() {
        return children;
    }

    public T setChildren(List<Element> children) {
        for (Element child : children) {
            child.parent = this;
        }
        this.children = children;
        return (T) this;
    }

    public T addChild(Element element) {
        element.parent = this;
        ElementEventGenerator.triggerBeforeInsert(this, element);
        this.children.add(element);
        ElementHelper.reorderElements(this.children);
        ElementEventGenerator.triggerAfterInsert(this, element);
        return (T) this;
    }

    public T addChildren(Collection<? extends Element> elements) {
        for (Element element : elements) {
            addChild(element);
        }
        ElementHelper.reorderElements(this.children);
        return (T) this;
    }

    public T addChildren(Element... elements) {
        this.addChildren(Arrays.<Element>asList(elements));
        return (T)this;
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

    public T setBody(String body) {
        return setBody(HtmlFormat.raw(body));
    }

    public T setBody(Html body) {
        this.body = body;
        return (T) this;
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

    public Html decorate(DecorationContext decorationContext) {
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

package main.origo.core.ui;

import com.google.common.collect.Lists;
import main.origo.core.helpers.ElementHelper;
import main.origo.core.helpers.ThemeHelper;
import org.apache.commons.lang3.StringUtils;
import play.api.templates.Html;
import play.api.templates.HtmlFormat;
import views.html.origo.core.decorators.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class Element {

    public static class Raw extends Element {
        public static final String TYPE = "raw";

        public Raw() {
            super(TYPE);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            return getBody();
        }
    }

    public static class Meta extends Element {
        public static final String TYPE = "meta";

        public Meta() {
            super(TYPE);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            //noinspection unchecked
            return meta.render(this, ElementHelper.getHtmlFromBody(this), getAttributes());
        }
    }

    public static class Script extends Element {
        public static final String TYPE = "script";

        public Script() {
            super(TYPE);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            //noinspection unchecked
            return script.render(this, ElementHelper.getHtmlFromBody(this), this.getAttributes());
        }
    }

    public static class Style extends Element {
        public static final String TYPE = "style";

        public Style() {
            super(TYPE);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            //noinspection unchecked
            return style.render(this, ElementHelper.getHtmlFromBody(this), this.getAttributes());
        }
    }

    public static class Link extends Element {
        public static final String TYPE = "link";

        public Link() {
            super(TYPE);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            //noinspection unchecked
            return link.render(this, ElementHelper.getHtmlFromBody(this), this.getAttributes());
        }
    }

    public static class ListBulleted extends Element {
        public static final String TYPE = "list_bulleted";

        public ListBulleted() {
            super(TYPE);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Html body = ThemeHelper.decorateChildren(this, renderingContext);
            //noinspection unchecked
            return list.render("ul", this, body, this.getAttributes());
        }
    }

    public static class ListOrdered extends Element {
        public static final String TYPE = "list_ordered";

        public ListOrdered() {
            super(TYPE);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Html body = ThemeHelper.decorateChildren(this, renderingContext);
            //noinspection unchecked
            return list.render("ol", this, body, this.getAttributes());
        }
    }

    public static class ListItem extends Element {
        public static final String TYPE = "list_item";

        public ListItem() {
            super(TYPE);
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
        public static final String TYPE = "form";

        public Form() {
            super(TYPE);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Html body = ThemeHelper.decorateChildren(this, renderingContext);
            //noinspection unchecked
            return form.render(this, body, this.getAttributes());
        }
    }

    public static class Legend extends Element {
        public static final String TYPE = "legend";

        public Legend() {
            super(TYPE);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Html body = ThemeHelper.decorateChildren(this, renderingContext);
            //noinspection unchecked
            return legend.render(this, body, this.getAttributes());
        }
    }

    public static class Label extends Element {
        public static final String TYPE = "label";

        public Label() {
            super(TYPE);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            //noinspection unchecked
            return label.render(this, ElementHelper.getHtmlFromBody(this), this.getAttributes());
        }
    }

    public static class InputHidden extends Element {
        public static final String TYPE = "input_hidden";

        public InputHidden() {
            super(TYPE, String.class);
        }
        public InputHidden(Class c) {
            super(TYPE, c);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Map<String, String> attributes = ElementHelper.combineAttributes(Collections.<String, String>singletonMap("type", "hidden"), this.getAttributes());
            //noinspection unchecked
            return input.render(this, null, attributes);
        }
    }

    public static class InputText extends Element {
        public static final String TYPE = "input_text";

        public InputText() {
            super(TYPE, String.class);
        }
        public InputText(Class c) {
            super(TYPE, c);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Map<String, String> attributes = ElementHelper.combineAttributes(Collections.<String, String>singletonMap("type", "text"), this.getAttributes());
            //noinspection unchecked
            return input.render(this, null, attributes);
        }
    }

    public static class InputTextArea extends Element {
        public static final String TYPE = "input_textarea";

        public InputTextArea() {
            super(TYPE, String.class);
        }
        public InputTextArea(Class c) {
            super(TYPE, c);
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
        public static final String TYPE = "input_radiobutton";

        public InputRadioButton() {
            super(TYPE, String.class);
        }
        public InputRadioButton(Class c) {
            super(TYPE, c);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Map<String, String> attributes = ElementHelper.combineAttributes(Collections.<String, String>singletonMap("type", "radiobutton"), this.getAttributes());
            //noinspection unchecked
            return input.render(this, null, attributes);
        }
    }

    public static class InputSelect extends Element {
        public static final String TYPE = "input_select";

        public InputSelect() {
            super(TYPE, String.class);
        }
        public InputSelect(Class c) {
            super(TYPE, c);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Html body = ThemeHelper.decorateChildren(this, renderingContext);
            //noinspection unchecked
            return select.render(this, body, this.getAttributes());
        }
    }

    public static class InputSelectOption extends Element {
        public static final String TYPE = "input_select_option";

        public InputSelectOption() {
            super(TYPE, String.class);
        }

        public InputSelectOption(Class c) {
            super(TYPE, c);
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
        public static final String TYPE = "input_button";

        public InputButton() {
            super(TYPE);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Map<String, String> attributes = ElementHelper.combineAttributes(Collections.<String, String>singletonMap("type", "button"), this.getAttributes());
            //noinspection unchecked
            return input.render(this, null, attributes);
        }
    }

    public static class InputSubmit extends Element {
        public static final String TYPE = "input_submit";

        public InputSubmit() {
            super(TYPE);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Map<String, String> attributes = ElementHelper.combineAttributes(Collections.<String, String>singletonMap("type", "submit"), this.getAttributes());
            //noinspection unchecked
            return input.render(this, null, attributes);
        }
    }

    public static class InputReset extends Element {
        public static final String TYPE = "input_reset";

        public InputReset() {
            super(TYPE);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Map<String, String> attributes = ElementHelper.combineAttributes(Collections.<String, String>singletonMap("type", "reset"), this.getAttributes());
            //noinspection unchecked
            return input.render(this, null, attributes);
        }
    }

    public static class InputImage extends Element {
        public static final String TYPE = "input_image";

        public InputImage() {
            super(TYPE, String.class);
        }
        public InputImage(Class c) {
            super(TYPE, c);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Map<String, String> attributes = ElementHelper.combineAttributes(Collections.<String, String>singletonMap("type", "image"), this.getAttributes());
            //noinspection unchecked
            return input.render(this, null, attributes);
        }
    }

    public static class InputFile extends Element {
        public static final String TYPE = "input_file";

        public InputFile() {
            super(TYPE, String.class);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Map<String, String> attributes = ElementHelper.combineAttributes(Collections.<String, String>singletonMap("type", "file"), this.getAttributes());
            //noinspection unchecked
            return input.render(this, null, attributes);
        }
    }

    public static class InputPassword extends Element {
        public static final String TYPE = "input_password";

        public InputPassword() {
            super(TYPE, String.class);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Map<String, String> attributes = ElementHelper.combineAttributes(Collections.<String, String>singletonMap("type", "password"), this.getAttributes());
            //noinspection unchecked
            return input.render(this, null, attributes);
        }
    }

    public static class Panel extends Element {
        public static final String TYPE = "panel";

        public Panel() {
            super(TYPE);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            Html body = ThemeHelper.decorateChildren(this, renderingContext);
            return panel.render(this, body, this.getAttributes());
        }
    }

    public static class Paragraph extends Element {
        public static final String TYPE = "paragaph";

        public Paragraph() {
            super(TYPE);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            //noinspection unchecked
            return paragraph.render(this, ElementHelper.getHtmlFromBody(this), this.getAttributes());
        }
    }

    public static class Text extends Element {
        public static final String TYPE = "text";

        public Text() {
            super(TYPE);
        }
        public Text(Class c) {
            super(TYPE, c);
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            //noinspection unchecked
            return text.render(this, ElementHelper.getHtmlFromBody(this), this.getAttributes());
        }
    }

    public static class Anchor extends Element {
        public static final String TYPE = "anchor";

        public Anchor() {
            super(TYPE);
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

    private static class H extends Element {
        private String size;

        private H(String type, String size) {
            super(type);
            this.size = size;
        }

        @Override
        public Html decorate(RenderingContext renderingContext) {
            //noinspection unchecked
            return heading.render(this, size, ElementHelper.getHtmlFromBody(this), this.getAttributes());
        }
    }

    public static class H1 extends H {
        public static final String TYPE = "heading_1";

        public H1() {
            //noinspection unchecked
            super(TYPE, "1");
        }
    }

    public static class H2 extends H {
        public static final String TYPE = "heading_2";

        public H2() {
            //noinspection unchecked
            super(TYPE, "2");
        }
    }

    public static class H3 extends H {
        public static final String TYPE = "heading_3";

        public H3() {
            //noinspection unchecked
            super(TYPE, "3");
        }
    }

    public static class H4 extends H {
        public static final String TYPE = "heading_4";

        public H4() {
            //noinspection unchecked
            super(TYPE, "4");
        }
    }

    public static class H5 extends H {
        public static final String TYPE = "heading_5";

        public H5() {
            //noinspection unchecked
            super(TYPE, "5");
        }
    }

    public static class H6 extends H {
        public static final String TYPE = "heading_6";

        public H6() {
            //noinspection unchecked
            super(TYPE, "5");
        }
    }



    public String id;

    public String type;

    public Map<String, String> attributes = new WeakHashMap<>();

    private int weight;

    private List<Element> children = Lists.newArrayList();

    private Html body;

    private Class inputType;

    protected Element(String type) {
        this.type = type;
    }

    protected Element(String type, Class t) {
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
        this.children = children;
        return this;
    }

    public Element addChild(Element element) {
        ElementHelper.triggerBeforeInsert(this, element);
        this.children.add(element);
        ElementHelper.triggerAfterInsert(this, element);
        return this;
    }

    public boolean removeChild(Element element) {
        ElementHelper.triggerBeforeRemove(this, element);
        boolean ret = this.children.remove(element);
        ElementHelper.triggerAfterRemove(this, element);
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
        result = 31 * result + (body != null ? body.hashCode() : 0);
        return result;
    }

    public Html decorate(RenderingContext renderingContext) {
        return new Html("Element ["+getClass().getName()+"] is not correctly decorated. Please add a decorator.");
    }
}

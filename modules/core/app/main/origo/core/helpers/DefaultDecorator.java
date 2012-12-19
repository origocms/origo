package main.origo.core.helpers;

import com.google.common.collect.Maps;
import main.origo.core.ui.RenderingContext;
import main.origo.core.ui.UIElement;
import play.api.templates.Html;
import play.api.templates.HtmlFormat;
import views.html.origo.core.decorators.*;

import java.util.Collections;
import java.util.Map;

public class DefaultDecorator {

    public static Html decorate(UIElement uiElement, RenderingContext renderingContext) {
        switch (uiElement.getType()) {
            case UIElement.META:
                return decorateMeta(uiElement, renderingContext);
            case UIElement.SCRIPT:
                return decorateScript(uiElement, renderingContext);
            case UIElement.STYLE:
                return decorateStyle(uiElement, renderingContext);
            case UIElement.LINK:
                return decorateLink(uiElement, renderingContext);
            case UIElement.FORM:
                return decorateForm(uiElement, renderingContext);
            case UIElement.INPUT_TEXT:
                return decorateInputText(uiElement, renderingContext);
            case UIElement.LABEL:
                return decorateLabel(uiElement, renderingContext);
            case UIElement.INPUT_TEXTAREA:
                return decorateInputTextArea(uiElement, renderingContext);
            case UIElement.INPUT_HIDDEN:
                return decorateInputHidden(uiElement, renderingContext);
            case UIElement.INPUT_PASSWORD:
                return decorateInputPassword(uiElement, renderingContext);
            case UIElement.INPUT_FILE:
                return decorateInputFile(uiElement, renderingContext);
            case UIElement.INPUT_IMAGE:
                return decorateInputImage(uiElement, renderingContext);
            case UIElement.INPUT_RADIO_BUTTON:
                return decorateInputRadioButton(uiElement, renderingContext);
            case UIElement.INPUT_SELECT:
                return decorateInputSelect(uiElement, renderingContext);
            case UIElement.INPUT_SELECT_OPTION:
                return decorateInputSelectOption(uiElement, renderingContext);
            case UIElement.INPUT_BUTTON:
                return decorateInputButton(uiElement, renderingContext);
            case UIElement.INPUT_SUBMIT:
                return decorateInputSubmit(uiElement, renderingContext);
            case UIElement.INPUT_RESET:
                return decorateInputReset(uiElement, renderingContext);
            case UIElement.LIST_BULLET:
                return decorateListBulleted(uiElement, renderingContext);
            case UIElement.LIST_ORDERED:
                return decorateListNumbered(uiElement, renderingContext);
            case UIElement.LIST_ITEM:
                return decorateListItem(uiElement, renderingContext);
            case UIElement.ANCHOR:
                return decorateAnchor(uiElement, renderingContext);
            case UIElement.PANEL:
                return decoratePanel(uiElement, renderingContext);
            case UIElement.TEXT:
                return decorateText(uiElement, renderingContext);
            case UIElement.PARAGRAPH:
                return decorateParagraph(uiElement, renderingContext);
            default:
                throw new RuntimeException("Unable to decorate unknown type '" + uiElement.getType() + "'");
        }
    }

    public static Html decorateMeta(UIElement uiElement, RenderingContext renderingContext) {
        return meta.render(uiElement, getHtmlFromBody(uiElement), uiElement.getAttributes());
    }

    public static Html decorateLink(UIElement uiElement, RenderingContext renderingContext) {
        return link.render(uiElement, getHtmlFromBody(uiElement), uiElement.getAttributes());
    }

    public static Html decorateStyle(UIElement uiElement, RenderingContext renderingContext) {
        return style.render(uiElement, getHtmlFromBody(uiElement), uiElement.getAttributes());
    }

    public static Html decorateScript(UIElement uiElement, RenderingContext renderingContext) {
        return script.render(uiElement, getHtmlFromBody(uiElement), uiElement.getAttributes());
    }

    public static Html decorateForm(UIElement uiElement, RenderingContext renderingContext) {
        Html body = ThemeHelper.decorateChildren(uiElement, renderingContext);
        return form.render(uiElement, body, uiElement.getAttributes());
    }

    public static Html decorateListBulleted(UIElement uiElement, RenderingContext renderingContext) {
        Html body = ThemeHelper.decorateChildren(uiElement, renderingContext);
        return list.render("ul", uiElement, body, uiElement.getAttributes());
    }

    public static Html decorateListNumbered(UIElement uiElement, RenderingContext renderingContext) {
        Html body = ThemeHelper.decorateChildren(uiElement, renderingContext);
        return list.render("ol", uiElement, body, uiElement.getAttributes());
    }

    public static Html decorateListItem(UIElement uiElement, RenderingContext renderingContext) {
        Html body = getHtmlFromBody(uiElement);
        if (uiElement.hasChildren()) {
            body = ThemeHelper.decorateChildren(uiElement, renderingContext);
        }
        return list_item.render(uiElement, body, uiElement.getAttributes());
    }

    public static Html decorateInputText(UIElement uiElement, RenderingContext renderingContext) {
        Map<String, String> attributes = combineAttributes(Collections.<String, String>singletonMap("type", "text"), uiElement.getAttributes());
        return input.render(uiElement, null, attributes);
    }

    public static Html decorateInputHidden(UIElement uiElement, RenderingContext renderingContext) {
        Map<String, String> attributes = combineAttributes(Collections.<String, String>singletonMap("type", "hidden"), uiElement.getAttributes());
        return input.render(uiElement, null, attributes);
    }

    public static Html decorateLabel(UIElement uiElement, RenderingContext renderingContext) {
        return label.render(uiElement, getHtmlFromBody(uiElement), uiElement.getAttributes());
    }

    public static Html decorateInputTextArea(UIElement uiElement, RenderingContext renderingContext) {
        Html body = getHtmlFromBody(uiElement);
        if (uiElement.hasChildren()) {
            body.$plus(ThemeHelper.decorateChildren(uiElement, renderingContext));
        }
        return textarea.render(uiElement, body, uiElement.getAttributes());
    }

    public static Html decorateInputRadioButton(UIElement uiElement, RenderingContext renderingContext) {
        Map<String, String> attributes = combineAttributes(Collections.<String, String>singletonMap("type", "radiobutton"), uiElement.getAttributes());
        return input.render(uiElement, null, attributes);
    }

    public static Html decorateInputSelect(UIElement uiElement, RenderingContext renderingContext) {
        Html body = ThemeHelper.decorateChildren(uiElement, renderingContext);
        return select.render(uiElement, body, uiElement.getAttributes());
    }

    public static Html decorateInputSelectOption(UIElement uiElement, RenderingContext renderingContext) {
        Html body = getHtmlFromBody(uiElement);
        if (uiElement.hasChildren()) {
            body.$plus(ThemeHelper.decorateChildren(uiElement, renderingContext));
        }
        return select_option.render(uiElement, body, uiElement.getAttributes());
    }

    public static Html decorateInputButton(UIElement uiElement, RenderingContext renderingContext) {
        Map<String, String> attributes = combineAttributes(Collections.<String, String>singletonMap("type", "button"), uiElement.getAttributes());
        return input.render(uiElement, null, attributes);
    }

    public static Html decorateInputSubmit(UIElement uiElement, RenderingContext renderingContext) {
        Map<String, String> attributes = combineAttributes(Collections.<String, String>singletonMap("type", "submit"), uiElement.getAttributes());
        return input.render(uiElement, null, attributes);
    }

    public static Html decorateInputReset(UIElement uiElement, RenderingContext renderingContext) {
        Map<String, String> attributes = combineAttributes(Collections.<String, String>singletonMap("type", "reset"), uiElement.getAttributes());
        return input.render(uiElement, null, attributes);
    }

    public static Html decorateInputImage(UIElement uiElement, RenderingContext renderingContext) {
        Map<String, String> attributes = combineAttributes(Collections.<String, String>singletonMap("type", "image"), uiElement.getAttributes());
        return input.render(uiElement, null, attributes);
    }

    public static Html decorateInputFile(UIElement uiElement, RenderingContext renderingContext) {
        Map<String, String> attributes = combineAttributes(Collections.<String, String>singletonMap("type", "file"), uiElement.getAttributes());
        return input.render(uiElement, null, attributes);
    }

    public static Html decorateInputPassword(UIElement uiElement, RenderingContext renderingContext) {
        Map<String, String> attributes = combineAttributes(Collections.<String, String>singletonMap("type", "password"), uiElement.getAttributes());
        return input.render(uiElement, null, attributes);
    }

    public static Html decoratePanel(UIElement uiElement, RenderingContext renderingContext) {
        Html body = ThemeHelper.decorateChildren(uiElement, renderingContext);
        return panel.render(uiElement, body, uiElement.getAttributes());
    }

    public static Html decorateText(UIElement uiElement, RenderingContext renderingContext) {
        return text.render(uiElement, getHtmlFromBody(uiElement), uiElement.getAttributes());
    }

    public static Html decorateParagraph(UIElement uiElement, RenderingContext renderingContext) {
        return paragraph.render(uiElement, getHtmlFromBody(uiElement), uiElement.getAttributes());
    }

    public static Html decorateAnchor(UIElement uiElement, RenderingContext renderingContext) {
        Html body = getHtmlFromBody(uiElement);
        if (uiElement.hasChildren()) {
            body = ThemeHelper.decorateChildren(uiElement, renderingContext);
        }
        return anchor.render(uiElement, body, uiElement.getAttributes());
    }

    private static Html getHtmlFromBody(UIElement uiElement) {
        if (uiElement.hasBody()) {
            return HtmlFormat.raw(uiElement.getBody());
        } else {
            return Html.empty();
        }
    }

    public static Map<String, String> combineAttributes(Map<String, String> map1, Map<String, String> map2) {
        Map<String, String> map = Maps.newHashMap(map1);
        map.putAll(map2);
        return map;
    }
}

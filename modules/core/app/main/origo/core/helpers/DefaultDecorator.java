package main.origo.core.helpers;

import main.origo.core.ui.RenderingContext;
import main.origo.core.ui.UIElement;
import play.api.templates.Html;
import views.html.origo.core.fragments.*;

import java.util.Collections;

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
        return meta.render(uiElement, uiElement.getBody(), Collections.<String, String>emptyMap());
    }

    public static Html decorateLink(UIElement uiElement, RenderingContext renderingContext) {
        return link.render(uiElement, uiElement.getBody(), Collections.<String, String>emptyMap());
    }

    public static Html decorateStyle(UIElement uiElement, RenderingContext renderingContext) {
        return style.render(uiElement, uiElement.getBody(), Collections.<String, String>emptyMap());
    }

    public static Html decorateScript(UIElement uiElement, RenderingContext renderingContext) {
        return script.render(uiElement, uiElement.getBody(), Collections.<String, String>emptyMap());
    }

    public static Html decorateForm(UIElement uiElement, RenderingContext renderingContext) {
        String body = ThemeHelper.decorateChildren(uiElement, renderingContext);
        return form.render(uiElement, body, Collections.<String, String>emptyMap());
    }

    public static Html decorateListBulleted(UIElement uiElement, RenderingContext renderingContext) {
        String body = ThemeHelper.decorateChildren(uiElement, renderingContext);
        return list.render("ul", uiElement, body, Collections.<String, String>emptyMap());
    }

    public static Html decorateListNumbered(UIElement uiElement, RenderingContext renderingContext) {
        String body = ThemeHelper.decorateChildren(uiElement, renderingContext);
        return list.render("ol", uiElement, body, Collections.<String, String>emptyMap());
    }

    public static Html decorateListItem(UIElement uiElement, RenderingContext renderingContext) {
        String body = uiElement.getBody();
        if (uiElement.hasChildren()) {
            body = ThemeHelper.decorateChildren(uiElement, renderingContext);
        }
        return list_item.render(uiElement, body, Collections.<String, String>emptyMap());
    }

    public static Html decorateInputText(UIElement uiElement, RenderingContext renderingContext) {
        return input.render(uiElement, null, Collections.singletonMap("type", "text"));
    }

    public static Html decorateInputHidden(UIElement uiElement, RenderingContext renderingContext) {
        return input.render(uiElement, null, Collections.singletonMap("type", "hidden"));
    }

    public static Html decorateLabel(UIElement uiElement, RenderingContext renderingContext) {
        return label.render(uiElement, uiElement.getBody(), Collections.<String, String>emptyMap());
    }

    public static Html decorateInputTextArea(UIElement uiElement, RenderingContext renderingContext) {
        String body = uiElement.getBody();
        if (uiElement.hasChildren()) {
            body = ThemeHelper.decorateChildren(uiElement, renderingContext);
        }
        return textarea.render(uiElement, body, Collections.<String, String>emptyMap());
    }

    public static Html decorateInputRadioButton(UIElement uiElement, RenderingContext renderingContext) {
        return input.render(uiElement, null, Collections.singletonMap("type", "radiobutton"));
    }

    public static Html decorateInputSelect(UIElement uiElement, RenderingContext renderingContext) {
        String body = ThemeHelper.decorateChildren(uiElement, renderingContext);
        return select.render(uiElement, body, Collections.<String, String>emptyMap());
    }

    public static Html decorateInputSelectOption(UIElement uiElement, RenderingContext renderingContext) {
        String body = uiElement.getBody();
        if (uiElement.hasChildren()) {
            body = ThemeHelper.decorateChildren(uiElement, renderingContext);
        }
        return select_option.render(uiElement, body, Collections.<String, String>emptyMap());
    }

    public static Html decorateInputButton(UIElement uiElement, RenderingContext renderingContext) {
        return input.render(uiElement, null, Collections.singletonMap("type", "button"));
    }

    public static Html decorateInputSubmit(UIElement uiElement, RenderingContext renderingContext) {
        return input.render(uiElement, null, Collections.singletonMap("type", "submit"));
    }

    public static Html decorateInputReset(UIElement uiElement, RenderingContext renderingContext) {
        return input.render(uiElement, null, Collections.singletonMap("type", "reset"));
    }

    public static Html decorateInputImage(UIElement uiElement, RenderingContext renderingContext) {
        return input.render(uiElement, null, Collections.singletonMap("type", "image"));
    }

    public static Html decorateInputFile(UIElement uiElement, RenderingContext renderingContext) {
        return input.render(uiElement, null, Collections.singletonMap("type", "file"));
    }

    public static Html decorateInputPassword(UIElement uiElement, RenderingContext renderingContext) {
        return input.render(uiElement, null, Collections.singletonMap("type", "password"));
    }

    public static Html decoratePanel(UIElement uiElement, RenderingContext renderingContext) {
        String body = ThemeHelper.decorateChildren(uiElement, renderingContext);
        return panel.render(uiElement, body, Collections.<String, String>emptyMap());
    }

    public static Html decorateText(UIElement uiElement, RenderingContext renderingContext) {
        return text.render(uiElement, uiElement.getBody(), Collections.<String, String>emptyMap());
    }

    public static Html decorateParagraph(UIElement uiElement, RenderingContext renderingContext) {
        return paragraph.render(uiElement, uiElement.getBody(), Collections.<String, String>emptyMap());
    }

    public static Html decorateAnchor(UIElement uiElement, RenderingContext renderingContext) {
        String body = uiElement.getBody();
        if (uiElement.hasChildren()) {
            body = ThemeHelper.decorateChildren(uiElement, renderingContext);
        }
        return anchor.render(uiElement, body, Collections.<String, String>emptyMap());
    }


}

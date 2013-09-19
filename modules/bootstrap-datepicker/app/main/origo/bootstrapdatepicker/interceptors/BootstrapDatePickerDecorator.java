package main.origo.bootstrapdatepicker.interceptors;

import controllers.routes;
import main.origo.core.Node;
import main.origo.core.annotations.Decorates;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnInsertElement;
import main.origo.core.event.NodeContext;
import main.origo.core.helpers.ElementHelper;
import main.origo.core.ui.Element;
import main.origo.core.ui.RenderingContext;
import play.api.templates.Html;
import play.i18n.Messages;
import view.origo.bootstrapdatepicker.html.datepicker;

import java.util.Date;

@Interceptor
public class BootstrapDatePickerDecorator {

    private static final String JS_LOADED = "datepicker_js_loaded";

    @Decorates(types = Element.InputText.class, input = Date.class)
    public static Html decorateDateFields(Element element, RenderingContext renderingContext) {

        element.setId("datepicker-" + element.getId());

        return datepicker.render(element, ElementHelper.getHtmlFromBody(element), element.getAttributes());
    }

    @OnInsertElement(with = Element.InputText.class, input = Date.class)
    public static void addJavascriptSrc(Node node, Element parent, Element element) {
        if(!NodeContext.current().attributes.containsKey(JS_LOADED)) {
            String script = routes.Assets.at("javascripts/origo/bootstrapdatepicker/bootstrap-datepicker.js").url();
            node.addTailElement(new Element.Script().addAttribute("src", script));
            String style = routes.Assets.at("stylesheets/origo/bootstrapdatepicker/datepicker-custom.css").url();
            node.addHeadElement(new Element.Link().addAttribute("href", style).addAttribute("rel", "stylesheet"));

            NodeContext.current().attributes.put(JS_LOADED, true);
        }

    }

    @OnInsertElement(with = Element.InputText.class, after = true, input = Date.class)
    public static void addJavascriptInvocation(Node node, Element parent, Element element) {

        String dateFormat = Messages.get("date.format").toLowerCase();

        node.addTailElement(new Element.Script().setBody(
                "$('#" + "datepicker-" + element.id + "').datepicker({ " +
                        "format: '" + dateFormat + "', " +
                        "todayBtn: 'linked', " +
                        "todayHighlight: true, " +
                        "});\n" +
                "$('#cal-datepicker-" + element.id +"').bind('click', function(){$('#" + "datepicker-" + element.id + "').datepicker('show');})"
        ));

    }

}

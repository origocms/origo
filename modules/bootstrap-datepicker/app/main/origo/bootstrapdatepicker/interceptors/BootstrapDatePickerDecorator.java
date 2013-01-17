package main.origo.bootstrapdatepicker.interceptors;

import controllers.routes;
import main.origo.core.annotations.Decorates;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnInsertElement;
import main.origo.core.helpers.ElementHelper;
import main.origo.core.ui.Element;
import play.api.templates.Html;
import play.i18n.Messages;
import view.origo.bootstrapdatepicker.html.datepicker;

import java.util.Date;

@Interceptor
public class BootstrapDatePickerDecorator {

    private static final String JS_LOADED = "datepicker_js_loaded";

    @Decorates(types = Element.InputText.class, input = Date.class)
    public static Html decorateDateFields(Decorates.Context context) {

        context.element.setId("datepicker-" + context.element.getId());

        return datepicker.render(context.element, ElementHelper.getHtmlFromBody(context.element), context.element.getAttributes());
    }

    @OnInsertElement(with = Element.InputText.class, input = Date.class)
    public static void addJavascriptSrc(OnInsertElement.Context context) {
        if(!context.attributes().containsKey(JS_LOADED)) {
            String script = routes.Assets.at("javascripts/origo/bootstrapdatepicker/bootstrap-datepicker.js").url();
            context.node().addTailElement(new Element.Script().addAttribute("src", script));
            String style = routes.Assets.at("stylesheets/origo/bootstrapdatepicker/datepicker-custom.css").url();
            context.node().addHeadElement(new Element.Link().addAttribute("href", style).addAttribute("rel", "stylesheet"));

            context.attributes().put(JS_LOADED, true);
        }

    }

    @OnInsertElement(with = Element.InputText.class, after = true, input = Date.class)
    public static void addJavascriptInvocation(OnInsertElement.Context context) {

        String dateFormat = Messages.get("date.format").toLowerCase();

        context.node().addTailElement(new Element.Script().setBody(
                "$('#" + "datepicker-" + context.element.id + "').datepicker({ " +
                        "format: '" + dateFormat + "', " +
                        "todayBtn: 'linked', " +
                        "todayHighlight: true, " +
                        "});\n" +
                "$('#cal-datepicker-" + context.element.id +"').bind('click', function(){$('#" + "datepicker-" + context.element.id + "').datepicker('show');})"
        ));

    }

}

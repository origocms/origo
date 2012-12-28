package main.origo.core.interceptors.forms;

import controllers.routes;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnLoad;
import main.origo.core.annotations.Provides;
import main.origo.core.annotations.Types;
import main.origo.core.ui.Element;
import models.origo.core.Content;

/**
 * This is the default editor in the system. A module can provide a different editor provider by changing it in the settings.
 */
@Interceptor
public class TinyMCEEditorProvider {

    public static final String EDITOR_TYPE = "origo.admin.editor.tinymce";
    private static final String JS_LOADED = "tinymce_js_loaded";

    @OnLoad(type = Types.RICHTEXT_EDITOR, with = EDITOR_TYPE)
    public static void setupEditor(OnLoad.Context context) {
        if (!context.attributes.containsKey(JS_LOADED)) {
            String jqueryTinyMCEScript = routes.Assets.at("javascripts/origo/tiny_mce/jquery.tiny_mce.js").url();
            if (jqueryTinyMCEScript != null) {
                String tinyMCEScript = routes.Assets.at("javascripts/origo/tiny_mce/tiny_mce.js").url();
                context.node.addTailUIElement(new Element.Script().setId(EDITOR_TYPE+"_src").setWeight(9999).addAttribute("type", "text/javascript").addAttribute("src", jqueryTinyMCEScript));
                context.node.addTailUIElement(new Element.Script().setId(EDITOR_TYPE+"_invocation").setWeight(10000).addAttribute("type", "text/javascript").
                        setBody(
                                "$().ready(function() {\n" +
                                        "  $('textarea.tinymce').tinymce({\n" +
                                        "    script_url : '" + tinyMCEScript + "',\n" +
                                        "// General options\n" +
                                        "      theme : \"advanced\",\n" +
                                        "      plugins : \"autolink,lists,pagebreak,style,layer,table,save,advhr,advimage,advlink,emotions,iespell,inlinepopups,insertdatetime,preview,media,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras,template,advlist\",\n" +
                                        "\n" +
                                        "      // Theme options\n" +
                                        //"      theme_advanced_buttons1 : \"save,newdocument,|,bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,styleselect,formatselect,fontselect,fontsizeselect\",\n" +
                                        "      theme_advanced_buttons1 : \"save,|,bold,italic,underline,strikethrough,|,styleselect,formatselect,fontselect,fontsizeselect\",\n" +
                                        //"      theme_advanced_buttons2 : \"cut,copy,paste,pastetext,pasteword,|,search,replace,|,bullist,numlist,|,outdent,indent,blockquote,|,undo,redo,|,link,unlink,anchor,image,cleanup,help,code,|,insertdate,inserttime,preview,|,forecolor,backcolor\",\n" +
                                        "      theme_advanced_buttons2 : \"cut,copy,paste,pastetext,pasteword,|,search,replace,|,bullist,numlist,|,outdent,indent,blockquote,|,undo,redo,|,link,unlink,anchor,image,cleanup,help,code,|,insertdate,inserttime,preview,|,forecolor,backcolor\",\n" +
                                        //"      theme_advanced_buttons3 : \"tablecontrols,|,hr,removeformat,visualaid,|,sub,sup,|,charmap,emotions,iespell,media,advhr,|,print,|,ltr,rtl,|,fullscreen\",\n" +
                                        "      theme_advanced_buttons3 : \"hr,removeformat,visualaid,|,sub,sup,|,iespell,\",\n" +
                                        //"      theme_advanced_buttons4 : \"insertlayer,moveforward,movebackward,absolute,|,styleprops,|,cite,abbr,acronym,del,ins,attribs,|,visualchars,nonbreaking,template,pagebreak\",\n" +
                                        "      theme_advanced_toolbar_location : \"top\",\n" +
                                        "      theme_advanced_toolbar_align : \"left\",\n" +
                                        "      theme_advanced_statusbar_location : \"bottom\",\n" +
                                        "      theme_advanced_resizing : true,\n" +
                                        "  });\n" +
                                        "})\n"
                        )
                );
            }

            context.attributes.put(JS_LOADED, true);
        }
    }

    @Provides(type = Types.RICHTEXT_EDITOR, with = EDITOR_TYPE)
    public static Element createEditor(Provides.Context context) {
        return new Element.InputTextArea().addAttribute("class", "tinymce");
    }

    @OnLoad(type = Types.RICHTEXT_EDITOR, with = EDITOR_TYPE)
    public static void addContent(OnLoad.Context context) {
        Content content = (Content) context.args.get("content");
        context.element.setBody(content.value);
    }

}
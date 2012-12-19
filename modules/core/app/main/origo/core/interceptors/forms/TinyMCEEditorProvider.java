package main.origo.core.interceptors.forms;

import controllers.routes;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnLoad;
import main.origo.core.annotations.Provides;
import main.origo.core.annotations.Types;
import main.origo.core.ui.UIElement;
import models.origo.core.Content;

/**
 * This is the default editor in the system. A module can provide a different editor provider by changing it in the settings.
 */
@Interceptor
public class TinyMCEEditorProvider {

    public static final String EDITOR_TYPE = "origo.admin.editor.tinymce";

    @Provides(type = Types.RICHTEXT_EDITOR, with = EDITOR_TYPE)
    public static UIElement createEditor(Provides.Context context) {

        String jqueryTinyMCEScript = routes.Assets.at("javascripts/tiny_mce/jquery.tiny_mce.js").url();
        if (jqueryTinyMCEScript != null) {
            String tinyMCEScript = routes.Assets.at("javascripts/tiny_mce/tiny_mce.js").url();
            context.node.addTailUIElement(new UIElement(UIElement.SCRIPT, 9999).addAttribute("type", "text/javascript").addAttribute("src", jqueryTinyMCEScript));
            context.node.addTailUIElement(new UIElement(UIElement.SCRIPT, 10000).addAttribute("type", "text/javascript").
                    setBody(
                            "$().ready(function() {\n" +
                            "\t$('textarea.tinymce').tinymce({\n" +
                            "\t\tscript_url : '"+tinyMCEScript+"',\n" +
                            "// General options\n" +
                            "\t\t\ttheme : \"advanced\",\n" +
                            "\t\t\tplugins : \"autolink,lists,pagebreak,style,layer,table,save,advhr,advimage,advlink,emotions,iespell,inlinepopups,insertdatetime,preview,media,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras,template,advlist\",\n" +
                            "\n" +
                            "\t\t\t// Theme options\n" +
                            //"\t\t\ttheme_advanced_buttons1 : \"save,newdocument,|,bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,styleselect,formatselect,fontselect,fontsizeselect\",\n" +
                            "\t\t\ttheme_advanced_buttons1 : \"save,|,bold,italic,underline,strikethrough,|,styleselect,formatselect,fontselect,fontsizeselect\",\n" +
                            //"\t\t\ttheme_advanced_buttons2 : \"cut,copy,paste,pastetext,pasteword,|,search,replace,|,bullist,numlist,|,outdent,indent,blockquote,|,undo,redo,|,link,unlink,anchor,image,cleanup,help,code,|,insertdate,inserttime,preview,|,forecolor,backcolor\",\n" +
                            "\t\t\ttheme_advanced_buttons2 : \"cut,copy,paste,pastetext,pasteword,|,search,replace,|,bullist,numlist,|,outdent,indent,blockquote,|,undo,redo,|,link,unlink,anchor,image,cleanup,help,code,|,insertdate,inserttime,preview,|,forecolor,backcolor\",\n" +
                            //"\t\t\ttheme_advanced_buttons3 : \"tablecontrols,|,hr,removeformat,visualaid,|,sub,sup,|,charmap,emotions,iespell,media,advhr,|,print,|,ltr,rtl,|,fullscreen\",\n" +
                            "\t\t\ttheme_advanced_buttons3 : \"hr,removeformat,visualaid,|,sub,sup,|,iespell,\",\n" +
                            //"\t\t\ttheme_advanced_buttons4 : \"insertlayer,moveforward,movebackward,absolute,|,styleprops,|,cite,abbr,acronym,del,ins,attribs,|,visualchars,nonbreaking,template,pagebreak\",\n" +
                            "\t\t\ttheme_advanced_toolbar_location : \"top\",\n" +
                            "\t\t\ttheme_advanced_toolbar_align : \"left\",\n" +
                            "\t\t\ttheme_advanced_statusbar_location : \"bottom\",\n" +
                            "\t\t\ttheme_advanced_resizing : true,\n" +
                            "\t});\n" +
                            "})\n"
                    )
            );
        }

        return new UIElement(UIElement.INPUT_TEXTAREA).addAttribute("class", "tinymce");
    }

    @OnLoad(type = Types.RICHTEXT_EDITOR, with = EDITOR_TYPE)
    public static void addContent(OnLoad.Context context) {
        Content content = (Content) context.args.get("content");
        context.uiElement.setBody(content.value);
    }

}

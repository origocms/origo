package main.origo.admin.interceptors.content;

import main.origo.admin.annotations.Admin;
import main.origo.admin.forms.BasicPageForm;
import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnLoad;
import main.origo.core.helpers.forms.FormHelper;
import main.origo.core.ui.Element;
import play.data.Form;
import play.i18n.Messages;

import java.util.Date;
import java.util.Map;

@Interceptor
public class BasicPageAdminPublishingTabProvider {

    private static final String PUBLISH_DATE_PARAM = "publishDate";
    private static final String PUBLISH_TIME_PARAM = "publishTime";
    private static final String UNPUBLISH_DATE_PARAM = "unpublishDate";
    private static final String UNPUBLISH_TIME_PARAM = "unpublishTime";

    /**
     * Publishing options
     */

    @OnLoad(with = Admin.With.TAB_BAR)
    public static void addTabItem(Node node, String withType, Element element, Map<String, Object> args) throws ModuleException, NodeLoadException {

        element.addChild(new Admin.TabItem().setWeight(500).addChild(new Element.Anchor().addAttribute("href", "#publishTab").setBody("Publish")));

    }

    @OnLoad(with = Admin.With.TAB_CONTENT)
    public static void loadNewPage(Node node, String withType, Element element, Map<String, Object> args) throws ModuleException, NodeLoadException {

        Element publishingFieldSet = new Element.FieldSet().setId("publish").
                addChild(new Element.Legend().setBody("Publish"));

        element.addChild(
                new Admin.TabPane().setId("publishTab").
                        addChild(new Element.Panel(new Element.Heading3().setBody("Publish")).
                                addChild(publishingFieldSet)
                        ));

        Form<BasicPageForm> form = FormHelper.getValidationResult(BasicPageForm.class);

        String datePattern = Messages.get("date.format");
        Element publishElement = new Element.Container().setWeight(15).addAttribute("class", "field").
                addChild(new Element.Container().addAttribute("class", "split-left").
                        addChild(new Element.Label().setWeight(10).setBody("From Date").
                                addAttribute("for", "date-" + PUBLISH_DATE_PARAM)
                        ).
                        addChild(new Element.InputText(Date.class).setId("date-" + PUBLISH_DATE_PARAM).
                                addAttribute("name", PUBLISH_DATE_PARAM).
                                addAttribute("value", FormHelper.getFieldValue(form, PUBLISH_DATE_PARAM)).
                                addAttribute("placeholder", datePattern.toLowerCase())
                        )
                ).
                addChild(new Element.Container().addAttribute("class", "split-right").
                        addChild(new Element.Label().setWeight(10).setBody("Until Date").
                                addAttribute("for", "date-" + UNPUBLISH_DATE_PARAM)
                        ).
                        addChild(new Element.InputText(Date.class).setId("date-" + UNPUBLISH_DATE_PARAM).
                                addAttribute("name", UNPUBLISH_DATE_PARAM).
                                addAttribute("value", FormHelper.getFieldValue(form, UNPUBLISH_DATE_PARAM)).
                                addAttribute("placeholder", datePattern.toLowerCase()))
                );
        publishingFieldSet.addChild(publishElement);

        String timePattern = Messages.get("time.format");
        Element publishTimeElement = new Element.Container().setWeight(15).addAttribute("class", "field").
                addChild(new Element.Container().addAttribute("class", "split-left").
                        addChild(new Element.Label().setWeight(10).setBody("From Time").
                                addAttribute("for", "date-" + PUBLISH_TIME_PARAM)
                        ).
                        addChild(new Element.InputText().setId("date-" + PUBLISH_TIME_PARAM).
                                addAttribute("name", PUBLISH_TIME_PARAM).
                                addAttribute("value", FormHelper.getFieldValue(form, PUBLISH_TIME_PARAM)).
                                addAttribute("placeholder", timePattern.toLowerCase()))
                ).
                addChild(new Element.Container().addAttribute("class", "split-right").
                        addChild(new Element.Label().setWeight(10).setBody("Until Time").
                                addAttribute("for", "date-" + UNPUBLISH_TIME_PARAM)
                        ).
                        addChild(new Element.InputText().setId("date-" + UNPUBLISH_TIME_PARAM).
                                addAttribute("name", UNPUBLISH_TIME_PARAM).
                                addAttribute("value", FormHelper.getFieldValue(form, UNPUBLISH_TIME_PARAM)).
                                addAttribute("placeholder", timePattern.toLowerCase()))
                );
        publishingFieldSet.addChild(publishTimeElement);

    }
}

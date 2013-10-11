package main.origo.admin.interceptors.content;

import main.origo.admin.annotations.Admin;
import main.origo.admin.forms.BasicPageForm;
import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.ThemeRepository;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.OnLoad;
import main.origo.core.helpers.CoreSettingsHelper;
import main.origo.core.helpers.forms.FormHelper;
import main.origo.core.internal.CachedThemeVariant;
import main.origo.core.ui.Element;
import org.apache.commons.lang3.StringUtils;
import play.data.Form;

import java.util.Map;

@Interceptor
public class BasicPageAdminGeneralTabProvider {

    private static final String TITLE_PARAM = "title";
    private static final String THEME_VARIANT_PARAM = "themeVariant";

    @OnLoad(with = Admin.With.TAB_BAR)
    public static void addTabItem(Node node, String withType, Element element, Map<String, Object> args) throws ModuleException, NodeLoadException {

        element.
                addChild(new Admin.TabItem().setWeight(100).
                        addAttribute("class", "active").
                        addChild(new Element.Anchor().
                                addAttribute("href", "#generalTab").setBody("General")));
    }

    @OnLoad(with = Admin.With.TAB_CONTENT)
    public static void loadNewPage(Node node, String withType, Element element, Map<String, Object> args) throws ModuleException, NodeLoadException {

        Form<BasicPageForm> form = FormHelper.getValidationResult(BasicPageForm.class);

        Element basicFieldSet = new Element.FieldSet().setId("general").
                addChild(new Element.Legend().setBody("Basic"));

        element.addChild(
                new Admin.TabPane().setId("generalTab").addAttribute("class", "active").
                        addChild(new Element.Panel(new Element.Heading3().setBody("General")).
                                addChild(basicFieldSet)
                        ));

        Element themeInputSelectElement = new Element.InputSelect();
        String themeVariantFormValue = FormHelper.getFieldValue(form, THEME_VARIANT_PARAM);
        for (CachedThemeVariant themeVariant : ThemeRepository.getAvailableThemeVariants()) {
            Element optionElement = new Element.InputSelectOption().setBody(themeVariant.variantId);
            if (StringUtils.isEmpty(themeVariantFormValue)) {
                if (themeVariant.variantId.equals(CoreSettingsHelper.getThemeVariant())) {
                    optionElement.addAttribute("selected", "selected");
                }
            } else {
                if (themeVariant.variantId.equals(themeVariantFormValue)) {
                    optionElement.addAttribute("selected", "selected");
                }
            }
            themeInputSelectElement.addChild(optionElement);
        }

        basicFieldSet.
                addChild(
                        FormHelper.createField(form,
                                new Element.Label().setWeight(10).setBody("Title").addAttribute("for", TITLE_PARAM),
                                new Element.InputText().setWeight(20).addAttribute("name", TITLE_PARAM)
                        )
                ).
                addChild(
                        FormHelper.createField(form,
                                new Element.Label().setWeight(10).setBody("Theme Variant").addAttribute("for", THEME_VARIANT_PARAM),
                                themeInputSelectElement.setWeight(25).addAttribute("class", "themeSelector").
                                        addAttribute("name", THEME_VARIANT_PARAM)
                        )
                );

    }
}

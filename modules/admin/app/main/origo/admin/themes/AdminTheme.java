package main.origo.admin.themes;

import main.origo.core.annotations.Theme;
import main.origo.core.annotations.ThemeVariant;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.origo.core.themes.DefaultTheme.variant_main_only;

@Theme(id = "admin")
public class AdminTheme {

    public static final String DEFAULT_VARIANT_NAME = "admin-default";

    @ThemeVariant(id = DEFAULT_VARIANT_NAME, regions = "main")
    public static Result renderDefaultTemplate(ThemeVariant.Context context) {
        return Controller.ok(variant_main_only.render(context));
    }

}

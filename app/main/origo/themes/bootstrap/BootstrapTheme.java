package main.origo.themes.bootstrap;

import main.origo.core.annotations.Theme;
import main.origo.core.annotations.ThemeVariant;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.origo.themes.bootstrap.variant_main_and_left_columns;
import views.html.origo.themes.bootstrap.variant_main_only;
import views.html.origo.themes.bootstrap.variant_three_columns;

@Theme(id = main.origo.themes.bootstrap.BootstrapTheme.ID)
public class BootstrapTheme {

    public static final String ID = "bootstrap";

    @ThemeVariant(id = "bootstrap-main_only", regions = {"main"})
    public static Result renderDefaultMainOnlyTemplate(ThemeVariant.Context context) {
        return Controller.ok(variant_main_only.render(context));
    }

    @ThemeVariant(id = "bootstrap-main_and_left_columns", regions = {"main", "left"})
    public static Result renderDefaultMainAndLeftColumnTemplate(ThemeVariant.Context context) {
        return Controller.ok(variant_main_and_left_columns.render(context));
    }

    @ThemeVariant(id = "bootstrap-three_columns", regions = {"main", "left", "right"})
    public static Result renderDefaultThreeColumnTemplate(ThemeVariant.Context context) {
        return Controller.ok(variant_three_columns.render(context));
    }

}

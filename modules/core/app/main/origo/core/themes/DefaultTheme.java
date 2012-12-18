package main.origo.core.themes;

import main.origo.core.annotations.Theme;
import main.origo.core.annotations.ThemeVariant;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.origo.core.themes.DefaultTheme.*;

@Theme(id = "default")
public class DefaultTheme {

    @ThemeVariant(id = "default-main_only", regions = {"main"})
    public static Result getDefaultMainOnlyTemplate(ThemeVariant.Context context) {
        return Controller.ok(variant_main_only.render(context));
    }

    @ThemeVariant(id = "default-main_and_left_columns", regions = {"main", "left"})
    public static Result getDefaultMainAndLeftColumnTemplate(ThemeVariant.Context context) {
        return Controller.ok(variant_main_and_left_columns.render(context));
    }

    @ThemeVariant(id = "default-three_columns", regions = {"main", "left", "right"})
    public static Result getDefaultThreeColumnTemplate(ThemeVariant.Context context) {
        return Controller.ok(variant_three_columns.render(context));
    }

}

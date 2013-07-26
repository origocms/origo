package main.origo.themes.bootstrap;

import main.origo.core.annotations.Theme;
import main.origo.core.annotations.ThemeVariant;
import play.mvc.Content;
import views.html.origo.themes.bootstrap.variant_main_and_left_columns;
import views.html.origo.themes.bootstrap.variant_main_only;
import views.html.origo.themes.bootstrap.variant_three_columns;

@Theme(id = main.origo.themes.bootstrap.BootstrapTheme.ID)
public class BootstrapTheme {

    public static final String ID = "bootstrap";

    @ThemeVariant(id = "bootstrap-main_only", regions = {"main"})
    public static Content renderDefaultMainOnlyTemplate(ThemeVariant.Context context) {
        return variant_main_only.render(context);
    }

    @ThemeVariant(id = "bootstrap-main_and_left_columns", regions = {"main", "left"})
    public static Content renderDefaultMainAndLeftColumnTemplate(ThemeVariant.Context context) {
        return variant_main_and_left_columns.render(context);
    }

    @ThemeVariant(id = "bootstrap-three_columns", regions = {"main", "left", "right"})
    public static Content renderDefaultThreeColumnTemplate(ThemeVariant.Context context) {
        return variant_three_columns.render(context);
    }

}

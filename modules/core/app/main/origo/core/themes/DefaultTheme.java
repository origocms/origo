package main.origo.core.themes;

import main.origo.core.annotations.Theme;
import main.origo.core.annotations.ThemeVariant;
import play.mvc.Content;
import views.html.origo.core.themes.DefaultTheme.variant_main_and_left_columns;
import views.html.origo.core.themes.DefaultTheme.variant_main_only;
import views.html.origo.core.themes.DefaultTheme.variant_three_columns;

@Theme(id = DefaultTheme.ID)
public class DefaultTheme {

    public static final String ID = "default";

    @ThemeVariant(id = "default-main_only", regions = {"main"})
    public static Content renderDefaultMainOnlyTemplate(ThemeVariant.Context context) {
        return variant_main_only.render(context);
    }

    @ThemeVariant(id = "default-main_and_left_columns", regions = {"main", "left"})
    public static Content renderDefaultMainAndLeftColumnTemplate(ThemeVariant.Context context) {
        return variant_main_and_left_columns.render(context);
    }

    @ThemeVariant(id = "default-three_columns", regions = {"main", "left", "right"})
    public static Content renderDefaultThreeColumnTemplate(ThemeVariant.Context context) {
        return variant_three_columns.render(context);
    }

}

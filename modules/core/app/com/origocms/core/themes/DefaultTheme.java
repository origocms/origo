package com.origocms.core.themes;

import com.origocms.core.annotations.Theme;
import com.origocms.core.annotations.ThemeVariant;
import com.origocms.core.ui.RenderedNode;

@Theme(id = "default")
public class DefaultTheme {

    @ThemeVariant(id = "default-main_and_left_columns", regions = {"main", "left"})
    public String getDefaultMainAndLeftColumnTemplate(RenderedNode renderedNode) {
        return "themes/origo/DefaultTheme/variant_main_and_left_columns.html";
    }

    @ThemeVariant(id = "default-three_columns", regions = {"main", "left", "right"})
    public String getDefaultThreeColumnTemplate(RenderedNode renderedNode) {
        return "themes/origo/DefaultTheme/variant_three_columns.html";
    }

}

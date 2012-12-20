package main.origo.admin.interceptors;

import main.origo.admin.annotations.Admin;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.annotations.Relationship;
import main.origo.core.ui.UIElement;

@Interceptor
public class BasicUserAdminProvider {

    private static final String BASE_TYPE = "origo.admin.dashboard.user.basicuser";

    @Provides(type = Admin.DASHBOARD_ITEM, with = BASE_TYPE)
    @Relationship(parent = Admin.USER_PAGE_TYPE)
    public static UIElement createDashboardItem(Provides.Context context) {
        return new UIElement(Admin.DASHBOARD_ITEM).addAttribute("class", "item").
                addChild(new UIElement(UIElement.PANEL, 20).
                        addChild(new UIElement(UIElement.HEADING4, 10, "User").addAttribute("class", "title")));
    }

}
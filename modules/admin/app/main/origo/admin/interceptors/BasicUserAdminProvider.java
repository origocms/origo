package main.origo.admin.interceptors;

import main.origo.admin.annotations.Admin;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.annotations.Relationship;
import main.origo.core.ui.Element;

@Interceptor
public class BasicUserAdminProvider {

    private static final String BASE_TYPE = "origo.admin.dashboard.user.basicuser";

    /*
     * Creating the Dashboard Items for the Basic type on the User dashboard.
     */
    @Provides(type = Admin.DASHBOARD_ITEM, with = BASE_TYPE)
    @Relationship(parent = Admin.USER_PAGE_TYPE)
    public static Element createDashboardItem(Provides.Context context) {
        return new Admin.DashboardItem().addAttribute("class", "item").
                addChild(new Element.Panel().setWeight(20).
                        addChild(new Element.Heading4().setWeight(10).setBody("User").addAttribute("class", "title")));
    }

}
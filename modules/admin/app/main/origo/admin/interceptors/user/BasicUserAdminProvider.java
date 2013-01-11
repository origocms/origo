package main.origo.admin.interceptors.user;

import controllers.origo.admin.routes;
import main.origo.admin.annotations.Admin;
import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.Provides;
import main.origo.core.annotations.Relationship;
import main.origo.core.ui.Element;

@Interceptor
public class BasicUserAdminProvider {

    private static final String BASE_TYPE = Admin.With.USER_PAGE + ".basicuser";
    private static final String EDIT_TYPE = BASE_TYPE + ".Admin";

    /*
     * Creating the Dashboard Items for the Basic type on the User dashboard.
     */
    @Provides(type = Admin.Type.DASHBOARD_ITEM, with = BASE_TYPE)
    @Relationship(parent = Admin.With.USER_PAGE)
    public static Element createDashboardItem(Provides.Context context) {
        return new Admin.DashboardItem().addAttribute("class", "item").
                addChild(new Element.Panel().setWeight(20).
                        addChild(new Element.Heading4().setWeight(10).setBody("User").addAttribute("class", "title")));
    }

    @Admin.Navigation(alias="/settings/user")
    private static String getProviderUrl() {
        return routes.Dashboard.pageWithType(Admin.With.USER_PAGE, EDIT_TYPE).url();
    }

}
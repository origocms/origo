package main.origo.admin;

import main.origo.admin.annotations.Admin;
import main.origo.admin.helpers.SettingsAdminHelper;
import main.origo.admin.themes.AdminTheme;
import main.origo.core.helpers.SettingsCoreHelper;
import models.origo.core.Settings;
import org.springframework.stereotype.Component;
import play.db.jpa.JPA;
import play.libs.F;

import javax.annotation.PostConstruct;

@Component
public class AdminBootStrap {

    @PostConstruct
    public void doJob() {
        JPA.withTransaction(new F.Callback0() {
            @Override
            public void invoke() throws Throwable {
                Settings settings = Settings.load();
                SettingsCoreHelper.setValueIfMissing(settings, SettingsAdminHelper.Keys.DASHBOARD_TYPE, Admin.FRONT_PAGE_TYPE);
                SettingsCoreHelper.setValueIfMissing(settings, SettingsAdminHelper.Keys.THEME_VARIANT, AdminTheme.DEFAULT_VARIANT_NAME);
                settings.save();
            }
        });

    }
}

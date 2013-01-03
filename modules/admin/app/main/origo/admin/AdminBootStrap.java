package main.origo.admin;

import main.origo.admin.annotations.Admin;
import main.origo.admin.helpers.AdminSettingsHelper;
import main.origo.admin.themes.AdminTheme;
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
                settings.setValueIfMissing(AdminSettingsHelper.Keys.HOME_DASHBOARD_TYPE, Admin.With.FRONT_PAGE);
                settings.setValueIfMissing(AdminSettingsHelper.Keys.THEME_VARIANT, AdminTheme.DEFAULT_VARIANT_NAME);
                settings.save();
            }
        });

    }
}

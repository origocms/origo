package main.origo.admin;

import main.origo.admin.interceptors.DefaultDashboardProvider;
import main.origo.admin.interceptors.TinyMCEEditorProvider;
import main.origo.admin.themes.AdminTheme;
import main.origo.core.helpers.SettingsHelper;
import models.origo.core.Settings;
import models.origo.core.SettingsKeys;
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
                SettingsHelper.setValueIfMissing(settings, SettingsKeys.Admin.DASHBOARD_TYPE, DefaultDashboardProvider.TYPE);
                SettingsHelper.setValueIfMissing(settings, SettingsKeys.Admin.THEME_VARIANT, AdminTheme.DEFAULT_VARIANT_NAME);
                SettingsHelper.setValueIfMissing(settings, SettingsKeys.Admin.RICHTEXT_EDITOR_TYPE, TinyMCEEditorProvider.EDITOR_TYPE);
                settings.save();
            }
        });

    }
}

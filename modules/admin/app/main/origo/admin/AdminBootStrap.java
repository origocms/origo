package main.origo.admin;

import main.origo.admin.annotations.Admin;
import main.origo.admin.themes.AdminTheme;
import main.origo.core.helpers.SettingsHelper;
import main.origo.core.interceptors.forms.TinyMCEEditorProvider;
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
                SettingsHelper.setValueIfMissing(settings, SettingsKeys.Admin.DASHBOARD_TYPE, Admin.FRONT_PAGE_TYPE);
                SettingsHelper.setValueIfMissing(settings, SettingsKeys.Admin.THEME_VARIANT, AdminTheme.DEFAULT_VARIANT_NAME);
                SettingsHelper.setValueIfMissing(settings, SettingsKeys.Admin.RICHTEXT_EDITOR_TYPE, TinyMCEEditorProvider.EDITOR_TYPE);
                settings.save();
            }
        });

    }
}

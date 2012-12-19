package main.origo.core;

import main.origo.core.helpers.SettingsHelper;
import main.origo.core.interceptors.forms.DefaultFormProvider;
import main.origo.core.interceptors.forms.DefaultSubmitHandler;
import models.origo.core.Settings;
import models.origo.core.SettingsKeys;
import org.springframework.stereotype.Component;
import play.db.jpa.JPA;
import play.libs.F;

import javax.annotation.PostConstruct;

@Component
public class CoreBootStrap {

    @PostConstruct
    public void doJob() {
        JPA.withTransaction(new F.Callback0() {
            @Override
            public void invoke() throws Throwable {
                Settings settings = Settings.load();
                SettingsHelper.setValueIfMissing(settings, SettingsKeys.Core.THEME_VARIANT, "default-main_and_left_columns");
                SettingsHelper.setValueIfMissing(settings, SettingsKeys.Core.SUBMIT_HANDLER, DefaultSubmitHandler.class.getName());
                SettingsHelper.setValueIfMissing(settings, SettingsKeys.Core.DEFAULT_FORM_TYPE, DefaultFormProvider.TYPE);
                settings.save();
            }
        });
    }
}

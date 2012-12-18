package main.origo.core;

import main.origo.core.helpers.SettingsHelper;
import main.origo.core.interceptors.forms.DefaultFormProvider;
import main.origo.core.interceptors.forms.DefaultSubmitHandler;
import models.origo.core.Settings;
import models.origo.core.SettingsKeys;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class CoreBootStrap {

    @PostConstruct
    public void doJob() {
        Settings settings = Settings.load();
        SettingsHelper.setValueIfMissing(settings, SettingsKeys.Core.THEME_VARIANT, "default-main_and_left_columns");
        SettingsHelper.setValueIfMissing(settings, SettingsKeys.Core.SUBMIT_HANDLER, DefaultSubmitHandler.class.getName());
        SettingsHelper.setValueIfMissing(settings, SettingsKeys.Core.DEFAULT_FORM_TYPE, DefaultFormProvider.TYPE);
        settings.save();
    }
}

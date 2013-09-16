package main.origo.core.helpers;

import models.origo.core.Settings;
import play.Logger;

public class SettingsHelper {

    public static String getClassTypeIfExists(String propertyName, String fallback) {
        String classType = Settings.load().getValue(propertyName);
        if (classType != null) {
            try {
                return Class.forName(classType).getName();
            } catch (ClassNotFoundException e) {
                Logger.warn("Unable to find " + propertyName + " type [" + classType + "], using fallback [" + fallback + "] instead");
            }
        }
        return fallback;
    }
}

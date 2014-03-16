package models.origo.core;

import org.apache.commons.lang3.StringUtils;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Key/Value based store for system-wide settings.
 *
 * @see main.origo.core.helpers.CoreSettingsHelper
 */
@Entity
@Table(name = "settings")
public class Settings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @MapKeyColumn(name = "name")
    @ElementCollection
    @JoinTable(name = "settings_values")
    @Column(name = "value")
    private Map<String, String> values = new HashMap<String, String>();

    public Map<String, String> getValues() {
        return values;
    }

    public Settings setValues(final Map<String, String> values) {
        this.values = values;
        return this;
    }

    public boolean containsKey(final String key) {
        return getValues().containsKey(key);
    }

    public Settings setValue(final String key, final String value) {
        getValues().put(key, value);
        return this;
    }

    public String getValue(final String key) {
        return getValues().get(key);
    }

    /*
     * Getting general settings based on name
     */

    public Integer getValueAsInteger(String name) {
        return getValueAsInteger(name, null);
    }

    public Integer getValueAsInteger(String name, Integer defaultValue) {
        final String value = getValues().get(name);
        if (value != null) {
            return Integer.parseInt(value);
        }
        return defaultValue;
    }

    public Long getValueAsLong(String name) {
        return getValueAsLong(name, null);
    }

    public Long getValueAsLong(String name, Long defaultValue) {
        final String value = getValues().get(name);
        if (value != null) {
            return Long.parseLong(value);
        }
        return defaultValue;
    }

    public Boolean getValueAsBoolean(String name) {
        return getValueAsBoolean(name, null);
    }

    public Boolean getValueAsBoolean(String name, Boolean defaultValue) {
        final String value = getValues().get(name);
        if (value != null) {
            return Boolean.parseBoolean(value);
        }
        return defaultValue;
    }

    public Double getValueAsDouble(String name) {
        return getValueAsDouble(name, null);
    }

    public Double getValueAsDouble(String name, Double defaultValue) {
        final String value = getValues().get(name);
        if (value != null) {
            return Double.parseDouble(value);
        }
        return defaultValue;
    }

    public Float getValueAsFloat(String name) {
        return getValueAsFloat(name, null);
    }

    public Float getValueAsFloat(String name, Float defaultValue) {
        final String value = getValues().get(name);
        if (value != null) {
            return Float.parseFloat(value);
        }
        return defaultValue;
    }

    public Settings setValueIfMissing(String settingKey, String newValue) {
        if (StringUtils.isBlank(getValue(settingKey))) {
            setValue(settingKey, newValue);
        }
        return this;
    }

    public static Settings load() {
        try {
            return (Settings) JPA.em().createQuery("select s from models.origo.core.Settings s").getSingleResult();
        } catch (NoResultException ignored) {
        }
        return new Settings();
    }

    @SuppressWarnings("unchecked")
    public static Settings save(Settings settings) {
        return settings.save();
    }

    public Settings save() {
        try {
            Settings settings = load();
            if (settings.id == null || settings.id.equals(id)) {
                JPA.em().merge(this);
            } else {
                throw new RuntimeException("Only one instance of setting should be available in the system");
            }
        } catch (NoResultException ignored) {
            JPA.em().persist(this);
        }
        return this;
    }

}

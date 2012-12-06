package com.origocms.core.models;

import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Key/Value based store for system-wide settings.
 *
 * @see SettingsKeys
 */
@Entity(name = "settings")
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

    public void setValues(final Map<String, String> values) {
        this.values = values;
    }

    public boolean containsKey(final String key) {
        return getValues().containsKey(key);
    }

    public void setValue(final String key, final String value) {
        getValues().put(key, value);
    }

    public String getValue(final String key) {
        return getValues().get(key);
    }

    /*
     * Getting general settings based on name
     */

    public Integer getValueAsInteger(final String name) {
        final String value = getValues().get(name);
        if (value != null) {
            return Integer.parseInt(value);
        }
        return null;
    }

    public Long getValueAsLong(final String name) {
        final String value = getValues().get(name);
        if (value != null) {
            return Long.parseLong(value);
        }
        return null;
    }

    public Boolean getValueAsBoolean(String name) {
        final String value = getValues().get(name);
        if (value != null) {
            return Boolean.parseBoolean(value);
        }
        return null;
    }

    public Double getValueAsDouble(final String name) {
        final String value = getValues().get(name);
        if (value != null) {
            return Double.parseDouble(value);
        }
        return null;
    }

    public Float getValueAsFloat(final String name) {
        final String value = getValues().get(name);
        if (value != null) {
            return Float.parseFloat(value);
        }
        return null;
    }

    private static TypedQuery<Settings> loadQuery() {
        return JPA.em().createQuery("select s from models.Settings s", Settings.class);
    }

    private static Settings doSave(Settings settings) {
        return settings.save();
    }

    public static Settings load() {
        List<Settings> result = loadQuery().getResultList();
        if (result.isEmpty()) {
            return doSave(new Settings());
        }
        return result.get(0);
    }

    @SuppressWarnings("unchecked")
    public static Settings save(Settings settings) {
        return settings.save();
    }

    public Settings save() {
        List<Settings> result = loadQuery().getResultList();
        if (!result.isEmpty() && !result.get(0).id.equals(id)) {
            throw new RuntimeException("Only one instance of setting should be available in the system");
        }
        JPA.em().persist(this);
        return this;
    }

}

package models.origo.core;

import org.apache.commons.lang.BooleanUtils;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.HashMap;
import java.util.Map;

/**
 * Key/Value based store for system-wide settings.
 *
 * @see SettingsKeys
 */
@Entity
public class Settings extends Model {

    @Id
    public Long id;
    
    //@MapKeyColumn(name = "name")
    //@ElementCollection
    //@JoinTable(name = "settings_values")
    //@Column(name = "value")
    private transient Map<String, String> values = new HashMap<String, String>();

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
        final String value = getValue(name);
        if (value != null) {
            return Integer.parseInt(value);
        }
        return null;
    }

    public Long getValueAsLong(final String name) {
        final String value = getValue(name);
        if (value != null) {
            return Long.parseLong(value);
        }
        return null;
    }

    public Boolean getValueAsBoolean(String name) {
        final String value = getValue(name);
        if (value != null) {
            return BooleanUtils.toBoolean(value);
        }
        return null;
    }

    public Double getValueAsDouble(final String name) {
        final String value = getValue(name);
        if (value != null) {
            return Double.parseDouble(value);
        }
        return null;
    }

    public Float getValueAsFloat(final String name) {
        final String value = getValue(name);
        if (value != null) {
            return Float.parseFloat(value);
        }
        return null;
    }

    public static Finder<Long, Settings> find = new Finder<Long, Settings>(
            Long.class, Settings.class
    );

    private static boolean hasSettingsStored() {
        return find.findUnique() != null;
    }

    private static Settings doSave(Settings settings) {
        return settings.store();
    }

    public static Settings load() {
        if (hasSettingsStored()) {
            return find.findUnique();
        } else {
            return doSave(new Settings());
        }
    }

    @SuppressWarnings("unchecked")
    public static Settings save(Settings settings) {
        return settings.store();
    }

    public Settings store() {
        if (hasSettingsStored()) {
            if (!load().id.equals(id)) {
                throw new RuntimeException("Only one instance of setting should be available in the system");
            }
        }
        super.save();
        return this;
    }

}

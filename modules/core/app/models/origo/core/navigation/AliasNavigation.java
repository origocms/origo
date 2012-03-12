package models.origo.core.navigation;

import models.origo.core.Alias;
import controllers.origo.core.helpers.SettingsHelper;
import play.db.ebean.Model;
import play.data.validation.Constraints.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AliasNavigation extends Model {

    @Id
    public Long id;

    @Required
    public String identifier;

    // TODO: It would be better to have alias point to the id of an Alias instead (but that is really hard with yaml)
    @Required
    public String alias;

    public String getLink() {
        Alias aliasModel = Alias.findWithPath(alias);
        if (aliasModel != null && SettingsHelper.Core.getStartPage().equals(aliasModel.pageId)) {
            return SettingsHelper.Core.getBaseUrl();
        }
        return SettingsHelper.Core.getBaseUrl() + alias;
    }

    public static Finder<Long, AliasNavigation> find = new Finder<Long, AliasNavigation>(
            Long.class, AliasNavigation.class
    );

    public static AliasNavigation findWithIdentifier(String identifier) {
        return find.where().eq("identifier", identifier).findUnique();
    }

    @Override
    public String toString() {
        return new StringBuilder().
                append("AliasNavigation {").
                append("identifier='").append(identifier).append('\'').
                append(", alias='").append(alias).append('\'').
                append('}').
                toString();
    }
}

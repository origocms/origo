package models.origo.core.navigation;

import models.origo.core.Alias;
import models.origo.core.RootNode;
import origo.core.helpers.SettingsHelper;
import play.db.ebean.Model;
import play.data.validation.Constraints.*;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Collection;
import java.util.Date;

@Entity
public class PageIdNavigation extends Model {

    @Id
    public Long id;

    @Required
    //@Unique
    public String identifier;

    @Required
    public String pageId;

    public String getLink() {
        Collection<Alias> aliases = Alias.findWithPageId(pageId);
        if (aliases == null || aliases.isEmpty()) {
            if (SettingsHelper.Core.getStartPage().equals(pageId)) {
                return SettingsHelper.Core.getBaseUrl();
            }
            return SettingsHelper.Core.getBaseUrl() + RootNode.findLatestPublishedVersionWithNodeId(pageId, new Date()).getNodeId();
        } else {
            Alias alias = aliases.iterator().next();
            return SettingsHelper.Core.getBaseUrl() + alias.path;
        }
    }

    public static Finder<Long, PageIdNavigation> find = new Finder<Long, PageIdNavigation>(
            Long.class, PageIdNavigation.class
    );

    public static PageIdNavigation findWithIdentifier(String identifier) {
        return find.where().
                eq("identifier", identifier).
                findUnique();
    }
}

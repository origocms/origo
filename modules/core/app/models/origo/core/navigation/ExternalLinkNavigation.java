package models.origo.core.navigation;

import play.db.ebean.Model;
import play.data.validation.Constraints.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ExternalLinkNavigation extends Model {

    @Id
    public Long id;

    @Required
    //@Unique
    public String identifier;

    @Required
    public String title;

    @Required
    public String link;

    public String getLink() {
        return link;
    }

    public static Finder<Long, ExternalLinkNavigation> find = new Finder<Long, ExternalLinkNavigation>(
            Long.class, ExternalLinkNavigation.class
    );

    public static ExternalLinkNavigation findWithIdentifier(String identifier) {
        return find.where().eq("identifier", identifier).findUnique();
    }
}

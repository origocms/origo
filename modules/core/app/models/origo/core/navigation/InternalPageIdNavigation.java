package models.origo.core.navigation;

import main.origo.core.helpers.CoreSettingsHelper;
import models.origo.core.Alias;
import models.origo.core.BasicPage;
import models.origo.core.Model;
import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity(name = "pageIdNavigation")
@Table(name = "navigation_page_id")
public class InternalPageIdNavigation extends Model<InternalPageIdNavigation> {

    public static final String TYPE = "origo.navigation.pageid";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Constraints.Required
    @Column(unique = true)
    public String identifier;

    @Constraints.Required
    public String pageId;

    public InternalPageIdNavigation() {
        super(TYPE);
    }

    public String getLink() {
        if (CoreSettingsHelper.getStartPage().equals(pageId)) {
            return CoreSettingsHelper.getBaseUrl();
        }
        Collection<Alias> aliases = Alias.findWithPageId(pageId);
        return getAliasUrl(aliases);
    }

    private String getAliasUrl(Collection<Alias> aliases) {
        if (aliases == null || aliases.isEmpty()) {
            return CoreSettingsHelper.getBaseUrl() + BasicPage.findCurrentVersion(pageId, new Date()).getNodeId();
        } else {
            Alias alias = aliases.iterator().next();
            return CoreSettingsHelper.getBaseUrl() + alias.path;
        }
    }

    public static InternalPageIdNavigation findWithIdentifier(String identifier) {
        try {
            return (InternalPageIdNavigation) JPA.em().createQuery("from "+InternalPageIdNavigation.class.getName()+" where identifier=:identifier").
                    setParameter("identifier", identifier).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}

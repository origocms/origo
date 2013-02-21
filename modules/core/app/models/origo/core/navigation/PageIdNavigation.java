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
public class PageIdNavigation extends Model<PageIdNavigation> {

    public static final String TYPE = "models.origo.core.navigation.PageIdNavigation";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Constraints.Required
    @Column(unique = true)
    public String identifier;

    @Constraints.Required
    public String pageId;

    public PageIdNavigation() {
        super(TYPE);
    }

    public String getLink() {
        Collection<Alias> aliases = Alias.findWithPageId(pageId);
        if (aliases == null || aliases.isEmpty()) {
            if (CoreSettingsHelper.getStartPage().equals(pageId)) {
                return CoreSettingsHelper.getBaseUrl();
            }
            return CoreSettingsHelper.getBaseUrl() + BasicPage.findCurrentVersion(pageId, new Date()).getNodeId();
        } else {
            Alias alias = aliases.iterator().next();
            return CoreSettingsHelper.getBaseUrl() + alias.path;
        }
    }

    public static PageIdNavigation findWithIdentifier(String identifier) {
        try {
            return (PageIdNavigation) JPA.em().createQuery("from models.origo.core.navigation.PageIdNavigation where identifier=:identifier").
                    setParameter("identifier", identifier).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}

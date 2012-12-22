package models.origo.core.navigation;

import main.origo.core.helpers.SettingsCoreHelper;
import models.origo.core.Alias;
import models.origo.core.BasicPage;
import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity(name = "pageIdNavigation")
@Table(name = "navigation_page_id")
public class PageIdNavigation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Constraints.Required
    @Column(unique = true)
    public String identifier;

    @Constraints.Required
    public String pageId;

    public String getLink() {
        Collection<Alias> aliases = Alias.findWithPageId(pageId);
        if (aliases == null || aliases.isEmpty()) {
            if (SettingsCoreHelper.getStartPage().equals(pageId)) {
                return SettingsCoreHelper.getBaseUrl();
            }
            return SettingsCoreHelper.getBaseUrl() + BasicPage.findCurrentVersion(pageId, new Date()).getNodeId();
        } else {
            Alias alias = aliases.iterator().next();
            return SettingsCoreHelper.getBaseUrl() + alias.path;
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

    public PageIdNavigation save() {
        JPA.em().persist(this);
        return this;
    }
}

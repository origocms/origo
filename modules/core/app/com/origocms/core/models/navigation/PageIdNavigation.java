package com.origocms.core.models.navigation;

import com.origocms.core.helpers.SettingsHelper;
import com.origocms.core.models.Alias;
import com.origocms.core.models.BasicPage;
import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity(name="pageIdNavigation")
@Table(name="navigation_page_id")
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
            if (SettingsHelper.Core.getStartPage().equals(pageId)) {
                return SettingsHelper.Core.getBaseUrl();
            }
            return SettingsHelper.Core.getBaseUrl() + BasicPage.findCurrentVersion(pageId, new Date()).getNodeId();
        } else {
            Alias alias = aliases.iterator().next();
            return SettingsHelper.Core.getBaseUrl() + alias.path;
        }
    }

    public static PageIdNavigation findWithIdentifier(String identifier) {
        return JPA.em().find(PageIdNavigation.class, identifier);
    }
}

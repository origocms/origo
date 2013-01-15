package models.origo.core.navigation;

import main.origo.core.helpers.CoreSettingsHelper;
import models.origo.core.Alias;
import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.*;

@Entity
@Table(name = "navigation_alias")
public class AliasNavigation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Constraints.Required
    @Column(unique = true)
    public String identifier;

    // TODO: It would be better to have alias point to the id of an Alias instead (but that is really hard with yaml)
    @Constraints.Required
    public String alias;

    @Constraints.Required
    public int weight;

    public String getLink() {
        Alias aliasModel = Alias.findWithPath(alias);
        if (aliasModel != null && CoreSettingsHelper.getStartPage().equals(aliasModel.pageId)) {
            return CoreSettingsHelper.getBaseUrl();
        }
        return CoreSettingsHelper.getBaseUrl() + alias;
    }

    public static AliasNavigation findWithIdentifier(String identifier) {
        try {
            return (AliasNavigation) JPA.em().createQuery("from models.origo.core.navigation.AliasNavigation where identifier=:identifier").
                    setParameter("identifier", identifier).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
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

    public AliasNavigation save() {
        JPA.em().persist(this);
        return this;
    }
}

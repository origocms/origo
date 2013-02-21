package models.origo.core.navigation;

import main.origo.core.helpers.CoreSettingsHelper;
import models.origo.core.Alias;
import models.origo.core.Model;
import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.*;

@Entity
@Table(name = "navigation_alias")
public class AliasNavigation extends Model<AliasNavigation> {

    public static final String TYPE = "models.origo.core.navigation.AliasNavigation";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Constraints.Required
    @Column(unique = true)
    public String identifier;

    @Constraints.Required
    public Long aliasId;

    @Constraints.Required
    public int weight;

    public AliasNavigation() {
        super(TYPE);
    }

    public String getLink() {
        Alias aliasModel = Alias.findWithId(aliasId);
        if (aliasModel != null && CoreSettingsHelper.getStartPage().equals(aliasModel.pageId)) {
            return CoreSettingsHelper.getBaseUrl();
        }
        return CoreSettingsHelper.getBaseUrl() + aliasModel.path;
    }

    public static AliasNavigation findWithAlias(Long aliasId) {
        try {
            return (AliasNavigation) JPA.em().createQuery("from models.origo.core.navigation.AliasNavigation where aliasId=:alias").
                    setParameter("alias", aliasId).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static AliasNavigation findWithIdentifier(String identifier) {
        try {
            return (AliasNavigation) JPA.em().createQuery("from models.origo.core.navigation.AliasNavigation where identifier=:identifier").
                    setParameter("identifier", identifier).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public String toString() {
        return new StringBuilder().
                append("AliasNavigation {").
                append("identifier='").append(identifier).append('\'').
                append(", alias='").append(aliasId).append('\'').
                append('}').
                toString();
    }

}

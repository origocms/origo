package models.origo.core.navigation;

import models.origo.core.Model;
import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.*;

@Entity(name = "exeternalLinkNavigation")
@Table(name = "navigation_external_link")
public class ExternalLinkNavigation extends Model<ExternalLinkNavigation> {

    public static final String TYPE = "models.origo.core.navigation.ExternalLinkNavigation";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Constraints.Required
    @Column(unique = true)
    public String identifier;

    @Constraints.Required
    public String title;

    @Constraints.Required
    public String link;

    public ExternalLinkNavigation() {
        super(TYPE);
    }

    public String getLink() {
        return link;
    }

    public static ExternalLinkNavigation findWithIdentifier(String identifier) {
        try {
            return (ExternalLinkNavigation) JPA.em().createQuery("from models.origo.core.navigation.ExternalLinkNavigation where identifier=:identifier").
                    setParameter("identifier", identifier).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}

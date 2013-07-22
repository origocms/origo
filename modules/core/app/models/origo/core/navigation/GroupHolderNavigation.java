package models.origo.core.navigation;

import models.origo.core.Model;
import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.*;

@Entity(name = "groupHolderNavigation")
@Table(name = "navigation_group_holder")
public class GroupHolderNavigation extends Model<GroupHolderNavigation> {

    public static final String TYPE = "origo.navigation.GroupHolderNavigation";

    public GroupHolderNavigation() {
        super(TYPE);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Constraints.Required
    @Column(unique = true)
    public String identifier;

    @Constraints.Required
    public String title;

    public static GroupHolderNavigation findWithIdentifier(String identifier) {
        try {
            return (GroupHolderNavigation) JPA.em().createQuery("from "+GroupHolderNavigation.class.getName()+" where identifier=:identifier").
                    setParameter("identifier", identifier).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }
}

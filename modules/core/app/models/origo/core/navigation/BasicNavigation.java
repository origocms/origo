package models.origo.core.navigation;

import main.origo.core.Navigation;
import main.origo.core.event.forms.OnCreateEventGenerator;
import main.origo.core.event.forms.OnDeleteEventGenerator;
import main.origo.core.event.forms.OnUpdateEventGenerator;
import models.origo.core.Model;
import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

/**
 * Basic Navigation type. Tree type structure. Provides the types AliasNavigation, ExternalLinkNavigation and PageIdNavigation.
 * Each entity stored represents one of the 3 subtypes. BasicNavigation itself is never used in the UI, only as a common data store for the subtypes.
 *
 * @see AliasNavigation
 * @see ExternalLinkNavigation
 * @see PageIdNavigation
 */
@Entity(name = "basicNavigation")
@Table(name = "navigation_basic")
public class BasicNavigation extends Model<BasicNavigation> implements Navigation<BasicNavigation>, Comparable<BasicNavigation> {

    public static final String TYPE = "models.origo.core.navigation.BasicNavigation";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @ManyToOne
    public BasicNavigation parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    public List<BasicNavigation> children;

    @Constraints.Required
    public String type;

    @Constraints.Required
    public String section;

    @Constraints.Required
    public String referenceId;

    @Constraints.Required
    public int weight;

    public BasicNavigation() {
        super(TYPE);
    }

    @Override
    public String getReferenceId() {
        return referenceId;
    }

    @Override
    public String getSection() {
        return section;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public String type() {
        return type;
    }

    public static BasicNavigation findWithId(long id) {
        try {
            final Query query = JPA.em().createQuery("select bn from models.origo.core.navigation.BasicNavigation bn where bn.id=:id");
            query.setParameter("id", id);
            return (BasicNavigation) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static BasicNavigation findWithReferenceIdentifier(String referenceId) {
        try {
            final Query query = JPA.em().createQuery("select bn from models.origo.core.navigation.BasicNavigation bn where bn.referenceId=:reference");
            query.setParameter("reference", referenceId);
            return (BasicNavigation) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static List<BasicNavigation> findWithSectionWithoutParent(String section) {
        final Query query = JPA.em().createQuery("select bn from models.origo.core.navigation.BasicNavigation bn where bn.section=:section and parent is null");
        query.setParameter("section", section);
        List<BasicNavigation> resultList = query.getResultList();
        Collections.sort(resultList);
        return resultList;
    }

    public static List<BasicNavigation> findAllWithSection(String section) {
        final Query query = JPA.em().createQuery("select bn from models.origo.core.navigation.BasicNavigation bn where bn.section=:section");
        query.setParameter("section", section);
        List<BasicNavigation> resultList = query.getResultList();
        Collections.sort(resultList);
        return resultList;
    }

    public static List<BasicNavigation> findWithSection(String section, BasicNavigation parent) {
        return findWithSection(section, parent.id);
    }

    public static List<BasicNavigation> findWithSection(String section, Long parentId) {
        final Query query = JPA.em().createQuery("select bn from models.origo.core.navigation.BasicNavigation bn where bn.section=:section and bn.parent.id=:parentId");
        query.setParameter("section", section);
        query.setParameter("parentId", parentId);
        List<BasicNavigation> resultList = query.getResultList();
        Collections.sort(resultList);
        return resultList;
    }

    @Override
    public int compareTo(BasicNavigation navigation) {
        return new Integer(weight).compareTo(navigation.weight);
    }

}

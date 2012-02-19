package models.origo.core.navigation;

import origo.core.Navigation;
import play.db.ebean.Model;
import play.data.validation.Constraints.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Collections;
import java.util.List;

@Entity
public class BasicNavigation extends Model implements Navigation<BasicNavigation>, Comparable<BasicNavigation> {

    @Id
    public Long id;

    @ManyToOne
    public BasicNavigation parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    public List<BasicNavigation> children;

    @Required
    public String type;

    @Required
    public String section;

    @Required
    public String referenceId;

    @Required
    public int weight;

    @Override
    public String getReferenceId() {
        return referenceId;
    }

    @Override
    public String getSection() {
        return section;
    }

    @Override
    public BasicNavigation getParent() {
        return parent;
    }

    @Override
    public List<BasicNavigation> getChildren() {
        return children;
    }

    public static Finder<Long, BasicNavigation> find = new Finder<Long, BasicNavigation>(
            Long.class, BasicNavigation.class
    );

    public static List<BasicNavigation> findWithSection(String section) {
        List<BasicNavigation> navigations =
                find.where().eq("section", section).findList();
        Collections.sort(navigations);
        return navigations;
    }

    public static List<BasicNavigation> findWithSection(String section, BasicNavigation parent) {
        List<BasicNavigation> navigations =
                find.where().eq("section", section).
                    eq("parent", parent).findList();
        Collections.sort(navigations);
        return navigations;
    }

    public static List<BasicNavigation> findWithSection(String section, String parentId) {
        List<BasicNavigation> navigations =
                find.where().
                        eq("section", section).
                        eq("parent", parentId).findList();
        Collections.sort(navigations);
        return navigations;
    }

    @Override
    public int compareTo(BasicNavigation navigation) {
        return new Integer(weight).compareTo(navigation.weight);
    }

}

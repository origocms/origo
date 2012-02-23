package models.origo.core.navigation;

import origo.core.Navigation;
import play.db.ebean.Model;
import play.data.validation.Constraints.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class BasicNavigation extends Model implements Navigation<BasicNavigation>, Comparable<BasicNavigation> {

    @Id
    public Long id;

    @ManyToOne
    public BasicNavigation parent;

    @OneToMany(mappedBy = "parent")
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

    public BasicNavigation create(String section, String type, String referenceId) {
        return create(section, null, type, referenceId);
    }
    
    public BasicNavigation create(String section, BasicNavigation parent, String type, String referenceId) {
        BasicNavigation navigation = new BasicNavigation();
        navigation.section = section;
        navigation.parent = parent;
        navigation.type = type;
        navigation.referenceId = referenceId;
        navigation.children = new ArrayList<BasicNavigation>();
        navigation.saveManyToManyAssociations("children");
        navigation.save();
        return this;
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
                find.where().eq("section", section).eq("parent", parent).findList();
        Collections.sort(navigations);
        return navigations;
    }

    public static List<BasicNavigation> findWithSection(String section, Long parentId) {
        return findWithSection(section, BasicNavigation.find.ref(parentId));
    }

    @Override
    public int compareTo(BasicNavigation navigation) {
        return new Integer(weight).compareTo(navigation.weight);
    }

}

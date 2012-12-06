package com.origocms.core.models.navigation;

import com.origocms.core.Navigation;
import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Entity(name="basicNavigation")
@Table(name="navigation_basic")
public class BasicNavigation implements Navigation<BasicNavigation>, Comparable<BasicNavigation> {

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

    public static List<BasicNavigation> findWithSection(String section) {
        final TypedQuery<BasicNavigation> query = JPA.em().createQuery("select bn from models.navigation.BasicNavigation bn where bn.section.id=:section", BasicNavigation.class);
        query.setParameter("section", section);
        List<BasicNavigation> resultList = query.getResultList();
        Collections.sort(resultList);
        return resultList;
    }

    public static List<BasicNavigation> findWithSection(String section, BasicNavigation parent) {

        final TypedQuery<BasicNavigation> query = JPA.em().createQuery("select bn from models.navigation.BasicNavigation bn where bn.section.id=:section and bn.parent=:parent", BasicNavigation.class);
        query.setParameter("section", section);
        query.setParameter("parent", parent);
        List<BasicNavigation> resultList = query.getResultList();
        Collections.sort(resultList);
        return resultList;
    }

    public static List<BasicNavigation> findWithSection(String section, String parentId) {
        final TypedQuery<BasicNavigation> query = JPA.em().createQuery("select bn from models.navigation.BasicNavigation bn where bn.section.id=:section and bn.parent.id=:parentId", BasicNavigation.class);
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

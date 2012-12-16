package models.origo.core;

import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "aliases")
public class Alias {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Constraints.Required
    @Column(unique = true)
    public String path;

    @Constraints.Required
    public String pageId;

    protected Alias() {
    }

    public Alias(String path, String pageId) {
        this.path = path;
        this.pageId = pageId;
    }

    public static Alias findWithId(long id) {
        return JPA.em().find(Alias.class, id);
    }

    @Override
    public String toString() {
        return new StringBuilder().
                append("Alias {").
                append("path='").append(path).append("\', ").
                append("page='").append(pageId).append('\'').
                append('}').
                toString();
    }

    public static Alias findWithPath(String path) {
        try {
            final Query query = JPA.em().createQuery("from models.origo.core.Alias an where an.path=:path");
            query.setParameter("path", path);
            return (Alias) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static List<Alias> findWithPageId(String pageId) {
        try {
            final Query query = JPA.em().createQuery("from models.origo.core.Alias an where an.pageId=:pageId");
            query.setParameter("pageId", pageId);
            return query.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    public static Alias findFirstAliasForPageId(String pageId) {
        Collection<Alias> aliases = Alias.findWithPageId(pageId);
        if (aliases.isEmpty()) {
            return null;
        }
        return aliases.iterator().next();
    }

    public Alias save() {
        JPA.em().persist(this);
        return this;
    }
}

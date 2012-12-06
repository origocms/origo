package com.origocms.core.models;

import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.Collection;

@Entity(name = "alias")
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

    public Alias(String path, String pageId) {
        this.path = path;
        this.pageId = pageId;
    }

    public static Alias findWithId(long id) {
        return JPA.em().find(Alias.class, id);
    }

    public static Alias findWithPath(String path) {
        final TypedQuery<Alias> query = JPA.em().createQuery("select an from models.Alias an where an.path=:path", Alias.class);
        query.setParameter("path", path);
        return query.getSingleResult();
    }

    public static Collection<Alias> findWithPageId(String pageId) {
        final TypedQuery<Alias> query = JPA.em().createQuery("select an from models.Alias an where an.opageId=:pageId", Alias.class);
        query.setParameter("pageId", pageId);
        return query.getResultList();
    }

    public static Alias findFirstAliasForPageId(String pageId) {
        Collection<Alias> aliases = Alias.findWithPageId(pageId);
        if (aliases == null || aliases.isEmpty()) {
            return null;
        } else {
            return aliases.iterator().next();
        }
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
}

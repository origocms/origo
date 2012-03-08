package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Collection;

import play.data.validation.Constraints.*;

@Entity(name = "Alias")
public class Alias extends Model {

    @Id
    public Long id;

    @Required
    //@Unique
    public String path;

    @Required
    public String pageId;

    public Alias(String path, String pageId) {
        this.path = path;
        this.pageId = pageId;
    }

    public static Finder<Long, Alias> find = new Finder<Long, Alias>(
            Long.class, Alias.class
    );

    public static Alias findWithId(long id) {
        return find.byId(id);
    }

    public static Alias findWithPath(String path) {
        return find.where().eq("path", path).findUnique();
    }

    public static Collection<Alias> findWithPageId(String pageId) {
        return find.where().eq("pageId", pageId).findList();
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

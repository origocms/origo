package models.origo.core;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.util.UUID;

@Entity
public class Content extends Model {

    @Id
    public Long id;

    @Required
    //@Unique
    public String identifier;

    @Required
    @Column(name = "content")
    @Lob
    public String value;

    public Content() {
        this.identifier = UUID.randomUUID().toString();
    }

    public static Finder<Long, Content> find = new Finder<Long, Content>(
            Long.class, Content.class
    );

    public static Content findWithIdentifier(String identifier) {
        return find.where().eq("identifier", identifier).findUnique();
    }

}

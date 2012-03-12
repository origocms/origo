package models.origo.core;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "Meta")
public class Meta extends Model {

    @Id
    public Long id;

    @Required
    public String nodeId;

    @Required
    public Integer version;

    @Required
    public String referenceId;

    @Required
    public Integer weight;

    @Required
    public String region;

    public static Finder<Long, Meta> find = new Finder<Long, Meta>(
            Long.class, Meta.class
    );

    public static Meta findWithNodeIdAndSpecificVersion(String nodeId, Integer version, String referenceId) {
        return find.
                where().
                eq("nodeId", nodeId).
                eq("version", version).
                eq("referenceId", referenceId).
                findUnique();
    }

    // TODO: Make this loaded from the database instead
    public static Meta defaultMeta() {
        Meta meta = new Meta();
        meta.region = "main";
        meta.weight = 100;
        return meta;
    }
}

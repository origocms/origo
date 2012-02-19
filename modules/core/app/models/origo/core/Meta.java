package models.origo.core;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Meta extends Model {

    @Id
    public Long id;

    @Required
    public String nodeId;

    // TODO: Should only have to be Integer but because of defect #521 in play that doesn't work. Should be fixed in 1.3 (2.0?)
    @Required
    public Long version;

    @Required
    public String referenceId;

    // TODO: Should only have to be Integer but because of defect #521 in play that doesn't work. Should be fixed in 1.3 (2.0?)
    @Required
    public Long weight;

    @Required
    public String region;

    public static Finder<Long, Meta> find = new Finder<Long, Meta>(
            Long.class, Meta.class
    );

    public static Meta findWithNodeIdAndSpecificVersion(String nodeId, Long version, String referenceId) {
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
        meta.weight = 100l;
        return meta;
    }
}

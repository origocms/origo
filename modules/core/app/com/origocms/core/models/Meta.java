package com.origocms.core.models;

import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.*;

@Entity(name="meta")
@Table(name="meta")
public class Meta {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Constraints.Required
    public String nodeId;

    // TODO: Should only have to be Integer but because of defect #521 in play that doesn't work. Should be fixed in 1.3 (2.0?)
    @Constraints.Required
    public Long version;

    @Constraints.Required
    public String referenceId;

    // TODO: Should only have to be Integer but because of defect #521 in play that doesn't work. Should be fixed in 1.3 (2.0?)
    @Constraints.Required
    public Long weight;

    @Constraints.Required
    public String region;

    public static Meta findWithNodeIdAndSpecificVersion(String nodeId, Integer version, String referenceId) {
        String queryString = "select distinct m from com.origocms.core.models.Meta m " +
                "where m.nodeId = :nodeId and m.version = :version and m.referenceId = :referenceId";
        final TypedQuery<Meta> query = JPA.em().createQuery(queryString, Meta.class);
        return query.getSingleResult();
    }

    // TODO: Make this loaded from the database instead
    public static Meta defaultMeta() {
        Meta meta = new Meta();
        meta.region = "main";
        meta.weight = 100l;
        return meta;
    }
}

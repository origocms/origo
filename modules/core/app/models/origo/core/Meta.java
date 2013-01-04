package models.origo.core;

import play.Logger;
import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.*;

@Entity
@Table(name = "meta")
public class Meta {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Constraints.Required
    public String nodeId;

    @Constraints.Required
    public Integer version;

    @Constraints.Required
    public String referenceId;

    @Constraints.Required
    public Integer weight;

    @Constraints.Required
    public String region;

    public static Meta findWithNodeIdAndSpecificVersion(String nodeId, Integer version, String referenceId) {
        try {
            String queryString = "select distinct m from models.origo.core.Meta m " +
                    "where m.nodeId = :nodeId and m.version = :version and m.referenceId = :referenceId";
            final Query query = JPA.em().createQuery(queryString);
            query.setParameter("version", version).setParameter("nodeId", nodeId).setParameter("referenceId", referenceId);
            return (Meta) query.getSingleResult();
        } catch (NoResultException e) {
            Logger.trace("No Meta found for node '" + nodeId + "' version '" + version + "' with reference '" + referenceId + "'");
            return null;
        }
    }

    // TODO: Make this loaded from the database instead
    public static Meta defaultMeta() {
        Meta meta = new Meta();
        meta.region = "main";
        meta.weight = 100;
        return meta;
    }

    public Meta save() {
        JPA.em().merge(this);
        return this;
    }

}

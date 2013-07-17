package models.origo.core.navigation;

import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.annotations.Core;
import main.origo.core.event.ProvidesEventGenerator;
import main.origo.core.helpers.CoreSettingsHelper;
import models.origo.core.Alias;
import models.origo.core.BasicPage;
import models.origo.core.Model;
import models.origo.core.RootNode;
import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.Collection;

@Entity(name = "pageIdNavigation")
@Table(name = "navigation_page_id")
public class InternalPageIdNavigation extends Model<InternalPageIdNavigation> {

    public static final String TYPE = "origo.navigation.pageid";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Constraints.Required
    @Column(unique = true)
    public String identifier;

    @Constraints.Required
    public String pageId;

    public InternalPageIdNavigation() {
        super(TYPE);
    }

    public String getLink() throws NodeLoadException, ModuleException {
        if (CoreSettingsHelper.getStartPage().equals(pageId)) {
            return CoreSettingsHelper.getBaseUrl();
        }
        Collection<Alias> aliases = Alias.findWithPageId(pageId);
        return getAliasUrl(aliases);
    }

    private String getAliasUrl(Collection<Alias> aliases) throws NodeLoadException, ModuleException {
        if (aliases == null || aliases.isEmpty()) {
            RootNode rootNode = RootNode.findLatestVersionWithNodeId(pageId);
            if (rootNode == null) {
                throw new NodeLoadException(identifier, "The navigation with id='"+identifier+"' references pageId='"+pageId+"' which doesn't exist");
            }
            Node node = ProvidesEventGenerator.triggerInterceptor(rootNode, Core.Type.NODE, rootNode.nodeType);
            if (node == null) {
                throw new NodeLoadException(identifier, "The node with id='"+rootNode.nodeId+"' is not associated with an instance of type '"+rootNode.nodeType+"'");
            }
            return CoreSettingsHelper.getBaseUrl() + node.getNodeId();
        } else {
            Alias alias = aliases.iterator().next();
            return CoreSettingsHelper.getBaseUrl() + alias.path;
        }
    }


    public static InternalPageIdNavigation findWithIdentifier(String identifier) {
        try {
            return (InternalPageIdNavigation) JPA.em().createQuery("from "+InternalPageIdNavigation.class.getName()+" as n where n.identifier=:identifier").
                    setParameter("identifier", identifier).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}

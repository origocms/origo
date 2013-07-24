package controllers.origo.admin;

import com.google.common.collect.Lists;
import main.origo.core.ModuleException;
import main.origo.core.Node;
import main.origo.core.NodeLoadException;
import main.origo.core.actions.ContextAware;
import main.origo.core.annotations.Core;
import main.origo.core.event.ProvidesEventGenerator;
import models.origo.core.RootNode;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

@Transactional(readOnly = true)
public class InternalAssets extends Controller {

    @ContextAware
    public static Result listPages() throws ModuleException, NodeLoadException {

        List<RootNode> rootNodes = RootNode.findCurrentVersions();

        List<Node> nodes = Lists.newArrayList();
        for (RootNode rootNode : rootNodes) {
            Node node = ProvidesEventGenerator.triggerInterceptor(rootNode, Core.Type.NODE, rootNode.nodeType());
            nodes.add(node);
        }

        return ok(Json.toJson(nodes));
    }

}

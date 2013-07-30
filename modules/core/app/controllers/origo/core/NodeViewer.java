package controllers.origo.core;

import models.origo.core.RootNode;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.origo.core.node.list;
import views.html.origo.core.node.show;

import java.util.List;

public class NodeViewer extends Controller {

    @Transactional
    public static Result nodes() {

        //Load NodeModel
        List<RootNode> nodes = RootNode.findCurrentPublishedVersions();
        return ok(list.render(nodes));
    }

    @Transactional
    public static Result nodeCurrent(String nodeId) {

        //Load NodeModel
        RootNode node = RootNode.findLatestPublishedVersionWithNodeId(nodeId);
        return ok(show.render(node));
    }

    @Transactional
    public static Result nodeVersions(String nodeId) {

        List<RootNode> nodes = RootNode.findAllVersionsWithNodeId(nodeId);

        return ok(list.render(nodes));
    }

    @Transactional
    public static Result nodeVersion(String nodeId, Integer version) {

        RootNode node = RootNode.findWithNodeIdAndSpecificVersion(nodeId, version);

        return ok(show.render(node));
    }

}

package controllers.origo.core;

import models.origo.core.RootNode;
import org.springframework.stereotype.Component;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Date;
import java.util.List;

public class Node extends Controller {

    //@Get("/node")
    @Transactional
    public static Result node() {

        //Load NodeModel
        List<RootNode> nodes = RootNode.findCurrentPublishedVersions();
        return ok(views.html.origo.core.node.list.render(nodes));
    }

    //@Get("/node/{nodeId}")
    @Transactional
    public static Result nodeCurrent(String nodeId) {

        //Load NodeModel
        RootNode node = RootNode.findLatestPublishedVersionWithNodeId(nodeId);
        return ok(views.html.origo.core.node.show.render(node));
    }

    //@Get("/node/{nodeId}/all")
    @Transactional
    public static Result nodeVersions(String nodeId) {

        List<RootNode> nodes = RootNode.findAllVersionsWithNodeId(nodeId);

        return ok(views.html.origo.core.node.list.render(nodes));
    }

    //@Get("/node/{nodeId}/{<[0-9]+>version}")
    @Transactional
    public static Result nodeVersion(String nodeId, Integer version) {

        RootNode node = RootNode.findWithNodeIdAndSpecificVersion(nodeId, version);

        return ok(views.html.origo.core.node.show.render(node));
    }

}

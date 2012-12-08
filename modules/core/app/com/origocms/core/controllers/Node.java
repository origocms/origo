package com.origocms.core.controllers;

import com.origocms.core.models.RootNode;
import org.springframework.stereotype.Component;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class Node extends Controller {

    //@Get("/node")
    @Transactional
    public Result node() {

        //Load NodeModel
        List<RootNode> nodes = RootNode.findAllCurrentVersions(new Date());

        return ok(com.origocms.core.views.html.node.render(nodes));
    }

    //@Get("/node/{nodeId}")
    @Transactional
    public Result nodeCurrent(String nodeId) {

        //Load NodeModel
        RootNode node = RootNode.findLatestPublishedVersionWithNodeId(nodeId, new Date());

        return ok(com.origocms.core.views.html.node.render(Collections.singletonList(node)));
    }

    //@Get("/node/{nodeId}/all")
    @Transactional
    public Result nodeVersions(String nodeId) {

        List<RootNode> nodes = RootNode.findAllVersionsWithNodeId(nodeId);

        return ok(com.origocms.core.views.html.node.render(nodes));
    }

    //@Get("/node/{nodeId}/{<[0-9]+>version}")
    @Transactional
    public Result nodeVersion(String nodeId, Integer version) {

        RootNode node = RootNode.findWithNodeIdAndSpecificVersion(nodeId, version);

        return ok(com.origocms.core.views.html.node.render(Collections.singletonList(node)));
    }

}

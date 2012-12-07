package com.origocms.controllers;

import com.origocms.core.NodeLoadException;
import com.origocms.core.controllers.CoreLoader;
import com.origocms.core.ui.RenderedNode;
import com.origocms.views.html.index;
import org.springframework.stereotype.Component;
import play.mvc.Controller;
import play.mvc.Result;

@Component
public class Application extends Controller {

    public Result index() {
        return CoreLoader.getStartPage();
    }

    public Result page(String identifier) {
        return CoreLoader.getPage(identifier);
    }

}

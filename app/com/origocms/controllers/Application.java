package com.origocms.controllers;

import com.origocms.core.controllers.CoreLoader;
import org.springframework.stereotype.Component;
import play.mvc.Controller;
import play.mvc.Result;

@Component
public class Application extends Controller {

    @play.db.jpa.Transactional
    public Result index() {
        return CoreLoader.getStartPage();
    }

    @play.db.jpa.Transactional
    public Result page(String identifier) {
        return CoreLoader.getPage(identifier);
    }

}

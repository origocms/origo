package com.origocms.controllers;

import com.origocms.views.html.index;
import org.springframework.stereotype.Component;
import play.mvc.Controller;
import play.mvc.Result;

@Component
public class Application extends Controller {

    public Result index() {
        return ok(index.render("Your new application is ready."));
    }

}

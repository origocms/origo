package com.origocms.core.controllers;

import org.springframework.stereotype.Component;
import play.*;
import play.mvc.*;

import com.origocms.core.views.html.*;

@Component
public class Core extends Controller {
  
    public Result index() {
        return ok(index.render("Your new application is ready."));
    }
  
}

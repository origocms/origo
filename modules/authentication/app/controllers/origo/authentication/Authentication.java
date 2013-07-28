package controllers.origo.authentication;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.origo.authentication.login;

public class Authentication extends Controller {

    public static Result login() {
        return ok(login.render());
    }

}
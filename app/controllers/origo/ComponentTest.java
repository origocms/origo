package controllers.origo;

import main.origo.core.actions.ComponentWrapper;
import main.origo.core.actions.ContextAware;
import play.data.Form;
import play.data.validation.Constraints;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.origo.component_test.index;

public class ComponentTest extends Controller {

    @Transactional
    @ContextAware
    @ComponentWrapper
    public static Result index() {
        return ok(index.render("Hello", createForm("Hello")));
    }

    @Transactional
    @ContextAware
    @ComponentWrapper
    public static Result submit() {

        Form<Message> form = Form.form(Message.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(index.render("Hello", form));
        }
        String message = form.get().text;

        return ok(index.render(message, form));
    }

    public static Form<Message> createForm(String value) {
        Message message = new Message();
        message.text = value;
        return Form.form(Message.class).fill(message);
    }

    public static class Message {

        @Constraints.Required
        public String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

}

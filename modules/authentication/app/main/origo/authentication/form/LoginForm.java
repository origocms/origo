package main.origo.authentication.form;

import main.origo.core.ModuleException;
import main.origo.core.NodeLoadException;
import main.origo.core.security.SecurityEventGenerator;
import play.data.validation.Constraints;

public class LoginForm {

    @Constraints.Required
    private String username;

    @Constraints.Required
    private String password;

    private String path;

    public String validate() throws ModuleException, NodeLoadException {
        if(authenticate(username,password) == null) {
            return "Invalid email or password";
        }
        return null;
    }

    private Object authenticate(String username, String password) throws NodeLoadException, ModuleException {
        return SecurityEventGenerator.triggerValidateInterceptor(username, password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

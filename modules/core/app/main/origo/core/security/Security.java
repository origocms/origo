package main.origo.core.security;

public class Security {

    private Security() {
    }

    public static class Params {
        public static final String AUTH_HANDLER = "authorization_handler";
        public static final String AUTH_META = "authorization_meta";
        public static final String AUTH_PATH = "authorization_path";
        public static final String AUTH_USERNAME = "authentication_username";
        public static final String AUTH_PASSWORD = "authentication_password";
        public static final String AUTH_USER = "authentication_user";
    }

    public static class Types {
        public static final String RESOURCE = "authorization.type.resource";
    }

}

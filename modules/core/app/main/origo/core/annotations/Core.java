package main.origo.core.annotations;

public class Core {

    private Core() {}

    public class Type {
        public static final String NODE = "node";
        public static final String NAVIGATION = "navigation";
        public static final String NAVIGATION_ITEM = "navigation_item";
        public static final String FORM = "form";

        public static final String USER = "user";
    }

    public class With {
        public static final String CONTENT_PAGE = "content";
        public static final String EDITOR = "richtexteditor";

        public static final String AUTHORIZATION_SUBJECT = "authorization_subject";
        public static final String AUTHORIZATION_FAILURE = "authorization_failure";
        public static final String AUTHORIZATION_CHECK = "authorization_check";

        public static final String AUTHENTICATION_CHECK = "authentication_check";
    }
}

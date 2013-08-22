package main.origo.core.interceptors;

import main.origo.core.actions.Component;
import main.origo.core.annotations.OnLoad;
import models.origo.core.Content;

public class ComponentInterceptor {

    @OnLoad(with = Content.TYPE)
    public static void onContentInsert(OnLoad.Context context) {

        Content content = (Content) context.args.get("content");

        if (content.value.contains(Component.COMPONENT_MARKER)) {
            Component component = Component.getWrappedComponent();

        }

    }

}

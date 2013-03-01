package main.origo.core.annotations;

import com.google.common.collect.Maps;
import main.origo.core.Node;
import main.origo.core.event.NodeContext;
import play.data.DynamicForm;

import java.util.Map;

public abstract class AbstractContext {

    public final Node node;
    public final Map<String, Object> args;
    public final Map<String, Object> attributes;

    protected AbstractContext(Node node, Map<String, Object> args) {
        this.node = node;
        this.args = Maps.newHashMap();
        this.args.putAll(args);
        this.args.putAll(DynamicForm.form().bindFromRequest().data());
        this.attributes = NodeContext.current().attributes;
    }

}

package main.origo.admin.interceptors;

import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.forms.OnSubmit;
import main.origo.core.helpers.forms.FormHelper;
import models.origo.core.RootNode;
import play.data.DynamicForm;

import java.util.Map;

@Interceptor
public class RootNodeAdminProvider {

    @OnSubmit(weight = 10) // TODO: Create a RootNodeForm and use validation
    public static Boolean storeNode() {
        DynamicForm form = DynamicForm.form().bindFromRequest();
        Map<String, String> data = form.data();
        String nodeId = FormHelper.getNodeId(data);
        Integer version = FormHelper.getNodeVersion(data);

        RootNode oldNodeVersion = RootNode.findLatestVersionWithNodeId(nodeId);
        if (oldNodeVersion != null && !oldNodeVersion.version().equals(version)) {
            throw new RuntimeException("Root node with id='" + nodeId + "' and version ='" + version + "' has a newer version stored.");
        }
        return true;
    }

}

package main.origo.admin.interceptors;

import main.origo.core.annotations.Interceptor;
import main.origo.core.annotations.forms.OnSubmit;
import main.origo.core.helpers.forms.FormHelper;
import models.origo.core.RootNode;
import play.data.DynamicForm;

import java.util.Map;

@Interceptor
public class RootNodeAdminProvider {

    @OnSubmit // TODO: Probably should be moved to validation when that is ready
    public static void storeNode(OnSubmit.Context context) {
        DynamicForm form = DynamicForm.form().bindFromRequest();
        Map<String, String> data = form.data();
        String nodeId = FormHelper.getNodeId(data);
        Integer version = FormHelper.getNodeVersion(data);
        RootNode oldRootNode = RootNode.findWithNodeIdAndSpecificVersion(nodeId, version);
        if (oldRootNode == null) {
            throw new RuntimeException("Root node with id='" + nodeId + "' does not exist");
        }

        RootNode oldNodeVersion = RootNode.findLatestVersionWithNodeId(nodeId);
        if (!oldNodeVersion.version.equals(version)) {
            throw new RuntimeException("Root node with id='" + nodeId + "' and version ='" + version + "' has a newer version stored.");
        }
    }

}

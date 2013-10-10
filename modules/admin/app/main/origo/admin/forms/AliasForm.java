package main.origo.admin.forms;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import play.data.validation.ValidationError;

import java.util.List;

public class AliasForm {

    public boolean use_alias;

    public String alias;

    public List<ValidationError>  validate() {
        List<ValidationError> errors = Lists.newArrayList();
        if (use_alias && StringUtils.isBlank(alias)) {
            errors.add(new ValidationError("alias", "error.required"));
        }
        return errors.isEmpty() ? null : errors;
    }
}

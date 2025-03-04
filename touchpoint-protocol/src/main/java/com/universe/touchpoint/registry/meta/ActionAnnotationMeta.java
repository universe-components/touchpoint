package com.universe.touchpoint.registry.meta;

import com.universe.touchpoint.config.task.ActionDependency;
import com.universe.touchpoint.registry.BaseAnnotationMeta;
import com.universe.touchpoint.utils.StringUtils;
import java.util.Map;

public class ActionAnnotationMeta extends BaseAnnotationMeta {

    public ActionAnnotationMeta(Class<?> importingClass, Map<String, Object> attributes) {
        super(importingClass, attributes);
    }

    public ActionDependency getActionDependency() {
        String actionName = (String) attributes.get("name");
        String[] toActions = (String[]) attributes.get("toActions");
        ActionDependency actionDependency = new ActionDependency(actionName);
        assert toActions != null;
        actionDependency.setToActions(StringUtils.convert(toActions));
        return actionDependency;
    }

}

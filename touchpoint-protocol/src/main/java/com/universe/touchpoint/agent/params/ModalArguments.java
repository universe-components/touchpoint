package com.universe.touchpoint.agent.params;

import com.universe.touchpoint.context.TouchPoint;
import java.util.List;

public class ModalArguments extends TouchPoint {

    private List<Object> modalArgs;

    public ModalArguments(List<Object> modalArgs) {
        this.modalArgs = modalArgs;
    }

    public List<Object> getModalArgs() {
        return modalArgs;
    }

    public void setModalArgs(List<Object> modalArgs) {
        this.modalArgs = modalArgs;
    }

}

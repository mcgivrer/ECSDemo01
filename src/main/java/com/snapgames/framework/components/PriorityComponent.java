package com.snapgames.framework.components;

public class PriorityComponent implements Component {
    private int priority = 0;

    public PriorityComponent() {

    }

    public PriorityComponent setPriority(int p) {
        this.priority = p;
        return this;
    }

    public int getPriority() {
        return this.priority;
    }


}

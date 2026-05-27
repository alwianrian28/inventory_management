package com.inventory.model;

/**
 *
 * @author Dearclaudia
 */
public class StatusItem {

    private final String label;
    private final boolean value;

    public StatusItem(String label, boolean value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return label;
    }
}

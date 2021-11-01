package com.np.bucketmanager.models;

public abstract class Rule<T> {
    private int ruleId;
    private T value;
    public abstract T getValue();
}

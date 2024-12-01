package me.rui.fdyzrank.model;

import lombok.Data;

@Data
public class Class {
    private long id;
    private Grade grade;
    private int order;

    public enum Grade {
        ONE,TWO,THREE
    }
}

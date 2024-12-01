package me.rui.fdyzrank.model;

import lombok.Data;

@Data
public class Teacher {
    private long id;
    private boolean isMale;
    private String name;
    private String subject;
    private long classId;
    private int score;
}

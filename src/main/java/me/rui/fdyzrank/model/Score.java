package me.rui.fdyzrank.model;

import lombok.Data;

@Data
public class Score {
    private long userId;
    private long teacherId;
    private int score;
}

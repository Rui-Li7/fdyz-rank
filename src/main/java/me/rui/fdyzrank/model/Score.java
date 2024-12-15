package me.rui.fdyzrank.model;

import com.mybatisflex.annotation.Table;
import lombok.Data;

@Data
@Table("score")
public class Score {
    private long userId;
    private long teacherId;
    private int score;
}

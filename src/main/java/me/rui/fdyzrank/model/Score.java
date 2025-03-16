package me.rui.fdyzrank.model;

import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Table("score")
@AllArgsConstructor
@RequiredArgsConstructor
public class Score {
    private final long userId;
    private final long teacherId;
    private final int score;
    private boolean verified = false;
}

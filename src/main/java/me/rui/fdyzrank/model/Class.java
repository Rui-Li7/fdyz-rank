package me.rui.fdyzrank.model;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;

@Data
@Table("class")
public class Class {
    @Id
    private long id;
    private Grade grade;
    private int order;

    public enum Grade {
        ONE,TWO,THREE
    }
}

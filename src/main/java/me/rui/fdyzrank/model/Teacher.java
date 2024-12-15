package me.rui.fdyzrank.model;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.Data;

@Data
@Table("teacher")
public class Teacher {
    @Id
    private long id;
    private long globalId;
    private boolean isMale;
    private String name;
    private String subject;
    private long classId;
    private int score;
}

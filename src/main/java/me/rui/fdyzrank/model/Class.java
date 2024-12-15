package me.rui.fdyzrank.model;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.Data;

@Data
@Table("class")
public class Class {
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private long id;
    private Grade grade;
    private int order;

    public enum Grade {
        ONE,TWO,THREE
    }
}

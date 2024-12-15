package me.rui.fdyzrank.model;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.Data;

@Data
@Table("user")
public class User {
    @Id
    private long id;
    private String nickname;
}

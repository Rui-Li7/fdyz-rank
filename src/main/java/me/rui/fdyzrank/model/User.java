package me.rui.fdyzrank.model;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;

@Data
@Table("user")
public class User {
    @Id
    private long id;
    private String nickname;
}

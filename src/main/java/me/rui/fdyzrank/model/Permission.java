package me.rui.fdyzrank.model;

import com.mybatisflex.annotation.Table;
import lombok.Data;

@Data
@Table("permission")
public class Permission {
    private long userId;
    private String permission;
}

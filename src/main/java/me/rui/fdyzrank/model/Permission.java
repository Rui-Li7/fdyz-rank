package me.rui.fdyzrank.model;

import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Table("permission")
@AllArgsConstructor
public class Permission {
    private long userId;
    private String permission;
}

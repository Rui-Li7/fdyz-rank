package me.rui.fdyzrank.manager;

import cn.dev33.satoken.stp.StpInterface;
import me.rui.fdyzrank.mapper.PermissionMapper;
import me.rui.fdyzrank.mapper.UserMapper;
import me.rui.fdyzrank.model.Permission;
import me.rui.fdyzrank.model.User;
import me.rui.fdyzrank.model.table.Tables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
public class PermissionManager implements StpInterface {
    private final PermissionMapper permissionMapper;
    private final UserMapper userMapper;

    @Autowired
    public PermissionManager(PermissionMapper permissionMapper, UserMapper userMapper) {
        this.permissionMapper = permissionMapper;
        this.userMapper = userMapper;
    }

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        Permission permission = new Permission(Long.parseLong((String) loginId), "user.*");
        List<Permission> permissions = permissionMapper.selectListByCondition(Tables.PERMISSION.USER_ID.eq(loginId));
        if (!permissions.contains(permission)) {
            permissions.add(permission);
        }
        return permissions.stream().map(Permission::getPermission).toList();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        User user = userMapper.selectOneById((Serializable) loginId);
        return List.of(user.getRole());
    }
}

package me.rui.fdyzrank.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.rui.fdyzrank.FdyzRankConfig;
import me.rui.fdyzrank.mapper.UserMapper;
import me.rui.fdyzrank.model.User;
import me.rui.fdyzrank.model.table.Tables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.FileAlreadyExistsException;
import java.security.InvalidParameterException;
import java.security.KeyException;
import java.util.Objects;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserRestController {
    private final UserMapper userMapper;
    private final FdyzRankConfig globalConfig;

    @SaIgnore
    @SneakyThrows
    @RequestMapping("/register")
    public JSONObject register(@RequestParam String answer) {
        JSONObject result = new JSONObject();

        if (!Objects.equals(globalConfig.getAnswer(), answer)) {
            throw new KeyException("回答错误，无法进入网站");
        }

        if (globalConfig.getAdminKey().equals(answer)) {
            result.put("success", true);
            result.put("msg", "进入管理员账号");
            StpUtil.login(globalConfig.getAdminId());
            return result;
        }

        User user = new User();
        user.setId(IdUtil.getSnowflakeNextId());
        user.setNickname(RandomUtil.randomString(10));
        user.setRole("user");
        userMapper.insert(user);
        result.put("success", true);
        result.put("msg", "回答正确");
        result.put("body", user);
        StpUtil.login(user.getId());

        return result;
    }

    @SaCheckLogin
    @SneakyThrows
    @RequestMapping("/changeNickname")
    public JSONObject changeNickname(@RequestParam String newName) {
        JSONObject result = new JSONObject();

        if (newName == null || newName.isEmpty()) {
            throw new InvalidParameterException("昵称不能为空");
        }

        if (userMapper.selectOneByCondition(Tables.USER.NICKNAME.eq(newName)) != null) {
            throw new FileAlreadyExistsException("昵称重复");
        }

        if (newName.length() > 10) {
            result.put("success", false);
            result.put("msg","长度超过10");
            return result;
        }

        User user = userMapper.selectOneById(StpUtil.getLoginIdAsLong());
        user.setNickname(newName);
        userMapper.update(user);
        result.put("success", true);
        result.put("msg", "修改成功");
        return result;
    }
}

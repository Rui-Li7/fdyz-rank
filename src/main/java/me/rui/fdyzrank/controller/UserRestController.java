package me.rui.fdyzrank.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson2.JSONObject;
import me.rui.fdyzrank.FdyzRankConfig;
import me.rui.fdyzrank.mapper.UserMapper;
import me.rui.fdyzrank.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserRestController {
    private final UserMapper userMapper;
    private final FdyzRankConfig globalConfig;

    @Autowired
    public UserRestController(UserMapper userMapper, FdyzRankConfig globalConfig) {
        this.userMapper = userMapper;
        this.globalConfig = globalConfig;
    }

    @RequestMapping("/register")
    public JSONObject register(@RequestParam String answer) {
        JSONObject result = new JSONObject();

        if (!Objects.equals(globalConfig.getAnswer(), answer)) {
            result.put("success", false);
            result.put("msg", "回答错误，无法进入网站");
            result.put("body", new JSONObject());
            return result;
        }

        User user = new User();
        user.setId(IdUtil.getSnowflakeNextId());
        user.setNickname(RandomUtil.randomString(10));
        userMapper.insert(user);
        result.put("success", true);
        result.put("msg", "回答正确");
        result.put("body", user);

        return result;
    }
}

package me.rui.fdyzrank.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaIgnore;
import com.alibaba.fastjson2.JSONObject;
import lombok.SneakyThrows;
import me.rui.fdyzrank.FdyzRankConfig;
import me.rui.fdyzrank.mapper.ScoreMapper;
import me.rui.fdyzrank.mapper.TeacherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidParameterException;

@RestController
@RequestMapping("/score")
public class ScoreRestController {
    private final FdyzRankConfig fdyzRankConfig;
    private final ScoreMapper scoreMapper;
    private final TeacherMapper teacherMapper;

    @Autowired
    public ScoreRestController(FdyzRankConfig fdyzRankConfig, ScoreMapper scoreMapper, TeacherMapper teacherMapper) {
        this.fdyzRankConfig = fdyzRankConfig;
        this.scoreMapper = scoreMapper;
        this.teacherMapper = teacherMapper;
    }

    @SaIgnore
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @SneakyThrows
    @RequestMapping("/update")
    public JSONObject update(@RequestParam String updateKey) {

        if (!fdyzRankConfig.getUpdateKey().equals(updateKey)) {
            throw new InvalidParameterException("密钥错误");
        }


    }
}

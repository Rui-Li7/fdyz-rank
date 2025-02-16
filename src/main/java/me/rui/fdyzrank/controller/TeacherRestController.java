package me.rui.fdyzrank.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSONObject;
import lombok.SneakyThrows;
import me.rui.fdyzrank.mapper.ScoreMapper;
import me.rui.fdyzrank.mapper.TeacherMapper;
import me.rui.fdyzrank.model.Score;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.security.InvalidParameterException;

@RestController
@RequestMapping("/teacher")
public class TeacherRestController {
    private final TeacherMapper teacherMapper;
    private final ScoreMapper scoreMapper;

    @Autowired
    public TeacherRestController(TeacherMapper teacherMapper, ScoreMapper scoreMapper) {
        this.teacherMapper = teacherMapper;
        this.scoreMapper = scoreMapper;
    }

    @SaCheckLogin
    @SneakyThrows
    @SaCheckPermission("user.vote")
    @RequestMapping("/vote/{teacherId}")
    public JSONObject vote(@PathVariable long teacherId, @RequestParam int scoreNum) {

        if (scoreNum < 0 || scoreNum > 10) {
            throw new InvalidParameterException("所填分数超出范围 {分数范围：[0, 10]");
        }

        if (teacherMapper.selectOneById(teacherId) == null) {
            throw new FileNotFoundException("不存在该老师");
        }

        JSONObject result = new JSONObject();
        Score score = new Score();
        score.setTeacherId(teacherId);
        score.setScore(scoreNum);
        score.setUserId(StpUtil.getLoginIdAsLong());
        scoreMapper.insert(score);
        result.put("success", true);
        result.put("msg", "投票成功");
        return result;
    }
}

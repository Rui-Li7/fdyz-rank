package me.rui.fdyzrank.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSONObject;
import me.rui.fdyzrank.mapper.ScoreMapper;
import me.rui.fdyzrank.mapper.TeacherMapper;
import me.rui.fdyzrank.model.Score;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping("/vote/{teacherId}")
    public JSONObject vote(@PathVariable long teacherId, @RequestParam int scoreNum) {
        JSONObject result = new JSONObject();

        if (!StpUtil.isLogin()) {
            result.put("success", false);
            result.put("msg", "未登录");
            return result;
        }

        if (!StpUtil.hasPermission("user.vote")) {
            result.put("success", false);
            result.put("msg", "没有权限 {user.vote}");
            return result;
        }

        if (scoreNum < 0 || scoreNum > 10) {
            result.put("success", false);
            result.put("msg", "所填分数超出范围 {分数范围：[0, 10]");
            return result;
        }

        if (teacherMapper.selectOneById(teacherId) == null) {
            result.put("success", false);
            result.put("msg", "不存在该老师");
            return result;
        }

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

package me.rui.fdyzrank.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSONObject;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.rui.fdyzrank.mapper.ScoreMapper;
import me.rui.fdyzrank.mapper.TeacherMapper;
import me.rui.fdyzrank.model.Score;
import me.rui.fdyzrank.model.Teacher;
import me.rui.fdyzrank.model.table.Tables;
import me.rui.fdyzrank.service.ScoreService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;
import java.security.InvalidParameterException;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherRestController {
    private final TeacherMapper teacherMapper;
    private final ScoreMapper scoreMapper;
    private final ScoreService scoreService;

    @SaCheckLogin
    @SneakyThrows
    @SaCheckPermission("teacher.vote")
    @RequestMapping("/vote/{teacherId}")
    public JSONObject vote(@PathVariable long teacherId, @RequestParam int scoreNum) {

        if (scoreNum < 0 || scoreNum > 10) {
            throw new InvalidParameterException("所填分数超出范围 {分数范围：[0, 10]");
        }

        if (teacherMapper.selectOneById(teacherId) == null) {
            throw new FileNotFoundException("不存在该老师");
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(
                Tables.SCORE.USER_ID.eq(StpUtil.getLoginIdAsLong())
                        .and(Tables.SCORE.TEACHER_ID.eq(teacherId))
        );
        if (scoreMapper.selectOneByQuery(queryWrapper) != null) {
            throw new FileAlreadyExistsException("您已投过票");
        }

        JSONObject result = new JSONObject();
        scoreService.vote(new Score(StpUtil.getLoginIdAsLong(), teacherId, scoreNum));
        result.put("success", true);
        result.put("msg", "投票成功");
        return result;
    }

    @SaCheckLogin
    @SneakyThrows
    @SaCheckPermission("teacher.create")
    @RequestMapping("/create/{name}")
    public JSONObject create(
            @PathVariable String name,
            @RequestParam boolean isMale,
            @RequestParam String subject,
            @RequestParam long classId) {

        if (name == null || name.isEmpty()) {
            throw new InvalidParameterException("名称不能为空");
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(
                Tables.TEACHER.CLASS_ID.eq(classId)
                        .and(Tables.TEACHER.SUBJECT.eq(subject))
        );
        if (teacherMapper.selectOneByQuery(queryWrapper) != null) {
            throw new FileAlreadyExistsException("当前老师与现有班级学科重复");
        }

        JSONObject result = new JSONObject();
        Teacher teacher = new Teacher();
        teacher.setId(IdUtil.getSnowflakeNextId());
        teacher.setGlobalId(IdUtil.getSnowflakeNextId());
        teacher.setName(name);
        teacher.setMale(isMale);
        teacher.setClassId(classId);
        teacher.setSubject(subject);
        teacherMapper.insert(teacher);
        result.put("success", true);
        result.put("msg","创建成功");
        return result;
    }
}

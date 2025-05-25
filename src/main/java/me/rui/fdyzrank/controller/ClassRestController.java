package me.rui.fdyzrank.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSONObject;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.rui.fdyzrank.FdyzRankConfig;
import me.rui.fdyzrank.mapper.ClassMapper;
import me.rui.fdyzrank.model.Class;
import me.rui.fdyzrank.model.table.Tables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/class")
public class ClassRestController {
    private final ClassMapper classMapper;
    private final FdyzRankConfig fdyzRankConfig;

    public ClassRestController(ClassMapper classMapper, FdyzRankConfig fdyzRankConfig) {
        this.classMapper = classMapper;
        this.fdyzRankConfig = fdyzRankConfig;
        for (int i = 0; i < Class.Grade.values().length; i++) {
            Class.Grade grade = Class.Grade.values()[i];
            for (int j = 0; j < fdyzRankConfig.getClassCount(); j++) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.where(Tables.CLASS.GRADE
                        .eq(grade)
                        .and(Tables.CLASS.ORDER.eq(j)));
                if (classMapper.selectOneByQuery(queryWrapper) != null) {
                    continue;
                }
                Class clazz = new Class();
                clazz.setId(IdUtil.getSnowflakeNextId());
                clazz.setGrade(grade);
                clazz.setOrder(j +1);
                classMapper.insert(clazz);
            }
        }
    }

    @RequestMapping
    @SneakyThrows
    @SaCheckLogin
    @SaCheckPermission("class.get")
    public JSONObject getClass(Class.Grade grade, int order) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.where(Tables.CLASS.GRADE
                    .eq(grade)
                    .and(Tables.CLASS.ORDER.eq(order)));
            Class clazz = classMapper.selectOneByQuery(queryWrapper);
            if (clazz == null) {
                throw new FileNotFoundException("找不到此班级");
            }
            JSONObject result = new JSONObject();
            result.put("success", true);
            result.put("body", clazz);
            return result;
    }
}

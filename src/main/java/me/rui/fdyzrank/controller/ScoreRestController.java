package me.rui.fdyzrank.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.rui.fdyzrank.FdyzRankConfig;
import me.rui.fdyzrank.service.ScoreService;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidParameterException;

@RestController
@RequestMapping("/score")
@RequiredArgsConstructor
public class ScoreRestController {
    private final FdyzRankConfig fdyzRankConfig;
    private final ScoreService scoreService;

    @SaIgnore
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @SneakyThrows
    @RequestMapping("/update")
    public JSONObject update(@RequestParam String updateKey) {

        if (!fdyzRankConfig.getUpdateKey().equals(updateKey)) {
            throw new InvalidParameterException("密钥错误");
        }

        scoreService.update();
        JSONObject result = new JSONObject();
        result.put("success", true);
        result.put("msg","更新成功");
        return result;
    }
}

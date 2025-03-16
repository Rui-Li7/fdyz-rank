package me.rui.fdyzrank.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.rui.fdyzrank.mapper.ScoreMapper;
import me.rui.fdyzrank.mapper.TeacherMapper;
import me.rui.fdyzrank.model.Score;
import me.rui.fdyzrank.model.Teacher;
import me.rui.fdyzrank.model.table.Tables;
import me.rui.fdyzrank.service.ScoreService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
@Service("ScoreServiceMysql")
@RequiredArgsConstructor
public class ScoreServiceMysqlImpl implements ScoreService {
    private final ScoreMapper scoreMapper;
    private final TeacherMapper teacherMapper;

    @Override
    public int vote(Score score) {
        int i = scoreMapper.insert(score);
        log.info("Score表执行vote业务，受影响行数{}", i);
        return i;
    }

    @Override
    public int update() {
        AtomicInteger c = new AtomicInteger();
        Map<Long, Set<Score>> scoreMap = new HashMap<>();
        scoreMapper.selectListByQuery(new QueryWrapper().where(Tables.SCORE.VERIFIED.eq(false)))
                .forEach(score -> {
            if (!scoreMap.containsKey(score.getTeacherId())) {
                scoreMap.put(score.getTeacherId(), new HashSet<>());
            }
            scoreMap.get(score.getTeacherId()).add(score);
        });

        scoreMap.forEach((teacherId, scores) -> {
            BigDecimal sum = BigDecimal.valueOf(scores.stream()
                    .mapToInt(Score::getScore)
                    .sum());

            Teacher teacher = teacherMapper.selectOneById(teacherId);
            BigDecimal count = BigDecimal.valueOf(scores.size() + teacher.getVoteCount());
            teacher.setScore(
                    teacher.getScore()
                            .multiply(new BigDecimal(teacher.getVoteCount()))
                            .setScale(0, RoundingMode.HALF_UP)
                            .add(sum)
                            .divide(count, 2, RoundingMode.HALF_UP)
            );
            teacher.setVoteCount(count.intValue());
            teacherMapper.update(teacher);
            c.addAndGet(scores.size());
        });
        log.info("Score表执行update业务，受影响行数{}", c.get());
        return c.get();
    }
}

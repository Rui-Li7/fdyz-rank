package me.rui.fdyzrank.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Db;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.rui.fdyzrank.mapper.ScoreMapper;
import me.rui.fdyzrank.mapper.TeacherMapper;
import me.rui.fdyzrank.model.Score;
import me.rui.fdyzrank.model.Teacher;
import me.rui.fdyzrank.model.table.Tables;
import me.rui.fdyzrank.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Log4j2
@Service("ScoreServiceMysql")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ScoreServiceImpl implements ScoreService {
    private final ScoreMapper scoreMapper;
    private final TeacherMapper teacherMapper;

    @Override
    public int vote(Score score) {
        int i = scoreMapper.insert(score);
        log.info("Score表执行vote业务，受影响行数{}", i);
        return i;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public int update() {
        int total_count = 0;
        Map<Long, Set<Score>> scoreMap = new HashMap<>();
        Set<Score> verifiedScore = new HashSet<>();

        // 获取未验证的score
        for (Score score : scoreMapper.selectListByQuery(QueryWrapper.create().where(Tables.SCORE.VERIFIED.eq(false)))) {
            scoreMap.computeIfAbsent(score.getTeacherId(), _ -> new HashSet<>()).add(score);
        }

        for (Map.Entry<Long, Set<Score>> entry : scoreMap.entrySet()) {
            Long teacherId = entry.getKey();
            Set<Score> scores = entry.getValue();
            Teacher teacher = teacherMapper.selectOneById(teacherId);

            if (teacher == null) {
                continue;
            }

            BigDecimal sum = BigDecimal.valueOf(scores.stream()
                    .mapToInt(Score::getScore)
                    .sum());

            teacher.setVoteCount(teacher.getVoteCount() + scores.size());
            teacher.setScore(
                    teacher.getScore().add(sum)
            );
            teacherMapper.update(teacher);
            total_count += 1;
            total_count += scores.size();
            scores.forEach(score -> score.setVerified(true));
            verifiedScore.addAll(scores);
        }
        for (Score score : verifiedScore) {
            scoreMapper.updateByQuery(score,
                    QueryWrapper.create()
                            .where(
                                    Tables.SCORE.TEACHER_ID.eq(score.getTeacherId())
                                    .and(Tables.SCORE.USER_ID.eq(score.getUserId()))
                                    .and(Tables.SCORE.SCORE.eq(score.getScore()))
                            )
            );
        }
        log.info("Score表执行update业务，受影响行数{}", total_count);
        return total_count;
    }
}

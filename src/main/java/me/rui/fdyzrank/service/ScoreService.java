package me.rui.fdyzrank.service;

import me.rui.fdyzrank.model.Score;

public interface ScoreService {
    int vote(Score score);
    int update();
}

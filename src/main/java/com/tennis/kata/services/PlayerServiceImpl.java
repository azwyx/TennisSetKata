package com.tennis.kata.services;

import com.tennis.kata.entities.Match;
import com.tennis.kata.entities.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl {

    @Autowired
    IManageTennisGameService manageTennisGameService;

    @Autowired
    IManageTennisSetService manageTennisSetService;

    @Autowired
    IManageTieBreakService manageTieBreakService;

    public Player winBall(Match match, Player player) {
        return null;
    }
}

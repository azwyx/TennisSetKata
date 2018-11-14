package com.tennis.kata.services;

import com.tennis.kata.entities.Match;
import com.tennis.kata.entities.Player;
import org.springframework.beans.factory.annotation.Autowired;

public class ManageTennisMatchServiceImpl {

    @Autowired
    private IManageTennisSetService manageTennisSetService;

    @Autowired
    private IManageTennisGameService manageTennisGameService;

    public Player getTheWinner(Match match) {
        return null;
    }

    public Match startMatch(Player firstPlayer, Player secondPlayer) {
        return null;
    }
}

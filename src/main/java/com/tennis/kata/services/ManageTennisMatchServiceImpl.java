package com.tennis.kata.services;

import com.tennis.kata.entities.Game;
import com.tennis.kata.entities.Match;
import com.tennis.kata.entities.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManageTennisMatchServiceImpl implements IManageTennisMatchService{

    @Autowired
    private IManageTennisSetService manageTennisSetService;

    @Autowired
    private IManageTennisGameService manageTennisGameService;

    @Override
    public Match startMatch(Player firstPlayer, Player secondPlayer) {
        Match match = new Match();
        match.setFirstPlayer(firstPlayer);
        match.setSecondPlayer(secondPlayer);
        match.setGame(new Game());
        manageTennisSetService.startTennisSet(match);
        manageTennisGameService.startNewGame(match.getGame());

        return match;
    }

    @Override
    public Player getTheWinner(Match match) {
        if(manageTennisSetService.hasWinner(match.getTennisSet())) return manageTennisSetService.getLeadPlayer(match.getTennisSet(), match.getFirstPlayer(), match.getSecondPlayer());
        throw new RuntimeException("Match not yet finished");
    }
}

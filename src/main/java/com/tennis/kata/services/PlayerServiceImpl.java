package com.tennis.kata.services;

import com.tennis.kata.entities.Match;
import com.tennis.kata.entities.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl implements IPlayerService{

    @Autowired
    IManageTennisGameService manageTennisGameService;

    @Autowired
    IManageTennisSetService manageTennisSetService;

    @Autowired
    IManageTieBreakService manageTieBreakService;

    @Override
    public Player winBall(Match match, Player player) {
        // verify tie break activated
        if(!manageTennisSetService.hasWinner(match.getTennisSet())) {
            if(!manageTennisGameService.hasWinner(match.getGame())) {
                return manageTennisGameService.winPoint(match, player);
            } else if(manageTennisSetService.tieBreakRuleActivated(match.getTennisSet())){
                return manageTieBreakService.winPointInTieBreak(match, player);
            }
        }
        throw new RuntimeException("Someting wrong");
    }

    @Override
    public Player loseBall(Match match, Player player) {
        return manageTennisGameService.losePoint(match.getGame(), player);
    }

    @Override
    public int getPlayerGameScore(Player player) {
        return player.getGameScore();
    }

    @Override
    public int getPlayerTennisSetScore(Player player) {
        return player.getTennisSetScore();
    }

    @Override
    public int getPlayerTieBreakScore(Player player) {
        return player.getTieBreakScore();
    }

}

package com.tennis.kata.services;

import com.tennis.kata.entities.Match;
import com.tennis.kata.entities.Player;
import com.tennis.kata.entities.TennisSet;
import org.springframework.stereotype.Service;

@Service
public class ManageTennisSetServiceImpl implements IManageTennisSetService{

    @Override
    public TennisSet startTennisSet(Match match) {
        return null;
    }

    @Override
    public String getTennisSetScore(TennisSet tennisSet, Player firstPlayer, Player secondPlayer) {
        return null;
    }

    @Override
    public Player incrementTennisSetScore(Match match, Player player) {
        return null;
    }

    @Override
    public boolean hasWinner(TennisSet tennisSet) {
        return false;
    }

    @Override
    public boolean tieBreakRuleActivated(TennisSet tennisSet) {
        return false;
    }
}

package com.tennis.kata.services;

import com.tennis.kata.entities.Match;
import com.tennis.kata.entities.Player;
import com.tennis.kata.entities.TennisSet;

public interface IManageTennisSetService {
    Player incrementTennisSetScore(Match match, Player player);

    TennisSet startTennisSet(Match match);

    String getTennisSetScore(TennisSet tennisSet, Player firstPlayer, Player secondPlayer);

    boolean hasWinner(TennisSet tennisSet);

    boolean tieBreakRuleActivated(TennisSet tennisSet);

    Player getLeadPlayer(TennisSet tennisSet, Player firstPlayer, Player secondPlayer);
}

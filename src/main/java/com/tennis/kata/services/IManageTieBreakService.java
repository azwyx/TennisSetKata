package com.tennis.kata.services;

import com.tennis.kata.entities.Match;
import com.tennis.kata.entities.Player;
import com.tennis.kata.entities.TennisSet;

public interface IManageTieBreakService {
    Match startTieBreak(Match match);
    String getTieBreakScore(TennisSet tennisSet, Player firstPlayer, Player secondPlayer);
    Player winPointInTieBreak(Match match, Player player);
    boolean hasWinner(TennisSet tennisSet, Player firstPlayer, Player secondPlayer);
}

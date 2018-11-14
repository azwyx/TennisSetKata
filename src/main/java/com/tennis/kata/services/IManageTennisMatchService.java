package com.tennis.kata.services;

import com.tennis.kata.entities.Match;
import com.tennis.kata.entities.Player;

public interface IManageTennisMatchService {
    Match startMatch(Player firstPlayer, Player secondPlayer);
    Player getTheWinner(Match match);
}

package com.tennis.kata.services;

import com.tennis.kata.entities.Game;
import com.tennis.kata.entities.Match;
import com.tennis.kata.entities.Player;

public interface IManageTennisGameService {
    Game startNewGame(Game game);
    String getGameScore(Game game, Player firstPlayer, Player secondPlayer);
    Player winPoint(Match match, Player player);
    Player losePoint(Game game, Player player);
    Player winGame(Match match, Player player);
    boolean deuceRuleActivated(Game game);
    boolean hasWinner(Game game);
}

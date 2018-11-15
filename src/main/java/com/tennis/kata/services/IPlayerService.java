package com.tennis.kata.services;

import com.tennis.kata.entities.Match;
import com.tennis.kata.entities.Player;

public interface IPlayerService {

    Player winBall(Match match, Player player);
    Player loseBall(Match match, Player player);
    int getPlayerGameScore(Player player);
    int getPlayerTennisSetScore(Player player);
    int getPlayerTieBreakScore(Player player);

}

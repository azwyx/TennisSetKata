package com.tennis.kata.services;

import com.tennis.kata.entities.Match;
import com.tennis.kata.entities.Player;

public interface IManageTennisSetService {
    Player incrementTennisSetScore(Match match, Player player);
}

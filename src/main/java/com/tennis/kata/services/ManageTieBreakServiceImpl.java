package com.tennis.kata.services;

import com.tennis.kata.entities.Match;
import com.tennis.kata.entities.Player;
import com.tennis.kata.entities.TennisSet;
import org.springframework.stereotype.Service;

@Service
public class ManageTieBreakServiceImpl {

    public Match startTieBreak(Match match) {
        return null;
    }

    public Player winPointInTieBreak(Match match, Player player) {
        return null;
    }

    public String getTieBreakScore(TennisSet tennisSet, Player firstPlayer, Player secondPlayer) {
        return null;
    }

    public boolean hasWinner(TennisSet tennisSet, Player firstPlayer, Player secondPlayer) {
        return false;
    }


}

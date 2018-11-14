package com.tennis.kata.services;

import com.tennis.kata.entities.Match;
import com.tennis.kata.entities.Player;
import com.tennis.kata.entities.TennisSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManageTieBreakServiceImpl{

    @Autowired
    IManageTennisSetService manageTennisSetService;

    public Match startTieBreak(Match match) {
        if(!match.getTennisSet().isTieBreakRule()) throw new RuntimeException("Tie Break Rule is not activated");
        match.getFirstPlayer().setTieBreakScore(0);
        match.getSecondPlayer().setTieBreakScore(0);
        return match;
    }

    public Player winPointInTieBreak(Match match, Player player) {
        if(manageTennisSetService.hasWinner(match.getTennisSet())) throw new RuntimeException("Set already won");
        Player player1 = checkValidPlayer(match, player);
        player1.setTieBreakScore(player1.getTieBreakScore()+1);
        if(hasWinner(match.getTennisSet(), match.getFirstPlayer(), match.getSecondPlayer())) {
            return manageTennisSetService.incrementTennisSetScore(match, player1);
        }
        return player1;
    }

    public String getTieBreakScore(TennisSet tennisSet, Player firstPlayer, Player secondPlayer) {
        if(hasWinner(tennisSet, firstPlayer, secondPlayer))
            return "Tie Break Score : "+firstPlayer.getName()+" won the tie-break ("+firstPlayer.getTieBreakScore()+" / "+secondPlayer.getTieBreakScore()+")";

        return "Tie Break Score : "+firstPlayer.getName()+" "+firstPlayer.getTieBreakScore()+" / "+secondPlayer.getTieBreakScore()+" "+secondPlayer.getName();
    }

    public boolean hasWinner(TennisSet tennisSet, Player firstPlayer, Player secondPlayer) {
        if(tennisSet.isTieBreakRule() && getLeadPlayer(firstPlayer, secondPlayer).getTieBreakScore() >= 7 && Math.abs(firstPlayer.getTieBreakScore()-secondPlayer.getTieBreakScore()) >= 2) return true;
        return false;
    }

    public Player getLeadPlayer(Player firstPlayer, Player secondPlayer) {
        return (firstPlayer.getTieBreakScore() > secondPlayer.getTieBreakScore()) ? firstPlayer : secondPlayer;
    }

    public Player checkValidPlayer(Match match, Player player) {
        if (player.equals(match.getFirstPlayer())) return match.getFirstPlayer();
        else if(player.equals(match.getSecondPlayer())) return match.getSecondPlayer();
        else throw new RuntimeException("Player not valid");
    }
}

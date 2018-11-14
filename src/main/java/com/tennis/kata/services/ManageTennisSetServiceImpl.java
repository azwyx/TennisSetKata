package com.tennis.kata.services;

import com.tennis.kata.entities.Match;
import com.tennis.kata.entities.Player;
import com.tennis.kata.entities.TennisSet;
import org.springframework.stereotype.Service;

@Service
public class ManageTennisSetServiceImpl implements IManageTennisSetService{

    @Override
    public TennisSet startTennisSet(Match match) {
        if(match == null) throw new RuntimeException("tennisSet is null");
        if(match.getTennisSet() == null) match.setTennisSet(new TennisSet());
        match.getFirstPlayer().setTennisSetScore(0);
        match.getSecondPlayer().setTennisSetScore(0);
        return match.getTennisSet();
    }

    @Override
    public String getTennisSetScore(TennisSet tennisSet, Player firstPlayer, Player secondPlayer) {
        if(hasWinner(tennisSet)) return "Set Score : " + getLeadPlayer(tennisSet, firstPlayer, secondPlayer).getName() + " won the set and the match (" + firstPlayer.getTennisSetScore() + " - " + secondPlayer.getTennisSetScore()+")";
        if(tieBreakRuleActivated(tennisSet)) return "Set Score : " + firstPlayer.getTennisSetScore() + " - " + secondPlayer.getTennisSetScore() + " (Go to Tie Break)";
        return "Set Score : " + firstPlayer.getName() + " " + firstPlayer.getTennisSetScore() + " - " + secondPlayer.getTennisSetScore() + " " + secondPlayer.getName();

    }

    @Override
    public Player incrementTennisSetScore(Match match, Player player) {
        Player player1 = checkValidPlayer(match, player);

        if(hasWinner(match.getTennisSet())) throw new RuntimeException("Set is already finished");

        player1.setTennisSetScore(player1.getTennisSetScore()+1);
        if((Math.abs(match.getFirstPlayer().getTennisSetScore() - match.getSecondPlayer().getTennisSetScore()) == 2 && getLeadPlayer(match.getTennisSet(), match.getFirstPlayer(), match.getSecondPlayer()).getTennisSetScore() >= 6) ||
                (getLeadPlayer(match.getTennisSet(), match.getFirstPlayer(), match.getSecondPlayer()).getTennisSetScore() == 7 && tieBreakRuleActivated(match.getTennisSet()))){
            match.getTennisSet().setWon(true);
        } else if(match.getFirstPlayer().getTennisSetScore() == match.getSecondPlayer().getTennisSetScore() && match.getFirstPlayer().getTennisSetScore() == 6) {
            match.getTennisSet().setTieBreakRule(true);
        }

        return player1;
    }

    @Override
    public boolean hasWinner(TennisSet tennisSet) {
        return tennisSet.isWon();
    }

    @Override
    public boolean tieBreakRuleActivated(TennisSet tennisSet) {
        return tennisSet.isTieBreakRule();
    }

    @Override
    public Player getLeadPlayer(TennisSet tennisSet, Player firstPlayer, Player secondPlayer) {
        return (firstPlayer.getTennisSetScore() > secondPlayer.getTennisSetScore()) ? firstPlayer : secondPlayer;
    }

    public Player checkValidPlayer(Match match, Player player) {

        if (player.equals(match.getFirstPlayer())) return match.getFirstPlayer();
        else if(player.equals(match.getSecondPlayer())) return match.getSecondPlayer();
        else throw new RuntimeException("Player not valid");
    }
}

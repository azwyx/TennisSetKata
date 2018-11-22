package com.tennis.kata.services;

import com.tennis.kata.entities.Game;
import com.tennis.kata.entities.Match;
import com.tennis.kata.entities.Player;
import com.tennis.kata.tools.PointsDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManageTennisGameServiceImpl implements IManageTennisGameService{

    @Autowired
    IManageTennisSetService manageTennisSetService;

    @Override
    public Game startNewGame(Game game) {
        if(game == null) throw new RuntimeException("Game is null");
        game.setDeuceRule(false);
        game.setWon(false);
        game.setWinnerName("");
        return game;
    }

    @Override
    public String getGameScore(Game game, Player firstPlayer, Player secondPlayer) {
        if(hasWinner(game)) {
            return "Game Score : " + game.getWinnerName() + " won this game";
        }else if(firstPlayer.isAdvantage() || secondPlayer.isAdvantage()) {
            return "Game Score : Advantage " + getLeadPlayer(firstPlayer, secondPlayer).getName();
        } else if(deuceRuleActivated(game) && firstPlayer.getGameScore() == 3) return "Game Score : Deuce";

        return "Game Score : " + firstPlayer.getName() + " " + getScoreDescription(firstPlayer.getGameScore()) + " - " + getScoreDescription(secondPlayer.getGameScore()) + " " + secondPlayer.getName();
    }

    @Override
    public Player winPoint(Match match, Player player) {
        Player player1 = checkValidPlayer(match, player);

        if(deuceRuleActivated(match.getGame())) {
            if(player1.isAdvantage()) return winGame(match, player1);
            else player1.setAdvantage(true);
        } else {
            if(match.getFirstPlayer().getGameScore() == 3 && match.getSecondPlayer().getGameScore() == 3) player1.setAdvantage(true);
            else if (Math.abs(match.getFirstPlayer().getGameScore() - match.getSecondPlayer().getGameScore()) >= 1 &&
                    getLeadPlayer(match.getFirstPlayer(), match.getSecondPlayer()).equals(player1) &&
                    player1.getGameScore() == 3) {
                return winGame(match, player1);
            }else {
                player1.setGameScore(player1.getGameScore()+1);
            }
        }
        return player1;
    }

    @Override
    public Player losePoint(Game game, Player player) {
        if(player.isAdvantage()) {
            player.setAdvantage(false);
            game.setDeuceRule(true);
        }
        return player;
    }

    @Override
    public Player winGame(Match match, Player player) {
        Player player1 = checkValidPlayer(match, player);
        match.getGame().setWon(true);
        match.getGame().setWinnerName(player.getName());
        match.getGame().setDeuceRule(false);
        match.getFirstPlayer().setGameScore(0);
        match.getSecondPlayer().setGameScore(0);
        match.getFirstPlayer().setAdvantage(false);
        match.getSecondPlayer().setAdvantage(false);
        return manageTennisSetService.incrementTennisSetScore(match, player1);
    }

    @Override
    public boolean hasWinner(Game game) {
        return game.isWon();
    }

    @Override
    public boolean deuceRuleActivated(Game game) {
        return game.isDeuceRule();
    }

    public String getScoreDescription(int gameScore){
        return PointsDescription.values()[gameScore].toString();
    }

    public Player getLeadPlayer(Player firstPlayer, Player secondPlayer) {
        if(secondPlayer.getGameScore() > firstPlayer.getGameScore() || secondPlayer.isAdvantage()) return secondPlayer;

        return firstPlayer;
    }

    public Player checkValidPlayer(Match match, Player player) {
        if (player.equals(match.getFirstPlayer())) return match.getFirstPlayer();
        else if(player.equals(match.getSecondPlayer())) return match.getSecondPlayer();

        throw new RuntimeException("Player not valid");
    }
}

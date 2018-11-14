package com.tennis.kata.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

import com.tennis.kata.entities.Game;
import com.tennis.kata.entities.Match;
import com.tennis.kata.entities.Player;

@RunWith(MockitoJUnitRunner.class)
public class ManageTennisGameServiceTests {

    @Mock
    IManageTennisSetService manageTennisSetService;

    @InjectMocks
    private ManageTennisGameServiceImpl manageTennisGameService;

    private Player amine;
    private Player federer;
    private Match match;
    Game game;

    @Before
    public void setUp() {
        amine = new Player("Amine");
        federer = new Player("Federer");
        match = new Match(amine, federer);
        match.setGame(new Game());
        match.getGame().setDeuceRule(true);
        game = manageTennisGameService.startNewGame(match.getGame());
    }

    @Test
    public void shouldReturnNewGame() {
        assertThat(game.isDeuceRule()).isEqualTo(false);
        assertThat(game.isWon()).isEqualTo(false);
        assertThat(manageTennisGameService.getGameScore(game, amine, federer)).isEqualTo("Game Score : Amine LOVE - LOVE Federer");
    }

    @Test
    public void thirtyShouldBeDescriptionForScore2AndfifteenForScore1() {
        Player player1 = manageTennisGameService.winPoint(match, amine);
        player1 = manageTennisGameService.winPoint(match, amine);
        Player player2 = manageTennisGameService.winPoint(match, federer);

        assertThat(manageTennisGameService.getGameScore(match.getGame(), player1, player2)).isEqualTo("Game Score : Amine THIRTY - FIFTEEN Federer");
    }

    @Test
    public void fortyShouldBeDescriptionForScore3() {
        IntStream.rangeClosed(1, 3).forEach((Integer) -> {
            manageTennisGameService.winPoint(match, amine);
        });
        IntStream.rangeClosed(1, 3).forEach((Integer) -> {
            manageTennisGameService.winPoint(match, federer);
        });

        assertThat(manageTennisGameService.getGameScore(match.getGame(), match.getFirstPlayer(), match.getSecondPlayer())).isEqualTo("Game Score : Amine FORTY - FORTY Federer");
        assertThat(amine.getGameScore()).isEqualTo(3);
        assertThat(match.getSecondPlayer().getGameScore()).isEqualTo(3);
    }

    @Test
    public void advantageShouldBeDescriptionWhenLeastThreePointsHaveBeenScoredByEachSideAndPlayerHasOnePointMoreThanHisOpponent() {
        IntStream.rangeClosed(1, 3).forEach((Integer) -> {
            manageTennisGameService.winPoint(match, amine);
        });
        IntStream.rangeClosed(1, 4).forEach((Integer) -> {
            manageTennisGameService.winPoint(match, federer);
        });
        assertThat(manageTennisGameService.getGameScore(match.getGame(), match.getFirstPlayer(), match.getSecondPlayer())).isEqualTo("Game Score : Advantage Federer");
        assertThat(amine.getGameScore()).isEqualTo(3);
        assertThat(match.getSecondPlayer().getGameScore()).isEqualTo(3);
        assertThat(match.getSecondPlayer().isAdvantage()).isEqualTo(true);
    }

    @Test
    public void gameShouldBeWonByTheFirstPlayerToHaveWonAtLeastFourPointsInTotalAndWithAtLeastTwoPointsMoreThanTheOpponent() {
        IntStream.rangeClosed(1, 3).forEach((Integer) -> {
            manageTennisGameService.winPoint(match, amine);
        });
        IntStream.rangeClosed(1, 2).forEach((Integer) -> {
            manageTennisGameService.winPoint(match, federer);
        });
        assertThat(manageTennisGameService.getGameScore(game, amine, federer)).isEqualTo("Game Score : Amine FORTY - THIRTY Federer");

        manageTennisGameService.winPoint(match, amine);
        assertThat(manageTennisGameService.getGameScore(game, amine, federer)).isEqualTo("Game Score : Amine won this game");
        assertThat(manageTennisGameService.hasWinner(game)).isEqualTo(true);
        assertThat(match.getFirstPlayer().getGameScore()).isEqualTo(0);
        assertThat(match.getSecondPlayer().getGameScore()).isEqualTo(0);
    }

    @Test
    public void deuceShouldBeDescriptionWhenThePlayerLoseAdvantage() {
        IntStream.rangeClosed(1, 3).forEach((Integer) -> {
            manageTennisGameService.winPoint(match, amine);
        });
        IntStream.rangeClosed(1, 3).forEach((Integer) -> {
            manageTennisGameService.winPoint(match, federer);
        });
        assertThat(manageTennisGameService.getGameScore(game, amine, federer)).isEqualTo("Game Score : Amine FORTY - FORTY Federer");

        manageTennisGameService.winPoint(match, amine);
        assertThat(manageTennisGameService.getGameScore(game, amine, federer)).isEqualTo("Game Score : Advantage Amine");

        manageTennisGameService.losePoint(game, amine);
        assertThat(manageTennisGameService.getGameScore(game, amine, federer)).isEqualTo("Game Score : Deuce");

        manageTennisGameService.winPoint(match, federer);
        assertThat(manageTennisGameService.getGameScore(game, amine, federer)).isEqualTo("Game Score : Advantage Federer");

        manageTennisGameService.losePoint(game, federer);
        assertThat(manageTennisGameService.getGameScore(game, amine, federer)).isEqualTo("Game Score : Deuce");
    }

    @Test
    public void gameShouldBeWonByThePlayerHowHasAdvantageWinAnOtherPoint() {
        IntStream.rangeClosed(1, 3).forEach((Integer) -> {
            manageTennisGameService.winPoint(match, amine);
        });
        IntStream.rangeClosed(1, 3).forEach((Integer) -> {
            manageTennisGameService.winPoint(match, federer);
        });
        assertThat(manageTennisGameService.getGameScore(game, amine, federer)).isEqualTo("Game Score : Amine FORTY - FORTY Federer");

        manageTennisGameService.winPoint(match, amine);
        assertThat(manageTennisGameService.getGameScore(game, amine, federer)).isEqualTo("Game Score : Advantage Amine");

        manageTennisGameService.losePoint(game, amine);
        assertThat(manageTennisGameService.getGameScore(game, amine, federer)).isEqualTo("Game Score : Deuce");

        manageTennisGameService.winPoint(match, amine);
        assertThat(manageTennisGameService.getGameScore(game, amine, federer)).isEqualTo("Game Score : Advantage Amine");

        amine.setTennisSetScore(1);
        given(manageTennisSetService.incrementTennisSetScore(any(Match.class), any(Player.class))).willReturn(amine);
        Player player2 = manageTennisGameService.winPoint(match, amine);
        assertThat(manageTennisGameService.getGameScore(game, amine, federer)).isEqualTo("Game Score : Amine won this game");
        assertThat(manageTennisGameService.hasWinner(game)).isEqualTo(true);
        assertThat(match.getFirstPlayer().getGameScore()).isEqualTo(0);
        assertThat(match.getSecondPlayer().getGameScore()).isEqualTo(0);
        assertThat(player2.getTennisSetScore()).isEqualTo(1);
    }
}


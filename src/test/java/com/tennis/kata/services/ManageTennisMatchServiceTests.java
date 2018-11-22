package com.tennis.kata.services;

import com.tennis.kata.entities.Game;
import com.tennis.kata.entities.Match;
import com.tennis.kata.entities.Player;
import com.tennis.kata.entities.TennisSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class ManageTennisMatchServiceTests {

    @Mock
    private IManageTennisGameService manageTennisGameService;

    @Mock
    private IManageTennisSetService manageTennisSetService;

    @InjectMocks
    private ManageTennisMatchServiceImpl manageTennisMatchService;

    private Player amine;
    private Player federer;
    private Match match;
    private TennisSet tennisSet;
    private Game game;

    @Before
    public void setUp() {
        amine = new Player("Amine");
        federer = new Player("Federer");

        tennisSet = new TennisSet();
        game = new Game();
        tennisSet.setTieBreakRule(true);
        tennisSet.setWon(false);

        match = new Match(amine, federer);
        match.setGame(game);

        match.setTennisSet(tennisSet);
    }

    @Test
    public void matchShouldStartWithAScoreOfZeroGameForEachPlayer() {
        Game game1 = new Game();
        TennisSet tennisSet1 = new TennisSet();
        given(manageTennisGameService.startNewGame(any(Game.class))).willReturn(game1);
        given(manageTennisSetService.startTennisSet(any(Match.class))).willReturn(tennisSet1);
        Match match = manageTennisMatchService.startMatch(amine, federer);

        assertThat(match.getGame().isWon()).isEqualTo(false);
        assertThat(match.getGame().isDeuceRule()).isEqualTo(false);
        assertThat(match.getFirstPlayer().getGameScore()).isEqualTo(0);
        assertThat(match.getSecondPlayer().getGameScore()).isEqualTo(0);

        assertThat(match.getFirstPlayer().getTennisSetScore()).isEqualTo(0);
        assertThat(match.getFirstPlayer().getTieBreakScore()).isEqualTo(0);
    }

    @Test
    public void matchShouldEndsWithAWinner() {

        given(manageTennisSetService.hasWinner(any(TennisSet.class))).willReturn(true);
        given(manageTennisSetService.getLeadPlayer(any(TennisSet.class), any(Player.class), any(Player.class))).willReturn(amine);

        assertThat(manageTennisMatchService.getTheWinner(match)).isEqualTo(amine);
    }
}
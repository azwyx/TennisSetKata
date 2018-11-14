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
public class PlayerServiceTests {

    @Mock
    private ManageTennisGameServiceImpl manageTennisGameService;

    @Mock
    private ManageTennisSetServiceImpl manageTennisSetService;

    @Mock
    private ManageTieBreakServiceImpl manageTieBreakService;

    @InjectMocks
    private PlayerServiceImpl playerServiceImpl;

    private Player amine;
    private Player federer;
    private Match match;

    @Before
    public void setUp() {
        amine = new Player("Amine");
        federer = new Player("Federer");
        match = new Match(amine, federer);
    }

    @Test
    public void shouldReturnPlayerWithGameScoreEqualToFIFTEEN() {
        amine.setGameScore(1);
        given(manageTennisSetService.hasWinner(any(TennisSet.class))).willReturn(false);
        given(manageTennisGameService.hasWinner(any(Game.class))).willReturn(false);
        given(manageTennisGameService.winPoint(any(Match.class), any(Player.class))).willReturn(amine);

        Player player = playerServiceImpl.winBall(match, amine);
        assertThat(player.getGameScore()).isEqualTo(1);
    }

    @Test
    public void shouldReturnPlayerWithTieBreakScoreEqualTo1() {
        amine.setTieBreakScore(1);
        given(manageTennisSetService.hasWinner(any(TennisSet.class))).willReturn(false);
        given(manageTennisGameService.hasWinner(any(Game.class))).willReturn(true);
        given(manageTennisSetService.tieBreakRuleActivated(any(TennisSet.class))).willReturn(true);
        given(manageTieBreakService.winPointInTieBreak(any(Match.class), any(Player.class))).willReturn(amine);

        Player player = playerServiceImpl.winBall(match, amine);
        assertThat(player.getTieBreakScore()).isEqualTo(1);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeException() {
        amine.setTieBreakScore(1);
        given(manageTennisSetService.hasWinner(any(TennisSet.class))).willReturn(false);
        given(manageTennisGameService.hasWinner(any(Game.class))).willReturn(true);
        given(manageTennisSetService.tieBreakRuleActivated(any(TennisSet.class))).willReturn(false);
        given(manageTieBreakService.winPointInTieBreak(any(Match.class), any(Player.class))).willReturn(amine);

        Player player = playerServiceImpl.winBall(match, amine);
    }
}

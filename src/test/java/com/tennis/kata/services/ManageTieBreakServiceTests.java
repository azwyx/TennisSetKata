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

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class ManageTieBreakServiceTests {

    @Mock
    private IManageTennisSetService manageTennisSetService;

    @InjectMocks
    private ManageTieBreakServiceImpl manageTieBreakService;

    private Player amine;
    private Player federer;
    private Match match;
    private TennisSet tennisSet;

    @Before
    public void setUp() {
        amine = new Player("Amine");
        federer = new Player("Federer");
        amine.setTennisSetScore(6);
        federer.setTennisSetScore(6);

        tennisSet = new TennisSet();
        tennisSet.setTieBreakRule(true);
        tennisSet.setWon(false);

        match = new Match(amine, federer);
        match.setGame(new Game());

        match.setTennisSet(tennisSet);
    }

    @Test
    public void tieBreakShouldStartWithAScoreOfZeroPointForEachPlayer() {
        Match rsltMatch = manageTieBreakService.startTieBreak(match);

        assertThat(match.getFirstPlayer().getTieBreakScore()).isEqualTo(0);
        assertThat(match.getSecondPlayer().getTieBreakScore()).isEqualTo(0);
        assertThat(manageTieBreakService.getTieBreakScore(match.getTennisSet(), match.getFirstPlayer(), match.getSecondPlayer())).isEqualTo("Tie Break Score : Amine 0 / 0 Federer");
        assertThat(manageTieBreakService.hasWinner(match.getTennisSet(), match.getFirstPlayer(), match.getSecondPlayer())).isEqualTo(false);
    }

    @Test
    public void tieBreakScoreShouldBeIncrementedEachTimeAPlayerWinAGame() {

        Player player1 = manageTieBreakService.winPointInTieBreak(match, amine);

        assertThat(match.getFirstPlayer().getTieBreakScore()).isEqualTo(1);
        assertThat(match.getSecondPlayer().getTieBreakScore()).isEqualTo(0);
        assertThat(manageTieBreakService.getTieBreakScore(match.getTennisSet(), match.getFirstPlayer(), match.getSecondPlayer())).isEqualTo("Tie Break Score : Amine 1 / 0 Federer");
        assertThat(manageTieBreakService.hasWinner(match.getTennisSet(), match.getFirstPlayer(), match.getSecondPlayer())).isEqualTo(false);
    }

    @Test
    public void tieBreakShouldEndAsSoonAsAPlayerGetsALeast7PointsAnd2PointsMoreThanHisOpponent() {
        IntStream.rangeClosed(1, 6).forEach((Integer) -> {
            manageTieBreakService.winPointInTieBreak(match, amine);
        });
        IntStream.rangeClosed(1, 4).forEach((Integer) -> {
            manageTieBreakService.winPointInTieBreak(match, federer);
        });

        assertThat(match.getFirstPlayer().getTieBreakScore()).isEqualTo(6);
        assertThat(match.getSecondPlayer().getTieBreakScore()).isEqualTo(4);
        assertThat(manageTieBreakService.getTieBreakScore(match.getTennisSet(), match.getFirstPlayer(), match.getSecondPlayer())).isEqualTo("Tie Break Score : Amine 6 / 4 Federer");
        assertThat(manageTieBreakService.hasWinner(match.getTennisSet(), match.getFirstPlayer(), match.getSecondPlayer())).isEqualTo(false);

        IntStream.rangeClosed(1, 3).forEach((Integer) -> {
            manageTieBreakService.winPointInTieBreak(match, federer);
        });

        assertThat(match.getFirstPlayer().getTieBreakScore()).isEqualTo(6);
        assertThat(match.getSecondPlayer().getTieBreakScore()).isEqualTo(7);
        assertThat(manageTieBreakService.getTieBreakScore(match.getTennisSet(), match.getFirstPlayer(), match.getSecondPlayer())).isEqualTo("Tie Break Score : Amine 6 / 7 Federer");
        assertThat(manageTieBreakService.hasWinner(match.getTennisSet(), match.getFirstPlayer(), match.getSecondPlayer())).isEqualTo(false);

        IntStream.rangeClosed(1, 2).forEach((Integer) -> {
            manageTieBreakService.winPointInTieBreak(match, amine);
        });

        amine.setTennisSetScore(7);
        given(manageTennisSetService.incrementTennisSetScore(any(Match.class), any(Player.class))).willReturn(amine);
        Player player1 = manageTieBreakService.winPointInTieBreak(match, amine);

        assertThat(match.getFirstPlayer().getTennisSetScore()).isEqualTo(7);
        assertThat(match.getFirstPlayer().getTieBreakScore()).isEqualTo(9);
        assertThat(match.getSecondPlayer().getTieBreakScore()).isEqualTo(7);
        assertThat(manageTieBreakService.getTieBreakScore(match.getTennisSet(), match.getFirstPlayer(), match.getSecondPlayer())).isEqualTo("Tie Break Score : Amine won the tie-break (9 / 7)");
        assertThat(manageTieBreakService.hasWinner(match.getTennisSet(), match.getFirstPlayer(), match.getSecondPlayer())).isEqualTo(true);
    }
}

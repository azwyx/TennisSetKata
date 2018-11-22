package com.tennis.kata.services;

import com.tennis.kata.entities.Game;
import com.tennis.kata.entities.Match;
import com.tennis.kata.entities.Player;
import com.tennis.kata.entities.TennisSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ManageTennisSetServiceTests {

    @InjectMocks
    private ManageTennisSetServiceImpl manageTennisSetService;

    private Player amine;
    private Player federer;
    private Match match;
    private TennisSet tennisSet;

    @Before
    public void setUp() {
        amine = new Player("Amine");
        federer = new Player("Federer");
        match = new Match(amine, federer);
        match.setGame(new Game());
        tennisSet = new TennisSet();
        match.setTennisSet(tennisSet);
    }

    @Test
    public void setShouldStartWithAScoreOfZeroGameForEachPlayer() {
        amine.setTennisSetScore(1);
        federer.setTennisSetScore(1);
        manageTennisSetService.startTennisSet(match);

        assertThat(manageTennisSetService.getTennisSetScore(match.getTennisSet(), match.getFirstPlayer(), match.getSecondPlayer())).isEqualTo("Set Score : Amine 0 - 0 Federer");
        assertThat(match.getFirstPlayer().getTennisSetScore()).isEqualTo(0);
        assertThat(match.getSecondPlayer().getTennisSetScore()).isEqualTo(0);

    }

    @Test
    public void setScoreShouldBeIncrementedEachTimeAPlayerWinAGame() {
        manageTennisSetService.startTennisSet(match);
        manageTennisSetService.incrementTennisSetScore(match, amine);

        assertThat(manageTennisSetService.getTennisSetScore(match.getTennisSet(), match.getFirstPlayer(), match.getSecondPlayer())).isEqualTo("Set Score : Amine 1 - 0 Federer");
        assertThat(match.getFirstPlayer().getTennisSetScore()).isEqualTo(1);
        assertThat(match.getSecondPlayer().getTennisSetScore()).isEqualTo(0);
    }

    @Test
    public void setShouldBeWonByTheFirstPlayerToHaveWonSixPointsInTotalAndWithAtLeastTwoPointsMoreThanTheOpponent() {
        IntStream.rangeClosed(1, 5).forEach((Integer) -> {
            manageTennisSetService.incrementTennisSetScore(match, amine);
        });
        IntStream.rangeClosed(1, 5).forEach((Integer) -> {
            manageTennisSetService.incrementTennisSetScore(match, federer);
        });

        assertThat(manageTennisSetService.hasWinner(tennisSet)).isEqualTo(false);
        assertThat(manageTennisSetService.getTennisSetScore(match.getTennisSet(), match.getFirstPlayer(), match.getSecondPlayer())).isEqualTo("Set Score : Amine 5 - 5 Federer");

        manageTennisSetService.incrementTennisSetScore(match, amine);
        assertThat(manageTennisSetService.hasWinner(tennisSet)).isEqualTo(false);

        manageTennisSetService.incrementTennisSetScore(match, amine);
        assertThat(match.getFirstPlayer().getTennisSetScore()).isEqualTo(7);
        assertThat(match.getSecondPlayer().getTennisSetScore()).isEqualTo(5);
        assertThat(manageTennisSetService.getTennisSetScore(match.getTennisSet(), match.getFirstPlayer(), match.getSecondPlayer())).isEqualTo("Set Score : Amine won the set and the match (7 - 5)");
        assertThat(manageTennisSetService.hasWinner(tennisSet)).isEqualTo(true);
    }

    @Test
    public void tieBreakRuleShouldBeactivedWhentheScoreOfEachPlayerIsSix() {
        IntStream.rangeClosed(1, 5).forEach((Integer) -> {
            manageTennisSetService.incrementTennisSetScore(match, amine);
        });
        IntStream.rangeClosed(1, 5).forEach((Integer) -> {
            manageTennisSetService.incrementTennisSetScore(match, federer);
        });

        assertThat(manageTennisSetService.hasWinner(tennisSet)).isEqualTo(false);
        assertThat(manageTennisSetService.getTennisSetScore(match.getTennisSet(), match.getFirstPlayer(), match.getSecondPlayer())).isEqualTo("Set Score : Amine 5 - 5 Federer");

        manageTennisSetService.incrementTennisSetScore(match, amine);
        assertThat(manageTennisSetService.hasWinner(tennisSet)).isEqualTo(false);
        assertThat(manageTennisSetService.tieBreakRuleActivated(tennisSet)).isEqualTo(false);

        manageTennisSetService.incrementTennisSetScore(match, federer);
        assertThat(manageTennisSetService.hasWinner(tennisSet)).isEqualTo(false);
        assertThat(manageTennisSetService.tieBreakRuleActivated(tennisSet)).isEqualTo(true);
        assertThat(manageTennisSetService.getTennisSetScore(match.getTennisSet(), match.getFirstPlayer(), match.getSecondPlayer())).isEqualTo("Set Score : 6 - 6 (Go to Tie Break)");
    }
}

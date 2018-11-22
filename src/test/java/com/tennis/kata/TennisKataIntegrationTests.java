package com.tennis.kata;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;

import com.tennis.kata.entities.Match;
import com.tennis.kata.entities.Player;
import com.tennis.kata.services.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TennisKataIntegrationTests {

    @Autowired
    private IManageTennisMatchService manageTennisMatchService;

    @Autowired
    IManageTieBreakService manageTieBreakService;

    @Autowired
    private IManageTennisSetService manageTennisSetService;

    @Autowired
    private IManageTennisGameService manageTennisGameService;

    @Autowired
    private IPlayerService managePlayerService;

    private Player amine;
    private Player federer;

    @Before
    public void setUp() {
        amine = new Player("Amine");
        federer = new Player("Federer");
    }

    @Test
    public void simulateTheProgressOfTheMatch() {

        //Start the match between Amine and Federer
        Match match = manageTennisMatchService.startMatch(amine, federer);

        //Verification of Game and Set Scores
        assertThat(manageTennisGameService.getGameScore(match.getGame(), amine, federer)).isEqualTo("Game Score : Amine LOVE - LOVE Federer");
        assertThat(manageTennisSetService.getTennisSetScore(match.getTennisSet(), amine, federer)).isEqualTo("Set Score : Amine 0 - 0 Federer");
        //Rules verification
        assertThat(manageTennisGameService.deuceRuleActivated(match.getGame())).isEqualTo(false);
        assertThat(manageTennisSetService.tieBreakRuleActivated(match.getTennisSet())).isEqualTo(false);

        // Amine win this game
        gameShouldBeWonByAmine(match, amine, federer);

        //Start new game
        shouldStartNewGame(match, amine, federer);

        // Federer win this game
        gameShouldBeWonByFedererAfterADeuce(match, amine, federer);

        // Start new game
        shouldStartNewGame(match, amine, federer);

        // Two players reach the score of 5 games
        twoPlayersShouldReachScoreOf5Games(match, amine, federer);

        // Start new game
        shouldStartNewGame(match, amine, federer);

        // If the 2 players reach the score of 6 Games , the Tie-Break rule is activated
        tieBreakRuleShouldBeActivatedIfTwoThePlayersReachScoreOf6Games(match, amine, federer);

        // Tie-Break score should be incremented each time a player win a point
        tieBreakScoreShouldBeIncrementedIfPlayersWinPoints(match, amine, federer);

        // The Tie-Break ends as soon as a player gets a least 7 points and 2 points more than his opponent
        amineGotTwoPointMoreThanFedererAndWonTheSetAndTheMatch(match, amine, federer);
    }

    public void shouldStartNewGame(Match match, Player player1, Player player2) {
        manageTennisGameService.startNewGame(match.getGame());
        assertThat(manageTennisGameService.getGameScore(match.getGame(), player1, player2)).isEqualTo("Game Score : Amine LOVE - LOVE Federer");
        assertThat(manageTennisGameService.deuceRuleActivated(match.getGame())).isEqualTo(false);
        assertThat(manageTennisGameService.hasWinner(match.getGame())).isEqualTo(false);
    }

    public void gameShouldBeWonByAmine(Match match, final Player player1, final Player player2) {
        Player firstPlayer, secondPlayer;
        //verify that each time a player win a point, the Game score is incremented
        firstPlayer = managePlayerService.winBall(match, player1);
        assertThat(managePlayerService.getPlayerGameScore(firstPlayer)).isEqualTo(1);
        assertThat(manageTennisGameService.getGameScore(match.getGame(), player1, player2)).isEqualTo("Game Score : Amine FIFTEEN - LOVE Federer");

        secondPlayer = managePlayerService.winBall(match, player2);
        assertThat(managePlayerService.getPlayerGameScore(secondPlayer)).isEqualTo(1);
        assertThat(manageTennisGameService.getGameScore(match.getGame(), player1, player2)).isEqualTo("Game Score : Amine FIFTEEN - FIFTEEN Federer");

        IntStream.rangeClosed(1, 2).forEach((Integer) -> {
            managePlayerService.winBall(match, player1);
        });
        assertThat(managePlayerService.getPlayerGameScore(match.getFirstPlayer())).isEqualTo(3);
        assertThat(manageTennisGameService.getGameScore(match.getGame(), player1, player2)).isEqualTo("Game Score : Amine FORTY - FIFTEEN Federer");
        assertThat(manageTennisGameService.hasWinner(match.getGame())).isEqualTo(false);

        //verify game score when the game is won
        firstPlayer = managePlayerService.winBall(match, player1);
        assertThat(manageTennisGameService.getGameScore(match.getGame(), player1, player2)).isEqualTo("Game Score : Amine won this game");
        assertThat(managePlayerService.getPlayerGameScore(firstPlayer)).isEqualTo(0);
        assertThat(manageTennisGameService.hasWinner(match.getGame())).isEqualTo(true);
        assertThat(manageTennisSetService.getTennisSetScore(match.getTennisSet(), player1, player2)).isEqualTo("Set Score : Amine 1 - 0 Federer");
        assertThat(managePlayerService.getPlayerTennisSetScore(firstPlayer)).isEqualTo(1);
    }

    public void gameShouldBeWonByFedererAfterADeuce(Match match, Player player1, Player player2) {

        IntStream.rangeClosed(1, 3).forEach((Integer) -> {
            managePlayerService.winBall(match, player1);
        });

        IntStream.rangeClosed(1, 3).forEach((Integer) -> {
            managePlayerService.winBall(match, player2);
        });
        assertThat(managePlayerService.getPlayerGameScore(match.getFirstPlayer())).isEqualTo(3);
        assertThat(managePlayerService.getPlayerGameScore(match.getFirstPlayer())).isEqualTo(3);
        assertThat(manageTennisGameService.getGameScore(match.getGame(), player1, player2)).isEqualTo("Game Score : Amine FORTY - FORTY Federer");
        assertThat(manageTennisGameService.hasWinner(match.getGame())).isEqualTo(false);
        assertThat(manageTennisGameService.deuceRuleActivated(match.getGame())).isEqualTo(false);

        managePlayerService.winBall(match, player2);
        assertThat(manageTennisGameService.getGameScore(match.getGame(), player1, player2)).isEqualTo("Game Score : Advantage Federer");

        managePlayerService.loseBall(match, player2);
        assertThat(manageTennisGameService.getGameScore(match.getGame(), player1, player2)).isEqualTo("Game Score : Deuce");
        assertThat(managePlayerService.getPlayerGameScore(match.getSecondPlayer())).isEqualTo(3);
        assertThat(manageTennisGameService.deuceRuleActivated(match.getGame())).isEqualTo(true);

        managePlayerService.winBall(match, player2);
        assertThat(manageTennisGameService.getGameScore(match.getGame(), player1, player2)).isEqualTo("Game Score : Advantage Federer");
        assertThat(manageTennisGameService.deuceRuleActivated(match.getGame())).isEqualTo(true);
        assertThat(manageTennisGameService.hasWinner(match.getGame())).isEqualTo(false);

        managePlayerService.winBall(match, player2);
        assertThat(manageTennisGameService.getGameScore(match.getGame(), player1, player2)).isEqualTo("Game Score : Federer won this game");
        assertThat(manageTennisGameService.deuceRuleActivated(match.getGame())).isEqualTo(false);
        assertThat(manageTennisGameService.hasWinner(match.getGame())).isEqualTo(true);
    }

    public void twoPlayersShouldReachScoreOf5Games(Match match, final Player player1, final Player player2){
        IntStream.rangeClosed(1, 4).forEach((Integer) -> {
            manageTennisGameService.winGame(match, player1);
        });

        IntStream.rangeClosed(1, 4).forEach((Integer) -> {
            manageTennisGameService.winGame(match, player2);
        });

        assertThat(managePlayerService.getPlayerTennisSetScore(player1)).isEqualTo(5);
        assertThat(managePlayerService.getPlayerTennisSetScore(player2)).isEqualTo(5);
        assertThat(manageTennisSetService.getTennisSetScore(match.getTennisSet(), player1, player2)).isEqualTo("Set Score : Amine 5 - 5 Federer");

    }

    public void tieBreakRuleShouldBeActivatedIfTwoThePlayersReachScoreOf6Games(Match match, final Player player1, final Player player2){
        Player firstPlayer, secondPlayer;

        firstPlayer = manageTennisGameService.winGame(match, player1);
        secondPlayer = manageTennisGameService.winGame(match, player2);

        assertThat(managePlayerService.getPlayerTennisSetScore(firstPlayer)).isEqualTo(6);
        assertThat(managePlayerService.getPlayerTennisSetScore(secondPlayer)).isEqualTo(6);
        assertThat(manageTennisSetService.tieBreakRuleActivated(match.getTennisSet())).isEqualTo(true);

        assertThat(manageTennisSetService.getTennisSetScore(match.getTennisSet(), player1, player2)).isEqualTo("Set Score : 6 - 6 (Go to Tie Break)");
        assertThat(manageTieBreakService.getTieBreakScore(match.getTennisSet(), player1, player2)).isEqualTo("Tie Break Score : Amine 0 / 0 Federer");
    }

    public void tieBreakScoreShouldBeIncrementedIfPlayersWinPoints(Match match, Player player1, Player player2) {
        Player firstPlayer, secondPlayer;

        IntStream.rangeClosed(1, 3).forEach((Integer) -> {
            managePlayerService.winBall(match, player1);
        });

        firstPlayer = managePlayerService.winBall(match, player1);

        secondPlayer = managePlayerService.winBall(match, player2);

        assertThat(managePlayerService.getPlayerTieBreakScore(firstPlayer)).isEqualTo(4);
        assertThat(managePlayerService.getPlayerTieBreakScore(secondPlayer)).isEqualTo(1);

    }

    public void amineGotTwoPointMoreThanFedererAndWonTheSetAndTheMatch(Match match, Player player1, Player player2) {

        IntStream.rangeClosed(1, 2).forEach((Integer) -> {
            managePlayerService.winBall(match, player1);
        });

        IntStream.rangeClosed(1, 4).forEach((Integer) -> {
            managePlayerService.winBall(match, player2);
        });

        managePlayerService.winBall(match, player2);
        managePlayerService.winBall(match, player1);
        assertThat(managePlayerService.getPlayerTieBreakScore(match.getFirstPlayer())).isEqualTo(7);
        assertThat(managePlayerService.getPlayerTieBreakScore(match.getSecondPlayer())).isEqualTo(6);
        assertThat(manageTennisSetService.hasWinner(match.getTennisSet())).isEqualTo(false);

        managePlayerService.winBall(match, player1);
        assertThat(managePlayerService.getPlayerTieBreakScore(match.getFirstPlayer())).isEqualTo(8);
        assertThat(managePlayerService.getPlayerTieBreakScore(match.getSecondPlayer())).isEqualTo(6);
        assertThat(managePlayerService.getPlayerTennisSetScore(match.getFirstPlayer())).isEqualTo(7);

        assertThat(manageTieBreakService.getTieBreakScore(match.getTennisSet(), match.getFirstPlayer(), match.getSecondPlayer())).isEqualTo("Tie Break Score : Amine won the tie-break (8 / 6)");
        assertThat(manageTennisSetService.hasWinner(match.getTennisSet())).isEqualTo(true);
        assertThat(manageTennisSetService.getTennisSetScore(match.getTennisSet(), player1, player2)).isEqualTo("Set Score : Amine won the set and the match (7 - 6)");
    }
}

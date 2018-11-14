package com.tennis.kata.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Match {

    private Player firstPlayer;
    private Player secondPlayer;
    private Game game;

    public Match(Player firstPlayer, Player secondPlayer) {
        super();
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }
}

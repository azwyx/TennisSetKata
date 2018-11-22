package com.tennis.kata.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Player {

    private String name;
    private int gameScore;
    private boolean advantage;
    private int tennisSetScore;
    private int tieBreakScore;

    public Player(String name) {
        super();
        this.name = name;
    }


}

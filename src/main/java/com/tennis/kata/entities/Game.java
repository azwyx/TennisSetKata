package com.tennis.kata.entities;

import lombok.Data;

@Data
public class Game {

    private boolean deuceRule;
    private boolean won;
    private String winnerName;
}

package com.towerdefense.game.entity;


public abstract class Player extends Entity{

    protected String name;

    public Player(String name){
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}

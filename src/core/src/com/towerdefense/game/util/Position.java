package com.towerdefense.game.util;


import com.badlogic.gdx.math.Vector3;

public class Position {

    public float x;

    public float y;

    public Position(){
        this(0f,0f);
    }

    public Position(float x, float y){
        this.x = x;
        this.y = y;
    }

    public void set(float x, float y){
        this.x = x;
        this.y = y;
    }

    public float distanceTo(Position p){
        Vector3 a = new Vector3(x, y, 0);
        return a.dst(p.x, p.y, 0);
    }
}

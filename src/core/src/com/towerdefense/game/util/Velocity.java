package com.towerdefense.game.util;


public class Velocity {

    public float x;

    public float y;

    public float maxVelocity;

    public Velocity(){
        this(0f, 0f, 0f);
    }

    public Velocity(float max){
        this(0f, 0f, max);
    }

    public Velocity(float x, float y, float max){
        this.x = x;
        this.y = y;
        maxVelocity = max;
    }

    public void set(float x, float y){
        this.x = x;
        this.y = y;
    }

    public void addX(float x){
        this.x += x;
        if(this.x > maxVelocity)
            this.x = maxVelocity;
        if(this.x < -maxVelocity)
            this.x = -maxVelocity;
    }

    public void addY(float y){
        this.y += y;
        if(this.y > maxVelocity)
            this.y = maxVelocity;
        if(this.y < -maxVelocity)
            this.y = -maxVelocity;
    }
}

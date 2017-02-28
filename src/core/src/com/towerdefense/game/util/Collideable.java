package com.towerdefense.game.util;

import com.badlogic.gdx.math.Rectangle;

public class Collideable {

    public static final int
            TURRET      = 0x1,
            FLOOR       = 0x1 << 1,
            ENTRY       = 0x1 << 2,
            EXIT        = 0x1 << 3,
            ENEMY       = 0x1 << 4,
            BULLET      = 0x1 << 5,
            WALL        = 0x1 << 6;


    public int maskThis = 0;

    public int maskOther = 0;

    public Collideable(int myself, int others){
        maskThis = myself;
        maskOther = others;
    }

    public boolean isCollidingWith(Collideable other){
        return (this.maskOther & other.maskThis) != 0;
    }

}

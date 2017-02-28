package com.towerdefense.game.screen.hud;


import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class HUD extends Stage{

    abstract public void resize(int width, int height);

    public Rectangle getBound(){
        return new Rectangle(getViewport().getScreenX(), getViewport().getScreenY(), getViewport().getScreenWidth(), getViewport().getScreenHeight());
    }
}

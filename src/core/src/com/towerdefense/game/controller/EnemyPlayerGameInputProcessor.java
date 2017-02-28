package com.towerdefense.game.controller;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.towerdefense.game.Game;
import com.towerdefense.game.entity.EnemyPlayer;
import com.towerdefense.game.screen.GameScreen;
import com.towerdefense.game.system.action.UpdateVelocityAction;

public class EnemyPlayerGameInputProcessor implements InputProcessor{

    private GameScreen screen;

    private EnemyPlayer player;

    private boolean moveUp, moveDown, moveRight, moveLeft;

    public EnemyPlayerGameInputProcessor(GameScreen screen, EnemyPlayer player){
        this.screen = screen;
        this.player = player;

        moveUp = moveRight = moveLeft = moveDown = false;
    }


    @Override
    public boolean keyDown(int keycode) {
        boolean updated = false;
        switch(keycode){
            case Input.Keys.Z:
                moveUp = true;
                updated = true;
                break;
            case Input.Keys.S:
                moveDown = true;
                updated = true;
                break;
            case Input.Keys.Q:
                moveLeft = true;
                updated = true;
                break;
            case Input.Keys.D:
                moveRight = true;
                updated = true;
                break;
        }

        if(updated){
            Game.gameLoop.actionManager.addAction(new UpdateVelocityAction(moveUp, moveDown, moveLeft, moveRight, player.getID()));
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        boolean updated = false;
        switch(keycode){
            case Input.Keys.Z:
                moveUp = false;
                updated = true;
                break;
            case Input.Keys.S:
                moveDown = false;
                updated = true;
                break;
            case Input.Keys.Q:
                moveLeft = false;
                updated = true;
                break;
            case Input.Keys.D:
                moveRight = false;
                updated = true;
                break;
        }

        if(updated){
            Game.gameLoop.actionManager.addAction(new UpdateVelocityAction(moveUp, moveDown, moveLeft, moveRight, player.getID()));
            return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}

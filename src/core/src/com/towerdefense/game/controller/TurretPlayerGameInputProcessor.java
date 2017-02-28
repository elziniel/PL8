package com.towerdefense.game.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.towerdefense.game.Game;
import com.towerdefense.game.entity.Tile;
import com.towerdefense.game.entity.TurretPlayer;
import com.towerdefense.game.entity.TurretTile;
import com.towerdefense.game.screen.GameScreen;
import com.towerdefense.game.screen.hud.TurretHUD;
import com.towerdefense.game.system.action.AddTurretAction;
import com.towerdefense.game.util.Cst;

public class TurretPlayerGameInputProcessor implements InputProcessor{

    private Vector3 currentTouchPosition;

    private GameScreen screen;

    private TurretPlayer player;

    public TurretPlayerGameInputProcessor(GameScreen screen, TurretPlayer player){
        this.screen = screen;
        this.player = player;

        currentTouchPosition = new Vector3(0,0,0);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
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
        if(screen.getGameStage().getBound().contains(screenX, screenY) && button == Input.Buttons.LEFT){
            currentTouchPosition.set(screenX, screenY, 0);
            screen.getGameStage().unproject(currentTouchPosition);
            int tileX = (int)(currentTouchPosition.x / (Cst.TILE_WIDTH * Cst.WORLD_TO_GRAPHIC));
            int tileY = (int)(currentTouchPosition.y / (Cst.TILE_HEIGHT * Cst.WORLD_TO_GRAPHIC));

            Tile focus = Game.gameLoop.entityManager.getTiles()[tileX][tileY];
            GameScreen gameScreen = (GameScreen) Game.gameUI.getScreen();
            if(focus instanceof TurretTile){
                ((TurretHUD) gameScreen.getHud()).setSelectedTurret((TurretTile) focus);
            }else{
                ((TurretHUD) gameScreen.getHud()).setSelectedTurret(null);
                Game.gameLoop.actionManager.addAction(new AddTurretAction(tileX, tileY));
            }
            return true;
        }
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

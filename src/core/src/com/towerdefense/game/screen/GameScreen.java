package com.towerdefense.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.towerdefense.game.Game;
import com.towerdefense.game.GameUI;
import com.towerdefense.game.controller.EnemyPlayerGameInputProcessor;
import com.towerdefense.game.controller.TurretPlayerGameInputProcessor;
import com.towerdefense.game.entity.EnemyPlayer;
import com.towerdefense.game.entity.TurretPlayer;
import com.towerdefense.game.screen.game.GameStage;
import com.towerdefense.game.screen.hud.EnemyHUD;
import com.towerdefense.game.screen.hud.HUD;
import com.towerdefense.game.screen.hud.TurretHUD;
import com.towerdefense.game.system.GameLoop;
import com.towerdefense.game.system.action.SpawnEnemyAction;
import com.towerdefense.game.util.Cst;


/**
 * Ecran d'une partie de jeu
 */
public class GameScreen implements Screen{

    private GameStage gameStage;

    private HUD hud;


    public GameScreen(){
        Game.gameLoop.gameScreen = this;
    }


    @Override
    public void show() {
        Game.gameUI = (GameUI) Gdx.app.getApplicationListener();

        // Game stage
        gameStage = new GameStage(this);

        // Inputs processors
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(Cst.allScreenInputController);
        if(Game.gameLoop.player instanceof TurretPlayer) {
            hud = new TurretHUD(this);
            multiplexer.addProcessor(new TurretPlayerGameInputProcessor(this, (TurretPlayer) Game.gameLoop.player));
            multiplexer.addProcessor(hud);
        }
        else if(Game.gameLoop.player instanceof EnemyPlayer) {
            hud = new EnemyHUD(this);
            multiplexer.addProcessor(new EnemyPlayerGameInputProcessor(this, (EnemyPlayer) Game.gameLoop.player));
            multiplexer.addProcessor(hud);
        }
        Gdx.input.setInputProcessor(multiplexer);

        if(Game.gameLoop.player instanceof EnemyPlayer) {
            Game.gameLoop.actionManager.addAction(new SpawnEnemyAction(Game.gameLoop.player.getID()));
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Game stage
        gameStage.act(delta);
        gameStage.draw();

        // HUD
        hud.act(delta);
        hud.draw();
    }

    @Override
    public void resize(int width, int height) {
        gameStage.resize(width, height);
        hud.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        gameStage.dispose();
        hud.dispose();
    }

    public GameStage getGameStage(){
        return gameStage;
    }

    public HUD getHud(){
        return hud;
    }
}

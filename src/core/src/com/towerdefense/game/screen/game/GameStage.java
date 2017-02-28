package com.towerdefense.game.screen.game;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.towerdefense.game.Game;
import com.towerdefense.game.screen.GameScreen;
import com.towerdefense.game.system.GameLoop;
import com.towerdefense.game.util.Cst;

// https://heerbann.com/?p=11

public class GameStage extends Stage{

    private GameScreen gameScreen;

    public GameStage(GameScreen gameScreen){
        super(
                new FitViewport(
                        Game.gameLoop.entityManager.getMap().getWidth(),
                        Game.gameLoop.entityManager.getMap().getHeight()));

        this.gameScreen = gameScreen;

        ((OrthographicCamera) getCamera()).zoom = Cst.WORLD_TO_GRAPHIC;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        Game.gameLoop.update(delta);
    }

    @Override
    public void draw() {
        getViewport().apply(true);
        getCamera().position.scl(((OrthographicCamera) getCamera()).zoom);
        getCamera().update();

        getBatch().setProjectionMatrix(getCamera().combined);

        getBatch().begin();
        Game.gameLoop.render(getBatch());
        getBatch().end();
    }

    public void resize(int width, int height){
        getViewport().update(width - Cst.HUD_WIDTH, height, true);
    }

    public Rectangle getBound(){
        return new Rectangle(getViewport().getScreenX(), getViewport().getScreenY(), getViewport().getScreenWidth(), getViewport().getScreenHeight());
    }

    public Vector3 unproject(Vector3 vector){
        return getCamera().unproject(vector, getViewport().getScreenX(), getViewport().getScreenY(), getViewport().getScreenWidth(), getViewport().getScreenHeight());
    }
}

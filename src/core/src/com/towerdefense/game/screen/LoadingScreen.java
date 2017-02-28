package com.towerdefense.game.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.towerdefense.game.Game;
import com.towerdefense.game.GameUI;
import com.towerdefense.game.util.Assets;
import com.towerdefense.game.util.Cst;

/**
 * Screen pour charger les assets du jeu avec une barre de chargement pour le suivi
 */
public class LoadingScreen implements Screen{

    public static final String LOG = "LoadingScreen";

    /**
     *  Stage pour placer la barre de chargement
     */
    private Stage stage;

    /**
     * Barre de chargement
     */
    private ProgressBar bar;

    @Override
    public void show() {
        stage = new Stage();
        bar = new ProgressBar(0f, 100f, 1f, false, Assets.uiSkin, "default");

        Table tableStage = new Table();
        tableStage.setFillParent(true);
        tableStage.add(bar).center();

        stage.addActor(tableStage);

        Assets.init();

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(Cst.allScreenInputController);
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(Assets.manager.update()){
            Assets.updateConstantes();
            Game.gameUI.setScreen(new TwitchScreen());
        }

        stage.setDebugAll(Cst.DEBUG);

        bar.setValue(Assets.manager.getProgress() * 100);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
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
        stage.dispose();
    }
}

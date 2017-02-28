package com.towerdefense.game.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.towerdefense.game.Game;
import com.towerdefense.game.GameUI;
import com.towerdefense.game.util.Assets;
import com.towerdefense.game.util.Cst;

public class OptionsScreen implements Screen{

    private Stage stage;

    @Override
    public void show() {

        TextButton buttonBack = new TextButton("Retour", Assets.uiSkin);
        buttonBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.gameUI.setScreen(new TwitchScreen());
            }
        });

        TextButton buttonTexturePacks = new TextButton("Pack de texture", Assets.uiSkin);
        buttonTexturePacks.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.gameUI.setScreen(new TexturePacksScreen());
            }
        });

        Table table = new Table(Assets.uiSkin);
        table.pad(30);
        table.setFillParent(true);
        table.add("OPTIONS").expandX().pad(40);
        table.row();
        table.add(buttonTexturePacks).padBottom(20).width(200);
        table.row();
        table.add(buttonBack).bottom().left().expandY();

        stage = new Stage();
        stage.addActor(table);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(Cst.allScreenInputController);
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.setDebugAll(Cst.DEBUG);

        stage.act(delta);
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

package com.towerdefense.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.towerdefense.game.Game;
import com.towerdefense.game.GameUI;
import com.towerdefense.game.net.TwitchClient;
import com.towerdefense.game.util.Assets;
import com.towerdefense.game.util.Cst;

public class TwitchConnectionScreen implements Screen {
    private Stage stage;
    private Table table;
    private Label label;
    private TextButton cancelButton;
    private Thread thread;

    private String player;
    private boolean online;

    public TwitchConnectionScreen(boolean b) {
        online = b;
    }

    public void setLabel(String s) {
        label.setText(s);
    }
    public String getLabel() {
        return label.getText().toString();
    }

    public void setButton(String s) {
        cancelButton.setText(s);
    }

    public void setPlayer(String s) {
        player = s;
    }

    public boolean getOnline() {
        return online;
    }

    @Override
    public void show() {
        stage = new Stage();

        thread = new Thread(new TwitchClient(this));
        thread.start();

        cancelButton = new TextButton("Annuler", Assets.uiSkin);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                thread.interrupt();
                if (cancelButton.getText().toString().equals("Accepter")) {
                    Game.gameUI.setScreen(new SelectServerScreen(player));
                } else {
                    Game.gameUI.setScreen(new TwitchScreen());
                }
            }
        });

        label = new Label("Tentative de connexion...", Assets.uiSkin);

        table = new Table(Assets.uiSkin);
        table.setFillParent(true);
        table.add(label).padBottom(10).width(200);
        table.row();
        table.add(cancelButton).padBottom(10).width(200);

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

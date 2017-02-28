package com.towerdefense.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.towerdefense.game.Game;
import com.towerdefense.game.GameUI;
import com.towerdefense.game.entity.EnemyPlayer;
import com.towerdefense.game.entity.TurretPlayer;
import com.towerdefense.game.net.Server;
import com.towerdefense.game.util.Assets;
import com.towerdefense.game.util.Cst;

import java.net.InetSocketAddress;


public class SelectServerScreen implements Screen {

    private String pseudo;

    private Stage stage;

    private TextField textFieldServerByIP;

    public SelectServerScreen(String pseudo){
        this.pseudo = pseudo;
    }

    @Override
    public void show() {
        stage = new Stage();

        Label labelTitle = new Label("Selection du serveur".toUpperCase(), Assets.uiSkin);
        labelTitle.setFontScale(2);

        Label labelServerByIP = new Label("Adresse du serveur", Assets.uiSkin);
        textFieldServerByIP = new TextField("127.0.0.1:"+Server.PORT, Assets.uiSkin);
        TextButton buttonServerByIPTurret = new TextButton("Go en joueur tourelle", Assets.uiSkin);
        buttonServerByIPTurret.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.gameUI.setScreen(new ConnectionGameServerScreen(getInetSocketAddress(), new TurretPlayer(pseudo), true));
            }
        });

        TextButton buttonServerByIPEnemy = new TextButton("Go en joueur enemy", Assets.uiSkin);
        buttonServerByIPEnemy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.gameUI.setScreen(new ConnectionGameServerScreen(getInetSocketAddress(), new EnemyPlayer(pseudo), false));
            }
        });

        TextButton buttonRetour = new TextButton("Retour", Assets.uiSkin);
        buttonRetour.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.gameUI.setScreen(new TwitchScreen());
            }
        });

        Label labelInfo = new Label("Port par defaut: "+ Server.PORT, Assets.uiSkin);
        labelInfo.setColor(Color.LIGHT_GRAY);

        Table buttons = new Table(Assets.uiSkin);
        buttons.add(buttonServerByIPTurret);
        buttons.row();
        buttons.add(buttonServerByIPEnemy);

        Table table = new Table(Assets.uiSkin);
        table.setFillParent(true);
        table.pad(30);
        table.add(labelTitle).colspan(3).expandX().height(100);
        table.row();
        table.add(labelServerByIP).width(100).height(50);
        table.add(textFieldServerByIP).width(200);
        table.add(buttons);
        table.row();
        table.add(labelInfo).colspan(3);
        table.row();
        table.add(buttonRetour).colspan(3).bottom().left();

        stage.addActor(table);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(Cst.allScreenInputController);
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    private InetSocketAddress getInetSocketAddress(){
        String tmp = textFieldServerByIP.getText().toString();

        String[] split = tmp.split(":");
        int port = Server.PORT;
        if (split.length > 1)
            port = Integer.parseInt(split[1]);

        return new InetSocketAddress(split[0], port);
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

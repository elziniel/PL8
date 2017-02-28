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
import com.towerdefense.game.entity.EnemyPlayer;
import com.towerdefense.game.entity.TurretPlayer;
import com.towerdefense.game.util.Assets;
import com.towerdefense.game.util.Cst;

import java.net.InetSocketAddress;

public class SelectPlayerModeScreen implements Screen{

    private Stage stage;

    private TextButton buttonClientTurret;
    private TextButton buttonClientEnemy;

    private String player = null;

    private InetSocketAddress adresseServer;

    public SelectPlayerModeScreen(InetSocketAddress adresseServer, String player){
        this.player = player;
        this.adresseServer = adresseServer;
    }


    @Override
    public void show() {
        stage = new Stage();

        buttonClientTurret = new TextButton("Joueur tourelles", Assets.uiSkin);
        buttonClientTurret.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (player == null) {
                    Game.gameUI.setScreen(new ConnectionGameServerScreen(adresseServer, new TurretPlayer("Michel"), false));
                }
                else {
                    Game.gameUI.setScreen(new ConnectionGameServerScreen(adresseServer, new TurretPlayer(player), false));
                }
            }
        });

        buttonClientEnemy = new TextButton("Joueur ennemi", Assets.uiSkin);
        buttonClientEnemy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (player == null) {
                    Game.gameUI.setScreen(new ConnectionGameServerScreen(adresseServer, new EnemyPlayer("Chantal"), false));
                }
                else {
                    Game.gameUI.setScreen(new ConnectionGameServerScreen(adresseServer, new EnemyPlayer(player), false));
                }
            }
        });

        Table tableSelect = new Table(Assets.uiSkin);
        tableSelect.setFillParent(true);
        tableSelect.add(buttonClientTurret).padBottom(10).width(200);
        tableSelect.row();
        tableSelect.add(buttonClientEnemy).width(200);
        tableSelect.left();
        tableSelect.pad(20);

        stage.addActor(tableSelect);

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

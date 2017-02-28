package com.towerdefense.game.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.towerdefense.game.Game;
import com.towerdefense.game.entity.EnemyPlayer;
import com.towerdefense.game.entity.TurretPlayer;
import com.towerdefense.game.system.action.RequestLaunchGameAction;
import com.towerdefense.game.system.listener.GameLoopListener;
import com.towerdefense.game.util.Assets;
import com.towerdefense.game.util.Cst;

import java.util.Iterator;

public class LobbyScreen implements Screen, GameLoopListener{

    private Stage stage;

    private Label labelTurretPlayer;

    private List<String> listEnemyPlayer;

    private boolean isHost;

    public LobbyScreen(boolean isHost){
        this.isHost = isHost;
    }

    @Override
    public void show() {

        labelTurretPlayer = new Label("vide", Assets.uiSkin);

        listEnemyPlayer = new List<String>(Assets.uiSkin);

        ScrollPane scrollPane = new ScrollPane(listEnemyPlayer, Assets.uiSkin);

        TextButton buttonBack = new TextButton("Annuler", Assets.uiSkin);

        TextButton buttonLaunchGame = new TextButton("Lancer partie", Assets.uiSkin);
        buttonLaunchGame.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.gameLoop.actionManager.addAction(new RequestLaunchGameAction(Game.gameLoop.player.getID()));
            }
        });

        Table table = new Table(Assets.uiSkin);
        table.pad(30);
        table.setFillParent(true);
        table.add("LOBBY").colspan(2);
        table.row();
        table.add("Joueur tourelles").pad(10);
        table.add("Joueur ennemi").pad(10);
        table.row();
        table.add(labelTurretPlayer).pad(10);
        table.add(scrollPane).pad(10);
        table.row();
        table.add(buttonBack).bottom().left();
        if(isHost)
            table.add(buttonLaunchGame);


        stage = new Stage();
        stage.addActor(table);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(Cst.allScreenInputController);
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);

        Game.gameLoop.addGameLoopListener(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Game.gameLoop.update(delta);
        update();

        stage.setDebugAll(Cst.DEBUG);

        stage.act(delta);
        stage.draw();
    }

    private void update(){
        TurretPlayer turretPlayer = Game.gameLoop.entityManager.getTurretPlayer();
        if(turretPlayer == null){
            labelTurretPlayer.setText("vide");
        }else{
            labelTurretPlayer.setText(turretPlayer.getName());
        }

        listEnemyPlayer.clearItems();

        Iterator<EnemyPlayer> enemies = Game.gameLoop.entityManager.getEnemiesPlayer().iterator();
        Array<String> enemiesName = new Array<String>();
        while(enemies.hasNext()){
            EnemyPlayer current = enemies.next();
            enemiesName.add(current.getName());
        }

        listEnemyPlayer.setItems(enemiesName);
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

    @Override
    public void launchGame() {
        Game.gameUI.setScreen(new GameScreen());
    }
}

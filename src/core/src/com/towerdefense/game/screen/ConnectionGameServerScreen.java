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
import com.towerdefense.game.entity.Player;
import com.towerdefense.game.net.Client;
import com.towerdefense.game.system.ClientMessageManager;
import com.towerdefense.game.system.GameLoop;
import com.towerdefense.game.system.listener.ConnectionStatusListener;
import com.towerdefense.game.util.Assets;
import com.towerdefense.game.util.Cst;

import java.net.InetSocketAddress;


public class ConnectionGameServerScreen implements Screen, ConnectionStatusListener{

    private Player player;

    private Stage stage;

    private Label label;

    private TextButton buttonRetour;

    private InetSocketAddress adresseServer;

    private boolean connected;

    private boolean isHost;

    public ConnectionGameServerScreen(InetSocketAddress adresseServer, Player player, boolean isHost){
        connected = false;
        this.player = player;
        this.isHost = isHost;
        this.adresseServer = adresseServer;
        Game.gameLoop = new GameLoop();
        Game.gameLoop.initClientSide(player);

        Game.client = new Client(this.adresseServer);

        Game.client.addMessageListener((ClientMessageManager) Game.gameLoop.messageManager);
        ((ClientMessageManager) Game.gameLoop.messageManager).addSendMessageListener(Game.client);

        ((ClientMessageManager) Game.gameLoop.messageManager).addConnectionStatusListener(this);
    }

    @Override
    public void show() {
        stage = new Stage();

        buttonRetour = new TextButton("Annuler", Assets.uiSkin);
        buttonRetour.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.gameUI.setScreen(new SelectServerScreen(player.getName()));
            }
        });

        label = new Label("Connexion en cours ...", Assets.uiSkin);

        Table table = new Table(Assets.uiSkin);
        table.setFillParent(true);
        table.add(label).padBottom(20).center();
        table.row();
        table.add(buttonRetour).bottom().right();
        table.pad(20);

        stage.addActor(table);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(Cst.allScreenInputController);
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);

        new Thread(Game.client).start();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(connected && Game.gameLoop.entityManager.getMap() != null) {
            ((ClientMessageManager) Game.gameLoop.messageManager).removeConnectionStatusListener(this);
            if(Game.gameLoop.isInLobby()){
                Game.gameUI.setScreen(new LobbyScreen(isHost));
            }else{
                Game.gameUI.setScreen(new GameScreen());
            }
        }

        stage.setDebugAll(Cst.DEBUG);
        stage.act(delta);
        Game.gameLoop.update(delta);

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

    @Override
    public void connectionSuccess() {
        label.setText("Connection reussi.\nEn attente des donnees du serveur ...");
        connected = true;
    }

    @Override
    public void connectionFailure(String message) {
        Game.client.close();
        label.setText(message);
    }
}

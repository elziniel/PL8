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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.towerdefense.game.Game;
import com.towerdefense.game.util.Assets;
import com.towerdefense.game.util.Cst;

public class TwitchScreen implements Screen {

    public static final String LOG = "TwitchScreen";

    private Stage stage;
    private TextButton twitchButton, twitchLocalButton, visitorButton, optionsButton;
    private Table tableTwitch;
    private Table tablePseudo;

    @Override
    public void show() {
        stage = new Stage();

        // Table Twitch
        twitchButton = new TextButton("Connexion Twitch", Assets.uiSkin);
        twitchButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.gameUI.setScreen(new TwitchConnectionScreen(true));
            }
        });

        twitchLocalButton = new TextButton("Connexion Twitch (local)", Assets.uiSkin);
        twitchLocalButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.gameUI.setScreen(new TwitchConnectionScreen(false));
            }
        });

        visitorButton = new TextButton("Mode visiteur", Assets.uiSkin);
        visitorButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                tablePseudo.setVisible(true);
                tableTwitch.setVisible(false);
            }
        });

        optionsButton = new TextButton("Options", Assets.uiSkin);
        optionsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.gameUI.setScreen(new OptionsScreen());
            }
        });

        tableTwitch = new Table(Assets.uiSkin);
        tableTwitch.setFillParent(true);
        tableTwitch.add(twitchButton).padBottom(10).width(200);
        tableTwitch.row();
        tableTwitch.add(twitchLocalButton).padBottom(10).width(200);
        tableTwitch.row();
        tableTwitch.add(visitorButton).padBottom(40).width(200);
        tableTwitch.row();
        tableTwitch.add(optionsButton).padBottom(10).width(200);

        stage.addActor(tableTwitch);

        // Table pseudo
        Label labelPseudo = new Label("Pseudo", Assets.uiSkin);
        final TextField textFieldPseudo = new TextField("", Assets.uiSkin);
        TextButton pseudoButton = new TextButton("Valider", Assets.uiSkin);
        pseudoButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.gameUI.setScreen(new SelectServerScreen(textFieldPseudo.getText()));
            }
        });
        TextButton retourButton = new TextButton("Retour", Assets.uiSkin);
        retourButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                tableTwitch.setVisible(true);
                tablePseudo.setVisible(false);
            }
        });

        tablePseudo = new Table(Assets.uiSkin);
        tablePseudo.setFillParent(true);
        tablePseudo.add(labelPseudo).padBottom(10).width(200);
        tablePseudo.add(textFieldPseudo).padBottom(10).width(200);
        tablePseudo.row();
        tablePseudo.add();
        tablePseudo.add(pseudoButton).padBottom(10).width(200);
        tablePseudo.row();
        tablePseudo.add(retourButton).bottom().left();
        tablePseudo.pad(20);

        tablePseudo.setVisible(false);

        stage.addActor(tablePseudo);

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

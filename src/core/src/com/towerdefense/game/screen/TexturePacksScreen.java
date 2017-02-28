package com.towerdefense.game.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.towerdefense.game.Game;
import com.towerdefense.game.GameUI;
import com.towerdefense.game.util.Assets;
import com.towerdefense.game.util.TexturePackManager;
import com.towerdefense.game.util.Cst;


public class TexturePacksScreen implements Screen {

    private Stage stage;

    private List<String> texturePackList;

    @Override
    public void show() {
        texturePackList = new List<String>(Assets.uiSkin);
        texturePackList.setItems(TexturePackManager.getTexturePackAvailable());
        texturePackList.setSelected(TexturePackManager.currentTexturePack);

        ScrollPane scrollPane = new ScrollPane(texturePackList, Assets.uiSkin);

        TextButton buttonBack = new TextButton("Retour", Assets.uiSkin);
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game.gameUI.setScreen(new OptionsScreen());
            }
        });

        TextButton buttonApply = new TextButton("Appliquer", Assets.uiSkin);
        buttonApply.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                TexturePackManager.changeTexturePack(texturePackList.getSelected());
                Game.gameUI.setScreen(new LoadingScreen());
            }
        });

        Table table = new Table(Assets.uiSkin);
        table.pad(30);
        table.setFillParent(true);
        table.add("PACK DE TEXTURE").colspan(2).pad(40);
        table.row();
        table.add(scrollPane).colspan(2).expand().fillX();
        table.row();
        table.add(buttonBack);
        table.add(buttonApply);

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

package com.towerdefense.game.util;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.towerdefense.game.Game;
import com.towerdefense.game.screen.GameScreen;
import com.towerdefense.game.system.ClientMessageManager;


public class DebugUI extends Stage {

    private GameScreen gameScreen;

    private Skin skin;

    private Label latence;
    private Label playerPseudo;

    private Label[] labels;

    private boolean visibility;

    public DebugUI(GameScreen gameScreen){
        super();

        this.gameScreen = gameScreen;

        visibility = false;
        skin = Assets.uiSkin;

        latence = new Label("Latence: ?", skin);
        playerPseudo = new Label("", skin);

        labels = new Label[2];
        labels[0] = playerPseudo;
        labels[1] = latence;

        addActor(latence);
        addActor(playerPseudo);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        playerPseudo.setText("Pseudo: "+ Game.gameLoop.player.getName());
        latence.setText("Latence: "+((ClientMessageManager) Game.gameLoop.messageManager).getRTT()+" ms");

        float axeY = Gdx.graphics.getHeight();
        for (Label label : labels) {
            axeY -= 10 + label.getHeight();
            label.setPosition(10, axeY);
        }

    }

    @Override
    public void draw() {
        if(visibility)
            super.draw();
    }

    public void setVisibility(boolean b){
        visibility = b;
    }


}

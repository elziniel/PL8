package com.towerdefense.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.towerdefense.game.Game;
import com.towerdefense.game.GameUI;
import com.towerdefense.game.screen.LoadingScreen;
import com.towerdefense.game.util.Log;
import com.towerdefense.game.util.TexturePackManager;
import com.towerdefense.game.util.Cst;


public class AllScreenInputController implements InputProcessor{

    public static final String LOG = "AllScreenInput";


    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch(keycode){
            case Input.Keys.F1:
                Cst.DEBUG = !Cst.DEBUG;
                Log.l(LOG, "Debug "+((Cst.DEBUG)? "on" : "off"));
                return true;
            case Input.Keys.F2:
                Log.l(LOG, "Switch texture pack to "+ Cst.TEXTURE_DEFAULT_PACK);
                TexturePackManager.changeTexturePack(Cst.TEXTURE_DEFAULT_PACK);
                Game.gameUI.setScreen(new LoadingScreen());
                return true;
            case Input.Keys.F3:
                Log.l(LOG, "Switch texture pack to simple_texture");
                TexturePackManager.changeTexturePack("simple_textures.atlas");
                Game.gameUI.setScreen(new LoadingScreen());
                return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}

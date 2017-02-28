package com.towerdefense.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.towerdefense.game.screen.LoadingScreen;
import com.towerdefense.game.util.Assets;


public class GameUI extends Game {

	@Override
	public void create () {
		com.towerdefense.game.Game.gameUI = this;
		setScreen(new LoadingScreen());
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
		Assets.dispose();
	}

	@Override
	public void setScreen(Screen screen) {
		Screen old = getScreen();
		if(old != null)
			old.dispose();
		super.setScreen(screen);
	}
}

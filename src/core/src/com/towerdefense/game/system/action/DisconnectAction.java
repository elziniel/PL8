package com.towerdefense.game.system.action;


import com.towerdefense.game.system.GameLoop;

public class DisconnectAction extends Action{

    public static final String PARSE_VALUE = "deco";

    public DisconnectAction(){
        super(PARSE_VALUE, Send.CLIENT);
    }

    @Override
    public void execute(GameLoop gameLoop) {
    }
}

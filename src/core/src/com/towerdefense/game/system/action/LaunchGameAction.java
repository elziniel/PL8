package com.towerdefense.game.system.action;


import com.towerdefense.game.system.GameLoop;
import com.towerdefense.game.util.Log;

public class LaunchGameAction extends Action{

    public static final String PARSE_VALUE = "LaunchGame";

    public LaunchGameAction(){
        super(PARSE_VALUE, Send.SERVER_BROADCAST);
    }

    @Override
    public void execute(GameLoop gameLoop) {
        if(!gameLoop.isServerSide()){
            Log.l("LauchGameAction", "received succes launch game");
            if(gameLoop.isInLobby())
                gameLoop.setInLobby(false);
        }
    }
}

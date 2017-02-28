package com.towerdefense.game.system.action;


import com.towerdefense.game.system.GameLoop;
import com.towerdefense.game.util.Log;

public class ConnectAskAction extends Action {

    public static final String PARSE_VALUE = "ConnectAsk";

    public static final String PSEUDO = "ps";
    public static final String IS_TURRET = "it";

    public ConnectAskAction(){
        super();
    }

    public ConnectAskAction(String pseudo, boolean isTurretPlayer){
        super(PARSE_VALUE, Send.CLIENT);

        add(PSEUDO, pseudo);
        add(IS_TURRET, isTurretPlayer+"");
    }

    @Override
    public void execute(GameLoop gameLoop) {
        Log.l(PARSE_VALUE, "Request connection");
    }

    public boolean isTurretPlayer(){
        return Boolean.parseBoolean(get(IS_TURRET));
    }
}

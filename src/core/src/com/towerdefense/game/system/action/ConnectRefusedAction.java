package com.towerdefense.game.system.action;


import com.towerdefense.game.system.ClientMessageManager;
import com.towerdefense.game.system.GameLoop;
import com.towerdefense.game.util.Log;

public class ConnectRefusedAction extends Action {

    public static final String PARSE_VALUE = "ConnectRefused";

    public static final String MESSAGE = "msg";

    public ConnectRefusedAction(){
        super();
    }

    public ConnectRefusedAction(String pseudo, String message){
        super(PARSE_VALUE, pseudo);

        add(MESSAGE, message);
    }

    @Override
    public void execute(GameLoop gameLoop) {
        String message = get(MESSAGE);
        if(!gameLoop.isServerSide()){
            ((ClientMessageManager) gameLoop.messageManager).connectionRefused(message);
        }
        Log.l(PARSE_VALUE, "Connection refused: "+message);
    }

    public String getMessage(){
        return get(MESSAGE);
    }
}

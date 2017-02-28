package com.towerdefense.game.system.action;


import com.towerdefense.game.system.ClientMessageManager;
import com.towerdefense.game.system.GameLoop;

public class RTTRespondAction extends Action {

    public static final String PARSE_VALUE = "RTTRespond";

    public static final String TIME = "t";

    public RTTRespondAction(){
        super();
    }

    public RTTRespondAction(String pseudo, long time){
        super(PARSE_VALUE, pseudo);

        add(TIME, time+"");
    }

    @Override
    public void execute(GameLoop gameLoop) {
        if(!gameLoop.isServerSide()){
            long time = Long.parseLong(get(TIME));
            ((ClientMessageManager) gameLoop.messageManager).setRTT(System.currentTimeMillis() - time);
        }
    }
}

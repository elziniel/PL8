package com.towerdefense.game.system.action;

import com.towerdefense.game.system.GameLoop;


public class RTTAskAction extends Action {

    public static final String PARSE_VALUE = "RTTAsk";

    public static final String TIME = "t";
    public static final String PSEUDO = "ps";

    public RTTAskAction(){
        super();
    }

    public RTTAskAction(String pseudo, long time){
        super(PARSE_VALUE, Send.CLIENT);

        add(TIME, time + "");
        add(PSEUDO, pseudo);
    }

    @Override
    public void execute(GameLoop gameLoop) {
        if(gameLoop.isServerSide()){
            long time = Long.parseLong(get(TIME));
            String ps = get(PSEUDO);
            gameLoop.actionManager.addAction(new RTTRespondAction(ps, time));
        }
    }
}

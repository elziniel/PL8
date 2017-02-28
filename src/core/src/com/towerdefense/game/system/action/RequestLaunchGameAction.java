package com.towerdefense.game.system.action;

import com.towerdefense.game.system.GameLoop;


public class RequestLaunchGameAction extends Action {

    public static final String PARSE_VALUE = "RequestLaunchGame";

    public static final String ID_PLAYER = "ip";

    public RequestLaunchGameAction(){
        super();
    }

    public RequestLaunchGameAction(int idPlayer){
        super(PARSE_VALUE, Send.CLIENT);

        add(ID_PLAYER, idPlayer+"");
    }

    @Override
    public void execute(GameLoop gameLoop) {
        if(gameLoop.isServerSide()){
            int idPlayer = Integer.parseInt(get(ID_PLAYER));

            if(!gameLoop.isInLobby())
                return;

            gameLoop.setInLobby(false);
            gameLoop.actionManager.addAction(new LaunchGameAction());
        }
    }
}

package com.towerdefense.game.system;


import com.towerdefense.game.system.action.Action;

import java.util.LinkedList;

public class ActionManager {

    public static final String LOG = "ActionManager";

    private GameLoop gameLoop;

    private final LinkedList<Action> bufferAction;

    public ActionManager(GameLoop gameLoop){
        this.gameLoop = gameLoop;
        bufferAction = new LinkedList<Action>();
    }

    public void addAction(Action action){
        synchronized (bufferAction){
            if(action != null)
                bufferAction.add(action);
        }
    }

    public void addAction(String message) {
        addAction(Action.decode(message));
    }

    public void executeActions(){
        synchronized (bufferAction){
            while(!bufferAction.isEmpty()){
                Action a = bufferAction.getFirst();
                executeAction(a);
                bufferAction.remove(a);
            }
        }
    }

    private void executeAction(Action action){
        action.execute(gameLoop);

        if(gameLoop.isServerSide() && action.requireSending().equals(Action.Send.SERVER))
            ((ServerMessageManager) gameLoop.messageManager).sendMessage(action.encode(), action.getPseudo());
        else if(gameLoop.isServerSide() && action.requireSending().equals(Action.Send.SERVER_BROADCAST))
            ((ServerMessageManager) gameLoop.messageManager).broadcastMessage(action.encode());
        else if(!gameLoop.isServerSide() && action.requireSending().equals(Action.Send.CLIENT))
            ((ClientMessageManager) gameLoop.messageManager).sendMessage(action.encode());
    }

    /*
    public static Class getType(String message){
        String[] split = message.split(Var.ACTION_SEPARATOR);
        if(split.length < 1)
            return null;

        if(split[0].equals(AddTurretAction.PARSE_TAG))
            return AddTurretAction.class;
        if(split[0].equals(MaxVelocityAction.PARSE_TAG))
            return MaxVelocityAction.class;
        if(split[0].equals(ZeroVelocityAction.PARSE_TAG))
            return ZeroVelocityAction.class;
        if(split[0].equals(SpawnEnemyAction.PARSE_TAG))
            return SpawnEnemyAction.class;
        if(split[0].equals(ConnectAskAction.PARSE_TAG))
            return ConnectAskAction.class;
        if(split[0].equals(ConnectGrantedAction.PARSE_TAG))
            return ConnectGrantedAction.class;
        if(split[0].equals(ConnectRefusedAction.PARSE_TAG))
            return ConnectRefusedAction.class;
        if(split[0].equals(RTTAskAction.PARSE_TAG))
            return RTTAskAction.class;
        if(split[0].equals(RTTRespondAction.PARSE_TAG))
            return RTTRespondAction.class;
        if(split[0].equals(UpdateAction.PARSE_TAG))
            return UpdateAction.class;
        if(split[0].equals(ShootBulletAction.PARSE_TAG))
            return ShootBulletAction.class;
        if(split[0].equals(UpdateTurretPlayerAction.PARSE_TAG))
            return UpdateTurretPlayerAction.class;

        return null;
    }*/
}

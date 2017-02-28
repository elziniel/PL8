package com.towerdefense.game.system;


import com.towerdefense.game.Game;
import com.towerdefense.game.entity.Enemy;
import com.towerdefense.game.entity.EnemyPlayer;
import com.towerdefense.game.entity.Player;
import com.towerdefense.game.entity.TurretPlayer;
import com.towerdefense.game.net.listener.SocketChannelListener;
import com.towerdefense.game.screen.MessageScreen;
import com.towerdefense.game.system.listener.MessageManagerListener;
import com.towerdefense.game.system.action.ConnectAskAction;
import com.towerdefense.game.system.action.RTTAskAction;
import com.towerdefense.game.system.listener.ConnectionStatusListener;
import com.towerdefense.game.util.Log;
import com.towerdefense.game.util.Cst;

import java.nio.channels.SelectionKey;

import javax.swing.event.EventListenerList;

public class ClientMessageManager extends MessageManager implements SocketChannelListener {

    public static final String LOG = "ClientMessageManager";

    private EventListenerList listeners = new EventListenerList();

    private boolean isConnectionAccepted;

    private Player player;

    private long rtt;

    private boolean runRTTUpdate;


    public ClientMessageManager(GameLoop gameLoop, Player player) {
        super(gameLoop);
        isConnectionAccepted = false;
        this.player = player;
    }

    public void sendMessage(String message){
        message = player.getName()+ Cst.SERVER_SEPARATOR +message+"\n";
        fireSendMessage(message);
    }

    @Override
    public void newMessage(String message) {
        gameLoop.actionManager.addAction(message);
    }

    public void connectionGranted(int newPlayerID, int idEnemy){
        player.setID(newPlayerID);
        if(player instanceof EnemyPlayer){
            ((EnemyPlayer) player).enemyID = idEnemy;

            Enemy e = new Enemy(0,0, player.getName());
            e.setID(idEnemy);
            gameLoop.entityManager.add(e);
        }
        isConnectionAccepted = true;
        runRTTUpdate = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                RTTUpdate();
            }
        }).start();

        fireConnectionSuccess();
    }

    public void connectionRefused(String message){
        isConnectionAccepted = false;
        fireConnectionFailure(message);
    }

    @Override
    public void newMessage(SelectionKey key, String message) {}

    @Override
    public void newAccept(SelectionKey key) {}

    @Override
    public void connectSucced() {
        Log.d(LOG, "Connected TCP with the server");
        gameLoop.actionManager.addAction(new ConnectAskAction(player.getName(), player instanceof TurretPlayer));
    }

    @Override
    public void connectionClosed(SelectionKey key) {
        runRTTUpdate = false;
        Log.d(LOG, "Connection with server lost");
        Game.gameLoop.setShutdownMessage("Connection avec le serveur perdue");
    }

    public void addSendMessageListener(MessageManagerListener listener){
        listeners.add(MessageManagerListener.class, listener);
    }

    public void removeSendMessageListener(MessageManagerListener listener){
        listeners.remove(MessageManagerListener.class, listener);
    }

    public void addConnectionStatusListener(ConnectionStatusListener listener){
        listeners.add(ConnectionStatusListener.class, listener);
    }

    public void removeConnectionStatusListener(ConnectionStatusListener listener){
        listeners.remove(ConnectionStatusListener.class, listener);
    }

    private void fireSendMessage(String message){
        for(MessageManagerListener listener : listeners.getListeners(MessageManagerListener.class))
            listener.sendMessage(message);
    }

    public void fireConnectionSuccess(){
        for(ConnectionStatusListener listener : listeners.getListeners(ConnectionStatusListener.class))
            listener.connectionSuccess();
    }

    private void fireConnectionFailure(String message){
        for(ConnectionStatusListener listener : listeners.getListeners(ConnectionStatusListener.class))
            listener.connectionFailure(message);
    }

    public long getRTT(){
        return rtt;
    }

    public void setRTT(long rtt){
        this.rtt = rtt;
    }

    private void RTTUpdate(){
        while(runRTTUpdate){
            gameLoop.actionManager.addAction(new RTTAskAction(player.getName(), System.currentTimeMillis()));
            try {
                Thread.sleep(Cst.RTT_SLEEP);
            } catch (InterruptedException e) {
                Log.e(LOG, "Error sleep RTT delay", e);
            }
        }
    }
}

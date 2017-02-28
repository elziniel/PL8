package com.towerdefense.game.system;



import com.towerdefense.game.entity.Enemy;
import com.towerdefense.game.entity.EnemyPlayer;
import com.towerdefense.game.entity.TurretPlayer;
import com.towerdefense.game.net.listener.SocketChannelListener;
import com.towerdefense.game.system.action.Action;
import com.towerdefense.game.system.action.DisconnectAction;
import com.towerdefense.game.system.listener.MessageManagerListener;
import com.towerdefense.game.system.action.ConnectAskAction;
import com.towerdefense.game.system.action.ConnectGrantedAction;
import com.towerdefense.game.system.action.ConnectRefusedAction;
import com.towerdefense.game.util.Log;
import com.towerdefense.game.util.Cst;

import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.event.EventListenerList;

public class ServerMessageManager extends MessageManager implements SocketChannelListener {

    public static final String LOG = "ServerMessageManager";

    private ArrayList<SelectionKey> clientsAccepted;

    private HashMap<SelectionKey, String> clientsConnected;

    private EventListenerList listeners = new EventListenerList();

    public ServerMessageManager(GameLoop gameLoop){
        super(gameLoop);

        clientsAccepted = new ArrayList<SelectionKey>();
        clientsConnected = new HashMap<SelectionKey, String>();
    }

    @Override
    public void newAccept(SelectionKey key) {
        Log.d(LOG, "New socketChannel added to accepted clients");
        clientsAccepted.add(key);
    }

    @Override
    public void connectSucced() {}

    @Override
    public void connectionClosed(SelectionKey key) {
        Log.l(LOG, "Client disconnected: " + clientsConnected.get(key));

        EnemyPlayer p = gameLoop.entityManager.getEnemyPlayerByName(clientsConnected.get(key));
        if(p != null){
            int enemyID = p.enemyID;
            ((Enemy) gameLoop.entityManager.getEntityByID(enemyID)).setAlive(false);
        }else{
            TurretPlayer t = gameLoop.entityManager.getTurretPlayer();
            if(t.getName().equals(clientsConnected.get(key))){
                for(Map.Entry<SelectionKey, String> entry : clientsConnected.entrySet()){
                    entry.getKey().cancel();
                }

                Log.l(LOG, "Server closing because turret player disconnected");
                System.exit(0);
            }
        }

        clientsConnected.remove(key);
    }

    @Override
    public void newMessage(SelectionKey key, String message) {
        //Log.d(LOG, "Message received: "+message);
        String[] split = message.split(Cst.SERVER_SEPARATOR);

        if(split.length != 2)
            return;

        String pseudo = split[0];
        String content = split[1];

        String type = Action.getActionType(content);

        // En etat d'acceptation
        if(clientsAccepted.contains(key)){
            Log.d(LOG, "Accepted client");

            // Message de type ConnectAsk
            if(type != null && type.equals(ConnectAskAction.PARSE_VALUE)){
                ConnectAskAction action = (ConnectAskAction) Action.decode(content);
                if(action == null)
                    return;

                // Connexion refusée
                if(clientsConnected.containsValue(pseudo)){
                    clientsConnected.put(key, pseudo);
                    gameLoop.actionManager.addAction(new ConnectRefusedAction(pseudo, "Joueur avec ce pseudo deja connecte"));
                    clientsConnected.remove(key);
                }else if(!pseudo.equals(action.get(ConnectAskAction.PSEUDO))){
                    clientsConnected.put(key, pseudo);
                    gameLoop.actionManager.addAction(new ConnectRefusedAction(pseudo, "Le pseudo ne correspond pas au pseudo du joueur"));
                    clientsConnected.remove(key);
                }else if(action.isTurretPlayer() && gameLoop.entityManager.getTurretPlayer() != null){
                    clientsConnected.put(key, pseudo);
                    gameLoop.actionManager.addAction(new ConnectRefusedAction(pseudo, "Il y a deja un joueur tourelle connecte"));
                    clientsConnected.remove(key);

                    // Connexion acceptée
                }else{
                    clientsConnected.put(key, pseudo);
                    gameLoop.actionManager.addAction(new ConnectGrantedAction(pseudo, action.isTurretPlayer()));
                }
                clientsAccepted.remove(key);
            }
        }

        // Client déjà accepté
        else if(clientsConnected.containsKey(key) && pseudo.equals(clientsConnected.get(key))){
            if(type != null && type.equals(DisconnectAction.PARSE_VALUE)){
                connectionClosed(key);
                key.cancel();
            }else {
                gameLoop.actionManager.addAction(content);
            }
        }
    }

    @Override
    public void newMessage(String message) {}

    public void sendMessage(String message, String pseudo){
        if(!clientsConnected.containsValue(pseudo))
            return;

        SelectionKey key = null;
        for (Map.Entry<SelectionKey, String> entry : clientsConnected.entrySet()) {
            if (entry.getValue().equals(pseudo)) {
                key = entry.getKey();
                break;
            }
        }

        if(key == null)
            return;

        fireSendMessage(key, message+"\n");
    }

    public void broadcastMessage(String message){
        for(SelectionKey key : clientsConnected.keySet()){
            fireSendMessage(key, message+"\n");
        }
    }

    public void addSendMessageListener(MessageManagerListener listener){
        listeners.add(MessageManagerListener.class, listener);
    }

    public void removeSendMessageListener(MessageManagerListener listener){
        listeners.remove(MessageManagerListener.class, listener);
    }

    private void fireSendMessage(SelectionKey key, String message){
        for(MessageManagerListener listener : listeners.getListeners(MessageManagerListener.class))
            listener.sendMessage(key, message);
    }

    private void fireBroadcastMessage(String message){
        for(MessageManagerListener listener : listeners.getListeners(MessageManagerListener.class))
            listener.broadcastMessage(message);
    }
}

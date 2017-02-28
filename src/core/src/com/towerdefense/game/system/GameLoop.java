package com.towerdefense.game.system;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.towerdefense.game.Game;
import com.towerdefense.game.entity.Enemy;
import com.towerdefense.game.entity.EnemyPlayer;
import com.towerdefense.game.entity.Entity;
import com.towerdefense.game.entity.EntryTile;
import com.towerdefense.game.entity.ExitTile;
import com.towerdefense.game.entity.FloorTile;
import com.towerdefense.game.entity.Map;
import com.towerdefense.game.entity.Player;
import com.towerdefense.game.entity.Tile;
import com.towerdefense.game.screen.GameScreen;
import com.towerdefense.game.screen.MessageScreen;
import com.towerdefense.game.system.action.UpdateAction;
import com.towerdefense.game.system.listener.GameLoopListener;
import com.towerdefense.game.util.Cst;

import java.util.ArrayList;

import javax.swing.event.EventListenerList;

public class GameLoop {

    private final static String LOG = "GameLoop";

    public RenderManager renderManager;

    public EntityManager entityManager;

    public ActionManager actionManager;

    public MovementManager movementManager;

    public MessageManager messageManager;

    public GameScreen gameScreen;

    public Player player;

    private ArrayList<Entity>[] lastTwoUpdates;

    private boolean isServer;

    private float timeSinceLastUpdate;

    private boolean updatedByServer;

    private boolean inLobby;

    private String shutdownMessage;

    private int payday;

    private EventListenerList listeners = new EventListenerList();

    public GameLoop(){
        payday = 0;
    }

    public void initServerSide(){
        this.isServer = true;
        inLobby = true;
        shutdownMessage = null;

        messageManager = new ServerMessageManager(this);

        renderManager = new RenderManager(this);
        actionManager = new ActionManager(this);
        movementManager = new MovementManager(this);
        entityManager = new EntityManager(this);

        // Map
        entityManager.add(new Map(20,15));

        // Init tiles map
        Tile[][] tiles = entityManager.getTiles();
        for(int i = 0; i < tiles.length; i++){
            for(int j = 0; j < tiles[i].length; j++){
                if(i == 0 && j == 0)
                    entityManager.add(new EntryTile(i * Cst.TILE_WIDTH, j * Cst.TILE_HEIGHT));
                else if(i == tiles.length-1 && j == tiles[i].length -1)
                    entityManager.add(new ExitTile(i * Cst.TILE_WIDTH, j * Cst.TILE_HEIGHT));
                else
                    entityManager.add(new FloorTile(i * Cst.TILE_WIDTH, j * Cst.TILE_HEIGHT));
            }
        }

        timeSinceLastUpdate = 0f;
    }

    public void initClientSide(Player player){
        this.isServer = false;
        inLobby = true;
        shutdownMessage = null;

        messageManager = new ClientMessageManager(this, player);

        renderManager = new RenderManager(this);
        actionManager = new ActionManager(this);
        movementManager = new MovementManager(this);
        entityManager = new EntityManager(this);

        this.player = player;

        entityManager.add(player);

        updatedByServer = false;

        lastTwoUpdates = new ArrayList[2];
    }

    public void update(float deltaTime){
        if(!isServer && shutdownMessage != null)
            Game.gameUI.setScreen(new MessageScreen(shutdownMessage));

        if(isServer) {
            timeSinceLastUpdate += deltaTime;
            if(timeSinceLastUpdate > Cst.GAMELOOP_UPDATE_RATE) {
                actionManager.addAction(new UpdateAction(this));
                if (payday >= 9 && !isInLobby()) {
                    if (entityManager.getTurretPlayer() != null) {
                        entityManager.getTurretPlayer().setMoney(entityManager.getTurretPlayer().getMoney()+10);
                    }
                    for (EnemyPlayer ep : entityManager.getEnemiesPlayer()) {
                        ep.setMoney(ep.getMoney()+10);
                    }
                    payday = 0;
                }
                timeSinceLastUpdate = 0f;
                payday++;
            }
        }

        if(updatedByServer){
            updateByServer();
            updatedByServer = false;
        }

        entityManager.act(deltaTime);

        actionManager.executeActions();

        movementManager.updateAll(deltaTime);
    }

    public void render(Batch batch){
        // Si c'est un serveur, alors pas de rendu
        if(isServer)
            return;

        renderManager.renderAll(batch);
    }

    public void addNewUpdateEntities(ArrayList<Entity> list){
        lastTwoUpdates[0] = lastTwoUpdates[1];
        lastTwoUpdates[1] = list;
    }

    private void updateByServer(){
        for(Entity copy : lastTwoUpdates[0]){
            Entity correspondance = entityManager.getEntityByID(copy.getID());
            if(correspondance != null){
                correspondance.update(copy);
                correspondance.setUpdated(true);
            }else{
                entityManager.add(copy);
                copy.setUpdated(true);
            }
        }

        if(player instanceof EnemyPlayer){
            EnemyPlayer p = (EnemyPlayer) player;
            Enemy enemy = (Enemy) entityManager.getEntityByID(((EnemyPlayer) player).enemyID);

            for(Entity copy: lastTwoUpdates[1]){
                if(copy.getID() == p.getID()){
                    p.update(copy);
                    p.setUpdated(true);
                }else if(copy.getID() == enemy.getID()){
                    enemy.update(copy);
                    enemy.setUpdated(true);
                }
            }
        }

        entityManager.resetUpdated();
    }

    public boolean isServerSide(){
        return isServer;
    }

    public void setUpdatedByServer(boolean b){
        if(lastTwoUpdates[0] == null)
            updatedByServer = false;
        else
            updatedByServer = b;
    }

    public boolean isInLobby(){
        return inLobby;
    }

    public void setInLobby(boolean b){
        inLobby = b;
        if(!inLobby)
            fireLaunchGame();
    }

    public void setShutdownMessage(String message){
        shutdownMessage = message;
    }



    /*
        LISTENERS
     */

    public void addGameLoopListener(GameLoopListener listener){
        listeners.add(GameLoopListener.class, listener);
    }

    public void removeGameLoopListener(GameLoopListener listener){
        listeners.remove(GameLoopListener.class, listener);
    }

    private void fireLaunchGame(){
        for(GameLoopListener listener : listeners.getListeners(GameLoopListener.class))
            listener.launchGame();
    }
}

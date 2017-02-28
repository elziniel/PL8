package com.towerdefense.game.system.action;


import com.towerdefense.game.entity.Bullet;
import com.towerdefense.game.entity.Enemy;
import com.towerdefense.game.entity.EnemyPlayer;
import com.towerdefense.game.entity.Entity;
import com.towerdefense.game.entity.Map;
import com.towerdefense.game.entity.Tile;
import com.towerdefense.game.system.GameLoop;
import com.towerdefense.game.util.Cst;

import java.util.ArrayList;

public class UpdateAction extends Action {

    public static final String PARSE_VALUE = "UpdateAction";

    public static final String LOG = "UpdateAction";

    public static final String MEMORY = "mm";

    public UpdateAction(){
        super();
    }

    public UpdateAction(GameLoop gameLoop){
        super(PARSE_VALUE, Send.SERVER_BROADCAST);
    }

    @Override
    public void execute(GameLoop gameLoop) {
        if(gameLoop.isServerSide()){
            StringBuilder builder = new StringBuilder();

            // Map
            Map map = gameLoop.entityManager.getMap();
            builder.append(map.encode());
            builder.append(Cst.ACTION_SEPARATOR_UPDATE);

            // Tiled
            Tile[][] tiles = gameLoop.entityManager.getTiles();
            if(tiles != null){
                for(int i = 0; i < tiles.length; i++){
                    for(int j = 0; j < tiles[i].length; j++){
                        if(tiles[i][j] != null){
                            builder.append(tiles[i][j].encode());
                            builder.append(Cst.ACTION_SEPARATOR_UPDATE);
                        }
                    }
                }
            }

            // Enemy
            for(Enemy e : gameLoop.entityManager.getEnemies()){
                builder.append(e.encode());
                builder.append(Cst.ACTION_SEPARATOR_UPDATE);
            }

            // Turret player
            if(gameLoop.entityManager.getTurretPlayer() != null) {
                builder.append(gameLoop.entityManager.getTurretPlayer().encode());
                builder.append(Cst.ACTION_SEPARATOR_UPDATE);
            }

            // Enemy player
            for(EnemyPlayer e : gameLoop.entityManager.getEnemiesPlayer()){
                builder.append(e.encode());
                builder.append(Cst.ACTION_SEPARATOR_UPDATE);
            }

            // Bullet
            for(Bullet b : gameLoop.entityManager.getBullets()){
                builder.append(b.encode());
                builder.append(Cst.ACTION_SEPARATOR_UPDATE);
            }

            String memory = builder.toString();
            if(memory.endsWith(Cst.ACTION_SEPARATOR_UPDATE))
                memory = memory.substring(0, memory.length() - Cst.ACTION_SEPARATOR_UPDATE.length());

            add(MEMORY, memory);
        }else{
            String memory = get(MEMORY);

            String[] split = memory.split(Cst.ACTION_SEPARATOR_UPDATE);

            ArrayList<Entity> newEntities = new ArrayList<Entity>();

            for(String current : split){
                Entity copy = Entity.decode(current, gameLoop);

                if(copy == null)
                    break;

                newEntities.add(copy);
            }
            gameLoop.addNewUpdateEntities(newEntities);
            gameLoop.setUpdatedByServer(true);
        }
    }
}

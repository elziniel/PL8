package com.towerdefense.game.system.action;


import com.badlogic.gdx.math.Vector2;
import com.towerdefense.game.entity.Enemy;
import com.towerdefense.game.entity.EnemyPlayer;
import com.towerdefense.game.entity.EntryTile;
import com.towerdefense.game.system.GameLoop;
import com.towerdefense.game.util.Log;

public class SpawnEnemyAction extends Action {

    public static final String PARSE_VALUE = "SpawnEnemy";

    public static final String ID_PLAYER = "ip";

    public SpawnEnemyAction(){
        super();
    }

    public SpawnEnemyAction(int idPlayer){
        super(PARSE_VALUE, Send.CLIENT);

        add(ID_PLAYER, idPlayer+"");
    }

    @Override
    public void execute(GameLoop gameLoop) {
        if(gameLoop.isServerSide()){
            int idPlayer = Integer.parseInt(get(ID_PLAYER));

            EntryTile tile = gameLoop.entityManager.getEntryTile();
            Vector2 center = new Vector2();
            tile.getRound().getCenter(center);

            EnemyPlayer enemyPlayer = (EnemyPlayer) gameLoop.entityManager.getEntityByID(idPlayer);
            if(enemyPlayer == null){
                Log.e(PARSE_VALUE, "Impossible de trouver le joueur ennemie "+idPlayer);
                return;
            }

            Enemy e = (Enemy) gameLoop.entityManager.getEntityByID(enemyPlayer.enemyID);
            if(e == null){
                Log.e(PARSE_VALUE, "Enemy lié au joueur "+enemyPlayer.getName()+" introuvable");
                return;
            }

            e.position.x = center.x;
            e.position.y = center.y;
            e.setAlive(true);

            Log.d(PARSE_VALUE, enemyPlayer.getName()+": spawn enemy at ("+e.position.x+","+e.position.y+")");
        }
    }
}

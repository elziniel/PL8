package com.towerdefense.game.system.action;

import com.towerdefense.game.entity.Enemy;
import com.towerdefense.game.entity.EnemyPlayer;
import com.towerdefense.game.system.GameLoop;
import com.towerdefense.game.util.Cst;
import com.towerdefense.game.util.Log;
import com.towerdefense.game.util.Velocity;

public class UpdateEnemyLifeAction extends Action {

    public static final String PARSE_VALUE = "UpdateEnemyLife";

    public static final String ENEMY_LIFE = "el";
    public static final String ID_PLAYER = "ip";

    public UpdateEnemyLifeAction(){
        super();
    }

    public UpdateEnemyLifeAction(int life, int idPlayer) {
        super(PARSE_VALUE, Send.CLIENT);

        add(ENEMY_LIFE, life+"");

        add(ID_PLAYER, idPlayer+"");
    }

    @Override
    public void execute(GameLoop gameLoop) {
        int idPlayer = Integer.parseInt(get(ID_PLAYER));
        EnemyPlayer enemyPlayer = (EnemyPlayer) gameLoop.entityManager.getEntityByID(idPlayer);
        int enemyLife = Integer.parseInt(get(ENEMY_LIFE));
        if (enemyPlayer.getMoney() >= Cst.ENEMY_LIFE_COST) {
            enemyPlayer.setMoney(enemyPlayer.getMoney() - Cst.ENEMY_LIFE_COST);
            Log.e(PARSE_VALUE, "Augmentation vie enemy : " + enemyLife);
            Enemy enemy = (Enemy) gameLoop.entityManager.getEntityByID(enemyPlayer.enemyID);
            enemy.addLife(enemyLife);
        }
    }
}

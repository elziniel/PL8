package com.towerdefense.game.system.action;

import com.towerdefense.game.entity.Enemy;
import com.towerdefense.game.entity.EnemyPlayer;
import com.towerdefense.game.system.GameLoop;
import com.towerdefense.game.util.Cst;
import com.towerdefense.game.util.Log;
import com.towerdefense.game.util.Velocity;

public class UpdateEnemySpeedAction extends Action {

    public static final String PARSE_VALUE = "UpdateEnemySpeed";

    public static final String ENEMY_SPEED = "es";
    public static final String ID_PLAYER = "ip";

    public UpdateEnemySpeedAction(){
        super();
    }

    public UpdateEnemySpeedAction (float speed, int idPlayer) {
        super(PARSE_VALUE, Send.CLIENT);

        add(ENEMY_SPEED, speed+"");

        add(ID_PLAYER, idPlayer+"");
    }

    @Override
    public void execute(GameLoop gameLoop) {
        int idPlayer = Integer.parseInt(get(ID_PLAYER));
        EnemyPlayer enemyPlayer = (EnemyPlayer) gameLoop.entityManager.getEntityByID(idPlayer);
        float enemySpeed = Float.parseFloat(get(ENEMY_SPEED));
        if (enemyPlayer.getMoney() >= Cst.ENEMY_SPEED_COST) {
            enemyPlayer.setMoney(enemyPlayer.getMoney() - Cst.ENEMY_SPEED_COST);
            Log.e(PARSE_VALUE, "Changement vitesse enemy : " + enemySpeed);
            Enemy enemy = (Enemy) gameLoop.entityManager.getEntityByID(enemyPlayer.enemyID);
            enemy.addSpeed(enemySpeed);
        }
    }
}

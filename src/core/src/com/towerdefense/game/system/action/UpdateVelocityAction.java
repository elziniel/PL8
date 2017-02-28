package com.towerdefense.game.system.action;


import com.towerdefense.game.entity.Enemy;
import com.towerdefense.game.entity.EnemyPlayer;
import com.towerdefense.game.system.GameLoop;
import com.towerdefense.game.util.Log;
import com.towerdefense.game.util.Velocity;

public class UpdateVelocityAction extends Action {

    public static final String PARSE_VALUE = "UpdateVelocity";

    public static final String MOVE_UP = "mu";
    public static final String MOVE_DOWN = "md";
    public static final String MOVE_RIGHT = "mr";
    public static final String MOVE_LEFT = "ml";
    public static final String ID_PLAYER = "ip";

    public UpdateVelocityAction(){
        super();
    }

    public UpdateVelocityAction(boolean moveUp, boolean moveDown, boolean moveLeft, boolean moveRight, int idPlayer){
        super(PARSE_VALUE, Send.CLIENT);

        add(MOVE_UP, moveUp+"");
        add(MOVE_DOWN, moveDown+"");
        add(MOVE_RIGHT, moveRight+"");
        add(MOVE_LEFT, moveLeft+"");

        add(ID_PLAYER, idPlayer+"");
    }

    @Override
    public void execute(GameLoop gameLoop) {
        int idPlayer = Integer.parseInt(get(ID_PLAYER));

        boolean moveUp = Boolean.parseBoolean(get(MOVE_UP));
        boolean moveDown = Boolean.parseBoolean(get(MOVE_DOWN));
        boolean moveRight = Boolean.parseBoolean(get(MOVE_RIGHT));
        boolean moveLeft = Boolean.parseBoolean(get(MOVE_LEFT));

        EnemyPlayer enemyPlayer = (EnemyPlayer) gameLoop.entityManager.getEntityByID(idPlayer);
        if(enemyPlayer == null){
            Log.e(PARSE_VALUE, "Impossible de trouver le joueur ennemi "+idPlayer);
            return;
        }

        Enemy enemy = (Enemy) gameLoop.entityManager.getEntityByID(enemyPlayer.enemyID);
        if(enemy == null){
            Log.e(PARSE_VALUE, "Enemy lié au joueur "+enemyPlayer.getName()+" introuvable");
            return;
        }

        Velocity v = enemy.velocity;
        Log.l(PARSE_VALUE, enemyPlayer.getName()+" up:"+moveUp+", down:"+moveDown+", left:"+moveLeft+", right:"+moveRight);

        v.y = 0f;
        if(moveUp)
            v.addY(v.maxVelocity);
        if(moveDown)
            v.addY(-v.maxVelocity);

        v.x = 0f;
        if(moveRight)
            v.addX(v.maxVelocity);
        if(moveLeft)
            v.addX(-v.maxVelocity);

    }
}

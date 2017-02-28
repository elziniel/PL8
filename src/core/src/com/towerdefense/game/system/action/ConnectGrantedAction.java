package com.towerdefense.game.system.action;


import com.towerdefense.game.entity.Enemy;
import com.towerdefense.game.entity.EnemyPlayer;
import com.towerdefense.game.entity.TurretPlayer;
import com.towerdefense.game.system.ClientMessageManager;
import com.towerdefense.game.system.GameLoop;
import com.towerdefense.game.util.Log;

public class ConnectGrantedAction extends Action {

    public static final String PARSE_VALUE = "ConnectGranted";

    public static final String LOG = "ConnectGrantedAction";

    public static final String IS_TURRET = "it";
    public static final String ID_PLAYER = "ip";
    public static final String ID_ENEMY = "ie";
    public static final String IS_LOBBY = "il";

    public ConnectGrantedAction(){
        super();
    }

    public ConnectGrantedAction(String pseudo, boolean isTurretPlayer){
        super(PARSE_VALUE, pseudo);

        add(IS_TURRET, isTurretPlayer+"");
    }

    @Override
    public void execute(GameLoop gameLoop) {
        boolean isTurretPlayer = Boolean.parseBoolean(get(IS_TURRET));

        if(gameLoop.isServerSide()){
            if(isTurretPlayer) {
                TurretPlayer player = new TurretPlayer(pseudo);
                gameLoop.entityManager.add(player);
                add(ID_PLAYER, player.getID() + "");
                add(ID_ENEMY, 0+"");
            }else {
                Enemy enemy = new Enemy(0,0, pseudo);
                gameLoop.entityManager.add(enemy);

                EnemyPlayer player = new EnemyPlayer(pseudo);
                player.enemyID = enemy.getID();
                gameLoop.entityManager.add(player);

                add(ID_PLAYER, player.getID() + "");
                add(ID_ENEMY, enemy.getID() + "");
            }
            add(IS_LOBBY, gameLoop.isInLobby()+"");
        }else{
            int idPlayer = Integer.parseInt(get(ID_PLAYER));
            int idEnemy = Integer.parseInt(get(ID_ENEMY));
            boolean islobby = Boolean.parseBoolean(get(IS_LOBBY));

            gameLoop.setInLobby(islobby);

            ((ClientMessageManager) gameLoop.messageManager).connectionGranted(idPlayer, idEnemy);
        }
        Log.l(LOG, "Connection granted");
    }
}

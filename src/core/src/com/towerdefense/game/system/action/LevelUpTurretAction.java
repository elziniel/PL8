package com.towerdefense.game.system.action;


import com.towerdefense.game.entity.Tile;
import com.towerdefense.game.entity.TurretPlayer;
import com.towerdefense.game.entity.TurretTile;
import com.towerdefense.game.system.GameLoop;
import com.towerdefense.game.util.Cst;

public class LevelUpTurretAction extends Action{

    public static final String PARSE_VALUE = "LevelUpTurret";

    public static final String POS_X = "x";
    public static final String POS_Y = "y";

    public LevelUpTurretAction(){
        super();
    }

    public LevelUpTurretAction(int x, int y){
        super(PARSE_VALUE, Send.CLIENT);

        add(POS_X, x +"");
        add(POS_Y, y +"");
    }

    @Override
    public void execute(GameLoop gameLoop) {
        int x = Integer.parseInt(get(POS_X));
        int y = Integer.parseInt(get(POS_Y));

        Tile t = gameLoop.entityManager.getTiles()[x][y];
        if(!(t instanceof TurretTile))
            return;

        TurretTile turret = (TurretTile) t;
        TurretPlayer player = gameLoop.entityManager.getTurretPlayer();

        if(player.getMoney() < Cst.TURRET_LEVEL_UP_COST)
            return;

        player.setMoney(player.getMoney() - Cst.TURRET_LEVEL_UP_COST);
        turret.level++;
    }
}

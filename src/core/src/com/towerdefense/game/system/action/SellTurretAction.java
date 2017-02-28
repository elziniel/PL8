package com.towerdefense.game.system.action;


import com.towerdefense.game.entity.*;
import com.towerdefense.game.system.GameLoop;
import com.towerdefense.game.util.Cst;
import com.towerdefense.game.util.Log;

public class SellTurretAction extends Action{

    public static final String PARSE_VALUE = "sellTurret";

    public static final String POS_X = "x";
    public static final String POS_Y = "y";

    public SellTurretAction(){
        super();
    }

    public SellTurretAction(int x, int y){
        super(PARSE_VALUE, Send.CLIENT);

        add(POS_X, x + "");
        add(POS_Y, y + "");
    }

    @Override
    public void execute(GameLoop gameLoop) {
        int x = Integer.parseInt(get(POS_X));
        int y = Integer.parseInt(get(POS_Y));

        Tile tile = gameLoop.entityManager.getTiles()[x][y];
        if(!(tile instanceof TurretTile))
            return;

        TurretTile turret = (TurretTile) tile;
        int prix = (int)(turret.level * Cst.TURRET_COST * Cst.TURRET_SELL_PERCENT);

        gameLoop.entityManager.add(new FloorTile(x, y));

        TurretPlayer p = gameLoop.entityManager.getTurretPlayer();
        p.setMoney(p.getMoney() + prix);

        Log.l(PARSE_VALUE, "Vente de la tourelle ("+x+", "+y+") pour "+prix);
    }
}

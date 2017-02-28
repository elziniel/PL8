package com.towerdefense.game.system.action;


import com.towerdefense.game.entity.ExitTile;
import com.towerdefense.game.entity.FloorTile;
import com.towerdefense.game.entity.Tile;
import com.towerdefense.game.entity.TurretTile;
import com.towerdefense.game.entity.WallTile;
import com.towerdefense.game.system.GameLoop;
import com.towerdefense.game.util.Log;
import com.towerdefense.game.util.Cst;

public class AddTurretAction extends Action {

    public static final String PARSE_VALUE = "addTurret";

    public static final String POS_X = "x";
    public static final String POS_Y = "y";

    private int[][] clear;

    public AddTurretAction(){
        super();
    }

    public AddTurretAction(int x, int y) {
        super(PARSE_VALUE, Send.CLIENT);

        add(POS_X, x + "");
        add(POS_Y, y + "");
    }

    @Override
    public void execute(GameLoop gameLoop) {
        int x = Integer.parseInt(get(POS_X));
        int y = Integer.parseInt(get(POS_Y));

        if(requestAddTurret(gameLoop, x, y)){
            if (gameLoop.entityManager.getTurretPlayer().getType() == Cst.WALL_TYPE &&
                    gameLoop.entityManager.getTurretPlayer().getMoney() >= Cst.WALL_COST) {
                gameLoop.entityManager.getTurretPlayer().setMoney(gameLoop.entityManager.getTurretPlayer().getMoney()- Cst.WALL_COST);
                WallTile t = new WallTile(x * Cst.TILE_WIDTH, y * Cst.TILE_HEIGHT);
                gameLoop.entityManager.add(t);
                Log.l(PARSE_VALUE, "Ajout wall :" + x + ", " + y);
            }
            else if (gameLoop.entityManager.getTurretPlayer().getMoney() >= Cst.TURRET_COST) {
                gameLoop.entityManager.getTurretPlayer().setMoney(gameLoop.entityManager.getTurretPlayer().getMoney()- Cst.TURRET_COST);
                TurretTile t = new TurretTile(x * Cst.TILE_WIDTH, y * Cst.TILE_HEIGHT, gameLoop.entityManager.getTurretPlayer().getType());
                gameLoop.entityManager.add(t);
                Log.l(PARSE_VALUE, "Ajout turret :" + x + ", " + y + ", " + gameLoop.entityManager.getTurretPlayer().getType());
            }
        }else{
            Log.l(PARSE_VALUE, "Ajout turret ("+x+","+y+") impossible");
        }
    }

    /**
     * Vérifie que l'ajout de la tourelle aux coordonnées x,y est possible
     * @param gameLoop la boucle de jeu
     * @param x coordonnées X
     * @param y coordonnées Y
     * @return retourne true si l'ajout est possible
     */
    public boolean requestAddTurret(GameLoop gameLoop, int x, int y){
        Tile focus = gameLoop.entityManager.getTiles()[x][y];
        clear = clearedPath(gameLoop.entityManager.getTiles(), x, y);
        return focus != null && focus instanceof FloorTile && pathFinding(0, 0);
    }

    public int[][] clearedPath(Tile[][] tiles, int x, int y) {
        int[][] clear = new int[tiles.length][tiles[0].length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j] instanceof TurretTile || tiles[i][j] instanceof WallTile || (i == x && j == y)) {
                    clear[i][j] = 1;
                }
                else if (tiles[i][j] instanceof ExitTile) {
                    clear[i][j] = 2;
                }
                else {
                    clear[i][j] = 0;
                }
            }
        }
        return clear;
    }

    public boolean pathFinding(int x, int y) {
        if (clear[x][y] == 2) {
            return true;
        }
        if (clear[x][y] == 1) {
            return false;
        }
        clear[x][y] = 3;
        if (x+1 < clear.length && clear[x+1][y] != 3 && pathFinding(x+1, y)) {
            return true;
        }
        if (y+1 < clear[x].length && clear[x][y+1] != 3 && pathFinding(x, y+1)) {
            return true;
        }
        if (x-1 >= 0 && clear[x-1][y] != 3 && pathFinding(x-1, y)) {
            return true;
        }
        if (y-1 >= 0 && clear[x][y-1] != 3 && pathFinding(x, y-1)) {
            return true;
        }
        clear[x][y] = 1;
        return false;
    }
}

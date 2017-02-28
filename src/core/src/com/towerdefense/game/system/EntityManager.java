package com.towerdefense.game.system;


import com.badlogic.gdx.math.Vector2;
import com.towerdefense.game.entity.Bullet;
import com.towerdefense.game.entity.Enemy;
import com.towerdefense.game.entity.EnemyPlayer;
import com.towerdefense.game.entity.Entity;
import com.towerdefense.game.entity.EntryTile;
import com.towerdefense.game.entity.ExitTile;
import com.towerdefense.game.entity.Map;
import com.towerdefense.game.entity.Tile;
import com.towerdefense.game.entity.TurretPlayer;
import com.towerdefense.game.entity.TurretTile;
import com.towerdefense.game.system.action.ShootBulletAction;
import com.towerdefense.game.util.Log;
import com.towerdefense.game.util.Cst;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Manager des entités
 */
public class EntityManager {

    private static final String LOG = "EntityManager";

    private GameLoop gameLoop;

    private ArrayList<Enemy> enemies;

    private Map map;

    private TurretPlayer turretPlayer;

    private ArrayList<EnemyPlayer> enemiesPlayer;

    private Tile[][] tiles;

    private EntryTile entryTile;

    private ExitTile exitTile;

    private ArrayList<Bullet> bullets;

    /**
     * Constructeur
     */
    public EntityManager(GameLoop gameLoop){
        this.gameLoop = gameLoop;
        enemies = new ArrayList<Enemy>();
        enemiesPlayer = new ArrayList<EnemyPlayer>();
        bullets = new ArrayList<Bullet>();
    }

    /**
     * Ajoute une entité
     * @param e
     */
    public void add(Entity e){
        if(gameLoop.isServerSide())
            e.setID(Entity.getNewID());

        if(e instanceof Map) {
            map = (Map) e;
            tiles = new Tile[map.getNbTileWidth()][map.getNbTileHeight()];
        }else if(e instanceof Enemy)
            enemies.add((Enemy) e);
        else if(e instanceof EnemyPlayer)
            enemiesPlayer.add((EnemyPlayer) e);
        else if(e instanceof TurretPlayer)
            turretPlayer = (TurretPlayer) e;
        else if(e instanceof Tile){
            Tile t = (Tile) e;
            if(e instanceof ExitTile)
                exitTile = (ExitTile) t;
            else if(e instanceof EntryTile)
                entryTile = (EntryTile) t;
            tiles[(int) (t.position.x/ Cst.TILE_WIDTH)][(int) (t.position.y/ Cst.TILE_HEIGHT)] = t;
        }else if(e instanceof Bullet){
            Bullet b = (Bullet) e;
            bullets.add(b);
        }
    }

    public EnemyPlayer getEnemyPlayerByName(String name){
        for(EnemyPlayer current : enemiesPlayer){
            if(current.getName().equals(name)) {
                return current;
            }
        }
        return null;
    }

    public Entity getEntityByID(int ID){
        if(map != null && map.getID() == ID)
            return map;

        if(tiles != null){
            for(int i = 0; i < tiles.length; i++){
                for(int j = 0; j < tiles[i].length; j++){
                    if(tiles[i][j] != null && tiles[i][j].getID() == ID)
                        return tiles[i][j];
                }
            }
        }

        if(turretPlayer!= null && turretPlayer.getID() == ID)
            return turretPlayer;

        for(Enemy e : enemies){
            if(e.getID() == ID)
                return e;
        }

        for(EnemyPlayer e : enemiesPlayer){
            if(e.getID() == ID)
                return e;
        }

        for(Bullet b : bullets){
            if(b.getID() == ID)
                return b;
        }

        return null;
    }

    public void resetUpdated() {
        if (map != null && map.isUpdated())
            map.setUpdated(false);
        else if(map != null)
            map = null;

        if(tiles != null){
            for(int i = 0; i < tiles.length; i++){
                for(int j = 0; j < tiles[i].length; j++) {
                    if(tiles[i][j] != null && tiles[i][j].isUpdated())
                        tiles[i][j].setUpdated(false);
                    else if(tiles[i][j] != null)
                        tiles[i][j] = null;
                }
            }
        }

        Iterator<EnemyPlayer> itEnemyPlayer = enemiesPlayer.iterator();
        while(itEnemyPlayer.hasNext()){
            EnemyPlayer e = itEnemyPlayer.next();
            if(e.isUpdated()){
                e.setUpdated(false);
            }else{
                itEnemyPlayer.remove();
            }
        }

        Iterator<Enemy> itEnemy = enemies.iterator();
        while(itEnemy.hasNext()){
            Enemy e = itEnemy.next();
            if(e.isUpdated()){
                e.setUpdated(false);
            }else{
                itEnemy.remove();
            }
        }

        Iterator<Bullet> itBullets = getBullets().iterator();
        while(itBullets.hasNext()){
            Bullet b = itBullets.next();
            if(b.isUpdated()){
                b.setUpdated(false);
            }else{
                itBullets.remove();
                Bullet.bulletPool.free(b);
            }
        }
    }

    public void act(float deltaTime){
        if (gameLoop.gameScreen != null) {
            if (gameLoop.player instanceof TurretPlayer && turretPlayer != null) {
                gameLoop.player = turretPlayer;
            }
            else if (gameLoop.player instanceof EnemyPlayer) {
                for (EnemyPlayer e : enemiesPlayer) {
                    if (e.getName().equals(gameLoop.player.getName())) {
                        gameLoop.player = e;
                        break;
                    }
                }
            }
        }
        if(tiles != null){
            for(int i = 0; i < tiles.length; i++){
                for(int j = 0; j < tiles[i].length; j++) {
                    if(tiles[i][j] != null && tiles[i][j] instanceof TurretTile){
                        TurretTile t = (TurretTile) tiles[i][j];
                        t.lastShoot += deltaTime;
                        if(t.lastShoot > Cst.TURRET_SHOOT_RATE){
                            Vector2 center = new Vector2();
                            t.getRound().getCenter(center);
                            Enemy target = null;
                            float distanceTarget = 0f;
                            for(Enemy e : enemies){
                                if(!e.isAlive())
                                    continue;
                                float distance = center.dst(e.position.x, e.position.y);
                                if(distance < Cst.TURRET_RANGE){
                                    if(target != null && distance < distanceTarget || target == null){
                                        distanceTarget = distance;
                                        target = e;
                                    }
                                }
                            }

                            if(target != null) {
                                int damage = Cst.TURRET_DAMAGE;
                                if(t.level > 1)
                                    damage = (int) (damage * Cst.TURRET_DAMAGE_MULTIPLIER_PER_LEVEL * (t.level -1));
                                gameLoop.actionManager.addAction(new ShootBulletAction(center.x, center.y, target.getID(), t.getColorType(), damage));
                                t.lastShoot = 0f;
                            }
                        }
                    }
                }
            }
        }
    }

    public void printEntities(){
        if(map != null)
            Log.l(LOG, map.encode());

        if(tiles != null){
            for(int i = 0; i < tiles.length; i++){
                for(int j = 0; j < tiles[i].length; j++){
                    if(tiles[i][j] != null)
                        Log.l(LOG, tiles[i][j].encode());
                }
            }
        }

        if(turretPlayer!= null)
            Log.l(LOG, turretPlayer.encode());

        for(Enemy e : enemies){
            Log.l(LOG, e.encode());
        }

        for(EnemyPlayer e : enemiesPlayer){
            Log.l(LOG, e.encode());
        }

        for(Bullet b : bullets){
            Log.l(LOG, b.encode());
        }
    }

    public Tile getTileAtCoordonate(float x, float y){
        int a = (int) (x / Cst.TILE_WIDTH);
        int b = (int) (y / Cst.TILE_HEIGHT);

        if(a < 0 || a >= tiles.length)
            return null;

        if(b < 0 || b >= tiles[0].length)
            return null;

        return tiles[a][b];
    }

    public ArrayList<Enemy> getEnemies(){
        return enemies;
    }

    public Map getMap(){
        return map;
    }

    public TurretPlayer getTurretPlayer(){
        return turretPlayer;
    }

    public ArrayList<EnemyPlayer> getEnemiesPlayer(){
        return enemiesPlayer;
    }

    public Tile[][] getTiles(){
        return tiles;
    }

    public EntryTile getEntryTile(){
        return entryTile;
    }

    public ExitTile getExitTile(){
        return exitTile;
    }

    synchronized public ArrayList<Bullet> getBullets(){
        return bullets;
    }
}

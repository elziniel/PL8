package com.towerdefense.game.system;


import com.badlogic.gdx.math.Rectangle;
import com.towerdefense.game.entity.Bullet;
import com.towerdefense.game.entity.Enemy;
import com.towerdefense.game.entity.Entity;
import com.towerdefense.game.entity.ExitTile;
import com.towerdefense.game.entity.Tile;
import com.towerdefense.game.entity.TurretTile;
import com.towerdefense.game.entity.WallTile;
import com.towerdefense.game.util.Position;
import com.towerdefense.game.util.Cst;
import com.towerdefense.game.util.Velocity;

import java.util.ArrayList;
import java.util.Iterator;


public class MovementManager {

    public static final String LOG = "MovementManager";

    private GameLoop gameLoop;

    public MovementManager(GameLoop gameLoop){
        this.gameLoop = gameLoop;
    }

    public void updateAll(float deltaTime){

        // Update des enemies
        for(Enemy e : gameLoop.entityManager.getEnemies()) {
            Velocity v = e.velocity;
            Position p = e.position;

            Position old = new Position(p.x, p.y);

            p.x = p.x + v.x * deltaTime;
            enemyCollision(e, old, p);
            old.set(p.x, p.y);

            p.y = p.y + v.y * deltaTime;
            enemyCollision(e, old, p);
        }

        // Update des bullets
        Iterator<Bullet> bullets = gameLoop.entityManager.getBullets().iterator();
        while(bullets.hasNext()){
            Bullet b = bullets.next();
            Entity t =  gameLoop.entityManager.getEntityByID(b.targetID);

            if(t != null && t instanceof Enemy){
                Enemy target = (Enemy) t;

                float deltaX = target.position.x - b.position.x;
                float deltaY = target.position.y - b.position.y;
                double direction = Math.atan2(deltaY, deltaX);

                b.position.x = b.position.x + b.velocity.x * deltaTime * (float)Math.cos(direction);
                b.position.y = b.position.y + b.velocity.y * deltaTime * (float)Math.sin(direction);

                if(b.getRound().overlaps(target.getRound())){
                    int damage = b.damage;
                    if(b.colorType == ((Enemy) t).getColor())
                        damage *= Cst.TURRET_SAME_COLOR_DAMAGE_MULTIPLIER;
                    target.addLife(-1 * damage);
                    if (target.getLife() == 0) {
                        target.setAlive(false);
                        gameLoop.entityManager.getTurretPlayer().setMoney(gameLoop.entityManager.getTurretPlayer().getMoney()+ Cst.ENEMY_COST);
                    }
                    bullets.remove();
                    Bullet.bulletPool.free(b);
                }
            }else if(t == null){
                bullets.remove();
                Bullet.bulletPool.free(b);
            }
        }
    }

    private void enemyCollision(Enemy e, Position old, Position now){
        ArrayList<Tile> tilesCollide = new ArrayList<Tile>();
        Tile[][] tiles = gameLoop.entityManager.getTiles();

        float xDown = now.x - Cst.ENEMY_WIDTH/2f;
        float xUp = xDown + Cst.ENEMY_WIDTH;

        float yDown = now.y - Cst.ENEMY_HEIGHT/2f;
        float yUp = yDown + Cst.ENEMY_HEIGHT;

        if(old.x != now.x){
            if(now.x > old.x){
                if(xUp > gameLoop.entityManager.getMap().getWidth()){
                    now.x = old.x;
                    return;
                }

                tilesCollide.add(gameLoop.entityManager.getTileAtCoordonate(xUp, yDown));

                if((int)(yDown / Cst.TILE_HEIGHT) != (int)(yUp / Cst.TILE_HEIGHT)){
                    tilesCollide.add(gameLoop.entityManager.getTileAtCoordonate(xUp, yUp));
                }
            }else{
                if(xDown < 0){
                    now.x = old.x;
                    return;
                }

                tilesCollide.add(gameLoop.entityManager.getTileAtCoordonate(xDown, yDown));

                if((int)(yDown / Cst.TILE_HEIGHT) != (int)(yUp / Cst.TILE_HEIGHT)){
                    tilesCollide.add(gameLoop.entityManager.getTileAtCoordonate(xDown, yUp));
                }
            }
        }

        if(old.y != now.y){
            if(now.y > old.y){
                if(yUp > gameLoop.entityManager.getMap().getHeight()){
                    now.y = old.y;
                    return;
                }

                tilesCollide.add(gameLoop.entityManager.getTileAtCoordonate(xDown, yUp));

                if((int)(xDown / Cst.TILE_WIDTH) != (int)(xUp / Cst.TILE_WIDTH)){
                    tilesCollide.add(gameLoop.entityManager.getTileAtCoordonate(xUp, yUp));
                }
            }else{
                if(yDown < 0){
                    now.y = old.y;
                    return;
                }

                tilesCollide.add(gameLoop.entityManager.getTileAtCoordonate(xDown, yDown));

                if((int)(xDown / Cst.TILE_WIDTH) != (int)(xUp / Cst.TILE_WIDTH)){
                    tilesCollide.add(gameLoop.entityManager.getTileAtCoordonate(xUp, yDown));
                }
            }
        }

        for(Tile t : tilesCollide){
            if(t instanceof TurretTile || t instanceof WallTile){
                now.x = old.x;
                now.y = old.y;
                return;
            }

            if(t instanceof ExitTile){
                if(t.getRound().contains(new Rectangle(xDown, yDown, Cst.ENEMY_WIDTH, Cst.ENEMY_HEIGHT)) && e.isAlive()) {
                    if (gameLoop.entityManager.getTurretPlayer().getLife() > 0) {
                        gameLoop.entityManager.getTurretPlayer().addLife(-1);
                    }
                    e.setAlive(false);
                }
            }
        }
    }
}

package com.towerdefense.game.system;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.towerdefense.game.entity.Bullet;
import com.towerdefense.game.entity.Enemy;
import com.towerdefense.game.entity.Tile;


/**
 * Manager de l'affichage graphique des entités sur le SpriteBatch
 */
public class RenderManager {

    private GameLoop gameLoop;

    public RenderManager(GameLoop gameLoop){
        this.gameLoop = gameLoop;
    }

    /**
     * Affiche toutes les entitées qui ont un Drawable
     * @param batch zone de dessin
     */
    public void renderAll(Batch batch){
        // Draw map
        if(gameLoop.entityManager.getMap() != null){
            Tile[][] tiles = gameLoop.entityManager.getTiles();
            for(int i = 0; i < tiles.length; i++){
                for(int j = 0; j < tiles[i].length; j++) {
                    if(tiles[i][j] != null)
                        tiles[i][j].draw(batch);
                }
            }
        }


        // Draw enemies
        for(Enemy e : gameLoop.entityManager.getEnemies())
            e.draw(batch);

        // Draw bullets
        for(Bullet b : gameLoop.entityManager.getBullets())
            b.draw(batch);
    }
}

package com.towerdefense.game.entity;


import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.towerdefense.game.util.Assets;
import com.towerdefense.game.util.Cst;

/**
 * Case contenant rien. Zone de passage des ennemis
 */
public class FloorTile extends Tile{

    public static final String PARSE_TAG = "ft";

    public FloorTile(float x, float y){
        super(x, y);
    }

    @Override
    protected TextureAtlas.AtlasRegion getTexture(){
        return ((TextureAtlas) Assets.manager.get(Assets.TEXTURES)).findRegion(Assets.FLOOR);
    }

    @Override
    public String encode() {
        return ID+
                Cst.ENTITY_SEPARATOR+
                PARSE_TAG+
                Cst.ENTITY_SEPARATOR+
                position.x+
                Cst.ENTITY_SEPARATOR+
                position.y;
    }

    @Override
    public void update(Entity e) {
        FloorTile t = (FloorTile) e;

        position.x = t.position.x;
        position.y = t.position.y;
    }

    public static FloorTile decode(String encoded){
        String[] split = encoded.split(Cst.ENTITY_SEPARATOR);

        if(split.length != 4)
            return null;

        FloorTile t = new FloorTile(Float.parseFloat(split[2]), Float.parseFloat(split[3]));
        t.setID(Integer.parseInt(split[0]));

        return t;
    }
}

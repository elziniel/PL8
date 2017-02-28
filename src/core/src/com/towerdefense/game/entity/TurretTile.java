package com.towerdefense.game.entity;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.towerdefense.game.util.Assets;
import com.towerdefense.game.util.Cst;

/**
 * Case contenant une tourelle
 */
public class TurretTile extends Tile{

    public static final String PARSE_TAG = "tt";

    public float lastShoot = 0f;

    public int level = 1;
    
    private int colorType;

    public TurretTile(float x, float y, int colorType){
        super(x, y);
        this.colorType = colorType;
    }

    @Override
    protected TextureAtlas.AtlasRegion getTexture(){
        switch (colorType) {
            case Cst.BLUE_TYPE:
                return ((TextureAtlas) Assets.manager.get(Assets.TEXTURES)).findRegion(Assets.TURRET_BLUE);
            case Cst.RED_TYPE:
                return ((TextureAtlas) Assets.manager.get(Assets.TEXTURES)).findRegion(Assets.TURRET_RED);
            case Cst.YELLOW_TYPE:
                return ((TextureAtlas) Assets.manager.get(Assets.TEXTURES)).findRegion(Assets.TURRET_YELLOW);
        }
        return null;
    }

    @Override
    public String encode() {
        return ID+
                Cst.ENTITY_SEPARATOR+
                PARSE_TAG+
                Cst.ENTITY_SEPARATOR+
                position.x+
                Cst.ENTITY_SEPARATOR+
                position.y+
                Cst.ENTITY_SEPARATOR+
                colorType+
                Cst.ENTITY_SEPARATOR+
                level;
    }

    @Override
    public void update(Entity e) {
        TurretTile t = (TurretTile) e;

        colorType = t.colorType;
        position.x = t.position.x;
        position.y = t.position.y;
        level = t.level;
    }

    public static TurretTile decode(String encoded){
        String[] split = encoded.split(Cst.ENTITY_SEPARATOR);

        if(split.length != 6)
            return null;

        TurretTile t = new TurretTile(Float.parseFloat(split[2]), Float.parseFloat(split[3]), Integer.parseInt(split[4]));
        t.setID(Integer.parseInt(split[0]));
        t.level = Integer.parseInt(split[5]);

        return t;
    }

    public int getColorType(){
        return colorType;
    }
}

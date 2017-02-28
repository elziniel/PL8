package com.towerdefense.game.entity;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.towerdefense.game.util.Drawable;
import com.towerdefense.game.util.Position;
import com.towerdefense.game.util.Cst;

public abstract class Tile extends Entity implements Drawable{

    public Position position;

    public Tile(float x, float y){
        super();

        position = new Position(x, y);
    }

    @Override
    public void draw(Batch batch) {
        TextureAtlas.AtlasRegion t = getTexture();
        if(t != null && batch != null && position != null)
            batch.draw(t, position.x*t.getRegionWidth(), position.y*t.getRegionHeight());
    }

    protected abstract TextureAtlas.AtlasRegion getTexture();

    public Rectangle getRound(){
        return new Rectangle(position.x, position.y, Cst.TILE_WIDTH, Cst.TILE_HEIGHT);
    }
}

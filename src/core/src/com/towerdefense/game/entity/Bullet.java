package com.towerdefense.game.entity;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;
import com.towerdefense.game.util.Assets;
import com.towerdefense.game.util.Collideable;
import com.towerdefense.game.util.Drawable;
import com.towerdefense.game.util.Position;
import com.towerdefense.game.util.Cst;
import com.towerdefense.game.util.Velocity;

public class Bullet extends Entity implements Drawable, Pool.Poolable{

    public static final String PARSE_TAG = "b";

    public final static Pool<Bullet> bulletPool = new Pool<Bullet>() {
        @Override
        protected Bullet newObject() {
            return new Bullet();
        }
    };

    public Position position;

    public Velocity velocity;

    public int colorType;

    public int damage;

    public int targetID;

    public static final Collideable collideable = new Collideable(Collideable.BULLET,
            Collideable.ENEMY);

    public Bullet(){
        position = new Position();
        velocity = new Velocity(Cst.BULLET_MAX_VELOCITY);
    }

    public void init(float x, float y, int targetID, int colorType, int damage){
        position.set(x, y);
        this.targetID = targetID;
        this.colorType = colorType;
        this.damage = damage;
        velocity.set(Cst.BULLET_MAX_VELOCITY, Cst.BULLET_MAX_VELOCITY);
    }

    @Override
    public void reset() {
        position.set(0f, 0f);
        targetID = -1;
        damage = 0;
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
                velocity.x+
                Cst.ENTITY_SEPARATOR+
                velocity.y+
                Cst.ENTITY_SEPARATOR+
                targetID+
                Cst.ENTITY_SEPARATOR+
                colorType+
                Cst.ENTITY_SEPARATOR+
                damage;
    }

    @Override
    public void update(Entity e) {
        Bullet copy = (Bullet) e;

        position.set(copy.position.x, copy.position.y);
        velocity.set(copy.velocity.x, copy.velocity.y);

        colorType = copy.colorType;
        damage = copy.damage;
        targetID = copy.targetID;
    }

    public static Bullet decode(String encoded){
        if(encoded == null)
            return null;

        String[] split = encoded.split(Cst.ENTITY_SEPARATOR);

        if(split.length != 9)
            return null;

        Bullet b = bulletPool.obtain();
        b.init(Float.parseFloat(split[2]), Float.parseFloat(split[3]), Integer.parseInt(split[6]), Integer.parseInt(split[7]), Integer.parseInt(split[8]));
        b.velocity.set(Float.parseFloat(split[4]), Float.parseFloat(split[5]));
        b.setID(Integer.parseInt(split[0]));

        return b;
    }

    @Override
    public void draw(Batch batch) {
        TextureAtlas.AtlasRegion t = getTexture();
        if(position != null && t != null && batch != null)
            batch.draw(t, position.x * Cst.WORLD_TO_GRAPHIC - t.getRegionWidth()/2, position.y * Cst.WORLD_TO_GRAPHIC - t.getRegionHeight()/2);
    }

    public TextureAtlas.AtlasRegion getTexture(){
        String asset = null;
        switch (colorType){
            case Cst.BLUE_TYPE:
                asset = Assets.BULLET_BLUE;
                break;
            case Cst.RED_TYPE:
                asset = Assets.BULLET_RED;
                break;
            case Cst.YELLOW_TYPE:
                asset = Assets.BULLET_YELLOW;
                break;
        }
        if(asset == null)
            return null;
        return ((TextureAtlas) Assets.manager.get(Assets.TEXTURES)).findRegion(asset);
    }

    public Rectangle getRound(){
        return new Rectangle(position.x - Cst.BULLET_WIDTH/2,
                position.y - Cst.BULLET_HEIGHT/2,
                Cst.BULLET_WIDTH,
                Cst.BULLET_HEIGHT);
    }
}

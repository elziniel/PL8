package com.towerdefense.game.entity;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.towerdefense.game.util.Collideable;
import com.towerdefense.game.util.Drawable;
import com.towerdefense.game.util.Assets;
import com.towerdefense.game.util.Position;
import com.towerdefense.game.util.Cst;
import com.towerdefense.game.util.Velocity;

public class Enemy extends Entity implements Drawable{

    public static final String PARSE_TAG = "e";

    public Position position;

    public Velocity velocity;

    private boolean alive;

    private int life, color;

    private GlyphLayout glyphLayout;

    private String name;

    public static final Collideable collideable = new Collideable(Collideable.ENEMY,
            Collideable.TURRET);

    public Enemy(float x, float y, String name){
        position = new Position(x, y);
        velocity = new Velocity(Cst.ENEMY_MAX_VELOCITY);
        alive = false;
        color = Cst.BLUE_TYPE;
        this.name = name;
    }

    @Override
    public void draw(Batch batch) {
        if(glyphLayout == null){
            glyphLayout = new GlyphLayout(Assets.fontEnemy, name);
        }

        TextureAtlas.AtlasRegion t = getTexture();
        if(alive && position != null && t != null && batch != null) {
            batch.draw(t, position.x * Cst.WORLD_TO_GRAPHIC - t.getRegionWidth() / 2, position.y * Cst.WORLD_TO_GRAPHIC - t.getRegionHeight() / 2);

            float centerX = position.x * Cst.WORLD_TO_GRAPHIC - glyphLayout.width /2;
            float centerY = position.y * Cst.WORLD_TO_GRAPHIC + 2*t.getRegionHeight() - glyphLayout.height /2;

            Assets.fontEnemy.setColor(Color.WHITE);
            Assets.fontEnemy.draw(batch, glyphLayout, centerX, centerY);
        }
    }

    public TextureAtlas.AtlasRegion getTexture(){
        switch (color) {
            case Cst.BLUE_TYPE:
                return ((TextureAtlas) Assets.manager.get(Assets.TEXTURES)).findRegion(Assets.ENEMY_BLUE);
            case Cst.RED_TYPE:
                return ((TextureAtlas) Assets.manager.get(Assets.TEXTURES)).findRegion(Assets.ENEMY_RED);
            case Cst.YELLOW_TYPE:
                return ((TextureAtlas) Assets.manager.get(Assets.TEXTURES)).findRegion(Assets.ENEMY_YELLOW);
        }
        return null;
    }

    @Override
    public String encode() {
        return  ID+
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
                (alive? "1" : "0")+
                Cst.ENTITY_SEPARATOR+
                color+
                Cst.ENTITY_SEPARATOR+
                life+
                Cst.ENTITY_SEPARATOR+
                name;
    }

    @Override
    public void update(Entity e) {
        Enemy copy = (Enemy) e;

        position.x = copy.position.x;
        position.y = copy.position.y;

        velocity.x = copy.velocity.x;
        velocity.y = copy.velocity.y;

        color = copy.color;
        life = copy.life;
        name = copy.name;

        alive = copy.alive;
    }

    public static Enemy decode(String encoded){
        if(encoded == null)
            return null;

        String[] split = encoded.split(Cst.ENTITY_SEPARATOR);

        if(split.length != 10)
            return null;

        Enemy e = new Enemy(Float.parseFloat(split[2]), Float.parseFloat(split[3]), split[9]);
        e.velocity.x = Float.parseFloat(split[4]);
        e.velocity.y = Float.parseFloat(split[5]);
        e.alive = "1".equals(split[6]);
        e.color = Integer.parseInt(split[7]);
        e.life = Integer.parseInt(split[8]);
        e.setID(Integer.parseInt(split[0]));

        return e;
    }

    public void setAlive(boolean b){
        alive = b;
        if (b) {
            life = Cst.ENEMY_LIFE;
        }
    }

    public void addSpeed(float v) {
        velocity.maxVelocity += v;
    }

    public boolean isAlive() {
        return alive;
    }

    public void addLife(int add){
        life += add;
        if(life < 0){
            life = 0;
        }
    }

    public int getLife() {
        return life;
    }

    public int getColor() {
        return color;
    }

    public Rectangle getRound(){
        return new Rectangle(position.x - Cst.ENEMY_WIDTH/2,
                position.y - Cst.ENEMY_HEIGHT/2,
                Cst.ENEMY_WIDTH,
                Cst.ENEMY_HEIGHT);
    }
}

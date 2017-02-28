package com.towerdefense.game.entity;


import com.towerdefense.game.system.GameLoop;
import com.towerdefense.game.util.Cst;

/**
 * Défini une entité : composé de components qui définissent les actions que peut réaliser
 * cette entité
 */
public abstract class Entity{

    /**
     * Separateur pour les représentations en String des entités
     */

    private static int ID_COMPTEUR = 1;

    protected int ID = -1;

    protected boolean updated;

    /**
     * Constructeur
     */
    public Entity(){
    }

    public abstract String encode();

    public abstract void update(Entity e);

    public void setID(int ID){
        this.ID = ID;
    }

    public int getID(){
        return ID;
    }

    public void setUpdated(boolean b){
        updated = b;
    }

    public boolean isUpdated(){
        return updated;
    }

    public static Entity decode(String encoded, GameLoop gameLoop){
        if(encoded == null)
            return null;

        String[] split = encoded.split(Cst.ENTITY_SEPARATOR);
        if(split.length < 2)
            return null;

        String tag = split[1];
        if(tag.equals(Enemy.PARSE_TAG))
            return Enemy.decode(encoded);
        if(tag.equals(EnemyPlayer.PARSE_TAG))
            return EnemyPlayer.decode(encoded);
        if(tag.equals(EntryTile.PARSE_TAG))
            return EntryTile.decode(encoded);
        if(tag.equals(ExitTile.PARSE_TAG))
            return ExitTile.decode(encoded);
        if(tag.equals(FloorTile.PARSE_TAG))
            return FloorTile.decode(encoded);
        if(tag.equals(Map.PARSE_TAG))
            return Map.decode(encoded);
        if(tag.equals(TurretPlayer.PARSE_TAG))
            return TurretPlayer.decode(encoded);
        if(tag.equals(TurretTile.PARSE_TAG))
            return TurretTile.decode(encoded);
        if(tag.equals(Bullet.PARSE_TAG))
            return Bullet.decode(encoded);
        if(tag.equals(WallTile.PARSE_TAG))
            return WallTile.decode(encoded);

        return null;
    }

    public static int getNewID(){
        return ID_COMPTEUR++;
    }
}

package com.towerdefense.game.entity;


import com.towerdefense.game.util.Cst;


/**
 * Carte du terrain de jeu.
 * Composé d'un tableau de tile.
 */
public class Map extends Entity{

    public static final String PARSE_TAG = "m";

    private int nbTileWidth;

    private int nbTileHeight;

    /**
     * Constructeur
     * @param nbTileWidth nombre de taille en largeur
     * @param nbTileHeight nombre de taille en hauteur
     */
    public Map(int nbTileWidth, int nbTileHeight){
        this.nbTileWidth = nbTileWidth;
        this.nbTileHeight = nbTileHeight;
    }

    public float getWidth(){
        return Cst.TILE_WIDTH * nbTileWidth;
    }

    public float getHeight(){
        return Cst.TILE_HEIGHT * nbTileHeight;
    }

    public int getNbTileWidth(){
        return nbTileWidth;
    }

    public int getNbTileHeight(){
        return nbTileHeight;
    }

    @Override
    public String encode() {
        return ID+
                Cst.ENTITY_SEPARATOR+
                PARSE_TAG+
                Cst.ENTITY_SEPARATOR+
                nbTileWidth+
                Cst.ENTITY_SEPARATOR+
                nbTileHeight;
    }

    @Override
    public void update(Entity e) {
    }

    public static Map decode(String encoded){
        String[] split = encoded.split(Cst.ENTITY_SEPARATOR);

        if(split.length != 4)
            return null;

        Map m = new Map(Integer.parseInt(split[2]), Integer.parseInt(split[3]));
        m.setID(Integer.parseInt(split[0]));

        return m;
    }
}

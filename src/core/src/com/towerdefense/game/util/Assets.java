package com.towerdefense.game.util;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Assets s'occupe de charger les assets utilés par le jeu pour un accès optimisé
 */
public class Assets {

    public static final String LOG = "Assets";

    /**
     * Le manager qui contient tous les assets
     */
    public static final AssetManager manager = new AssetManager();

    /**
     * Atlas des textures
     */
    private static AssetDescriptor<TextureAtlas> textures;

    /**
     * Skin des fenêtres, boutons, etc...
     */
    public static final Skin uiSkin = new Skin(Gdx.files.internal("skin/uiskin.json"));

    public static String TEXTURES;

    public static final BitmapFont fontEnemy = new Skin(Gdx.files.internal("skin/uiskin.json")).getFont("default-font");

    public static final String FLOOR = "floor";
    public static final String TURRET_BLUE = "turret_blue";
    public static final String TURRET_RED = "turret_red";
    public static final String TURRET_YELLOW = "turret_yellow";
    public static final String ENTRY = "entry";
    public static final String EXIT = "exit";
    public static final String ENEMY_BLUE = "enemy_blue";
    public static final String ENEMY_RED = "enemy_red";
    public static final String ENEMY_YELLOW = "enemy_yellow";
    public static final String WALL = "wall";
    public static final String BULLET_BLUE = "bullet_blue";
    public static final String BULLET_RED = "bullet_red";
    public static final String BULLET_YELLOW = "bullet_yellow";


    /**
     * Charge les assets
     */
    public static void init(){
        TEXTURES = TexturePackManager.getTexturePackPath();
        textures = new AssetDescriptor<TextureAtlas>(TEXTURES, TextureAtlas.class);

        manager.load(textures);
    }

    public static void initReload(){
        manager.unload(TEXTURES);
    }

    public static void updateConstantes(){
        Cst.WORLD_TO_GRAPHIC = (float) ((TextureAtlas) Assets.manager.get(Assets.TEXTURES)).findRegion(Assets.FLOOR).getRegionWidth();
    }

    /**
     * Dispose les assets
     */
    public static void dispose(){
        manager.dispose();
        uiSkin.dispose();
        textures = null;
    }
}

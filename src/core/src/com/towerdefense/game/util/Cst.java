package com.towerdefense.game.util;

import com.towerdefense.game.controller.AllScreenInputController;

/**
 * Constantes globales
 */
public class Cst {
    // DEBUG
    public static boolean DEBUG = false;


    // Input controller global
    public static final AllScreenInputController allScreenInputController = new AllScreenInputController();


    // ENTITIES
    public static final String ENTITY_SEPARATOR = "#";
    public static float WORLD_TO_GRAPHIC = 128f;

    public static final int BLUE_TYPE = 1;
    public static final int RED_TYPE = 2;
    public static final int YELLOW_TYPE = 3;

    // --- TILE
    public static final float TILE_WIDTH = 1f;
    public static final float TILE_HEIGHT = 1f;

    // --- BULLET
    public static final float BULLET_WIDTH = TILE_WIDTH/8f;
    public static final float BULLET_HEIGHT = TILE_HEIGHT/8f;
    public static final float BULLET_MAX_VELOCITY = BULLET_WIDTH*50f;

    // --- ENEMY
    public static final float ENEMY_WIDTH = TILE_WIDTH/2f;
    public static final float ENEMY_HEIGHT = TILE_HEIGHT/2f;
    public static final float ENEMY_MAX_VELOCITY = ENEMY_WIDTH*5f;
    public static final int ENEMY_COST = 100;
    public static final int ENEMY_LIFE = 100;
    public static final int ENEMY_SPEED_COST = 100;
    public static final int ENEMY_LIFE_COST = 100;

    // --- TURRET
    public static final int TURRET_COST = 400;
    public static final int TURRET_LEVEL_UP_COST = 400;
    public static final double TURRET_SELL_PERCENT = 0.5;

    public static final float TURRET_RANGE = TILE_WIDTH * 3f;
    public static final float TURRET_SHOOT_RATE = 1f;
    public static final int TURRET_DAMAGE = 10;

    public static final float TURRET_SAME_COLOR_DAMAGE_MULTIPLIER = 1.5f;
    public static final float TURRET_DAMAGE_MULTIPLIER_PER_LEVEL = 2f;

    // --- WALL
    public static final int WALL_COST = 50;
    public static final int WALL_TYPE = 0;


    // SERVER
    public static final String SERVER_SEPARATOR = "&";


    // SERVER GAME
    public static final int SERVER_GAME_PORT = 14144;


    // TWITCH CLIENT
    public static final int TWITCH_CLIENT_REQUEST_INTERVAL = 1000;


    // HUD
    public static final int HUD_WIDTH = 200;


    // ACTION
    public static final String ACTION_SEPARATOR = "!";
    public static final String ACTION_SEPARATOR_VARIABLE = ":";
    public static final String ACTION_SEPARATOR_UPDATE = ";";


    // RTT
    public static final long RTT_SLEEP = 2000;


    // GAMELOOP
    public final static float GAMELOOP_UPDATE_RATE = 0.1f;


    // TEXTURE
    public static final String TEXTURE_DIRECTORY = "texture/";
    public static final String TEXTURE_DEFAULT_PACK = "simple_textures.atlas";
}

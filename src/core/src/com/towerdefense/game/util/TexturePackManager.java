package com.towerdefense.game.util;


import com.badlogic.gdx.utils.Array;

import java.io.File;
import java.io.FilenameFilter;

public class TexturePackManager {

    public static String currentTexturePack = Cst.TEXTURE_DEFAULT_PACK;

    public static Array<String> getTexturePackAvailable(){
        Array<String> texturePacks = new Array<String>();

        File[] files  = new File(Cst.TEXTURE_DIRECTORY).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.endsWith(".atlas");
            }
        });

        for(File current : files){
            if(current.isFile()){
                texturePacks.add(current.getName());
            }
        }

        return texturePacks;
    }

    public static void changeTexturePack(String filename){
        if(filename.equals(currentTexturePack))
            return;

        currentTexturePack = filename;
        Assets.initReload();
    }

    public static String getTexturePackPath(){
        return Cst.TEXTURE_DIRECTORY+currentTexturePack;
    }
}

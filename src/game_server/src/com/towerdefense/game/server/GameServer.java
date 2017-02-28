package com.towerdefense.game.server;


import com.towerdefense.game.Game;
import com.towerdefense.game.net.Server;
import com.towerdefense.game.system.GameLoop;
import com.towerdefense.game.system.ServerMessageManager;
import com.towerdefense.game.util.Log;

public class GameServer {

    private  static final String LOG = "GameServer";

    private static final long TARGET_FPS = 60;

    private static final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;

    private static final Server server = new Server();

    private static final GameLoop gameLoop = new GameLoop();

    public static void main(String[] arg){
        Game.gameLoop = gameLoop;
        Game.server = server;
        gameLoop.initServerSide();

        server.addMessageListener((ServerMessageManager) gameLoop.messageManager);
        ((ServerMessageManager) gameLoop.messageManager).addSendMessageListener(server);

        new Thread(new Runnable() {
            @Override
            public void run() {
                long lastLoopTime = System.nanoTime();
                while(true){
                    long now = System.nanoTime();
                    float updateLength = (float)now - (float)lastLoopTime;
                    lastLoopTime = now;

                    gameLoop.update(updateLength / 1000000000f);

                    try{
                        long s = (lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000;
                        Thread.sleep(s < 0? 0 : s);
                    }catch (InterruptedException e){
                        Log.e(LOG, "Error sleep gameloop thread", e);
                    }
                }
            }
        }).start();

        server.run();
    }
}

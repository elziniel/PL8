package com.towerdefense.game.system.listener;


import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.EventListener;

public interface MessageManagerListener extends EventListener {

    void sendMessage(String message);

    void sendMessage(SelectionKey key, String message);

    void broadcastMessage(String message);
}

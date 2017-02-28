package com.towerdefense.game.net.listener;


import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.EventListener;

public interface SocketChannelListener extends EventListener{

    void newMessage(SelectionKey key, String message);

    void newMessage(String message);

    void newAccept(SelectionKey key);

    void connectSucced();

    void connectionClosed(SelectionKey key);
}

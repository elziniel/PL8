package com.towerdefense.game.system.listener;


import java.util.EventListener;

public interface ConnectionStatusListener extends EventListener{

    public void connectionSuccess();

    public void connectionFailure(String message);
}

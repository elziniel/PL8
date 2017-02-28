package com.towerdefense.game.net;


import com.towerdefense.game.net.listener.SocketChannelListener;
import com.towerdefense.game.system.listener.MessageManagerListener;
import com.towerdefense.game.util.Log;
import com.towerdefense.game.util.Cst;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.event.EventListenerList;

public class Server implements MessageManagerListener {

    public static final String LOG = "Server";

    private static final int BUFFER_SIZE = 256;

    public static final int PORT = Cst.SERVER_GAME_PORT;

    private ServerSocketChannel serverChannel;

    private Selector selector;

    private ByteBuffer buf = ByteBuffer.allocate(BUFFER_SIZE);

    private final EventListenerList listeners = new EventListenerList();

    private StringBuilder builder = new StringBuilder();

    private LinkedList<String> messages = new LinkedList<String>();

    public Server(){
        try {
            serverChannel = ServerSocketChannel.open();
            serverChannel.socket().bind(new InetSocketAddress(PORT));
            serverChannel.configureBlocking(false);

            selector = Selector.open();

            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (IOException e){
            Log.e(LOG, "Error constructor", e);
        }

    }

    public void run(){
        try{
            Log.l(LOG, "Server starting on port " + PORT);

            Iterator<SelectionKey> it;
            SelectionKey key;
            while(serverChannel.isOpen()){
                selector.select();
                it = selector.selectedKeys().iterator();
                while(it.hasNext()){
                    key = it.next();
                    it.remove();

                    if(!key.isValid()) {
                        fireConnectionClosed(key);
                        continue;
                    }

                    if(key.isAcceptable()) {
                        SelectionKey keySocket = accept(key);
                        if(keySocket != null)
                            fireNewAccept(keySocket);
                    }

                    if(key.isReadable()) {
                        read(key);
                        while(!messages.isEmpty()){
                            fireNewMessage(key, messages.getFirst());
                            messages.removeFirst();
                        }
                    }
                }
            }
        }catch (IOException e){
            Log.e(LOG, "Error on run", e);
        }
    }

    protected SelectionKey accept(SelectionKey key){
        SocketChannel socketChannel = null;
        SelectionKey keySocket = null;
        try{
            socketChannel = ((ServerSocketChannel) key.channel()).accept();
            socketChannel.configureBlocking(false);
            keySocket = socketChannel.register(selector, SelectionKey.OP_READ, socketChannel);
            Log.l(LOG, "New client connected");
        }catch (IOException e){
            Log.e(LOG, "Error accept", e);
        }
        return keySocket;
    }

    protected void read(SelectionKey key){
        SocketChannel socketChannel = (SocketChannel) key.channel();

        buf.clear();
        int read = 0;
        try{
            while((read = socketChannel.read(buf)) > 0){
                buf.flip();
                byte[] bytes = new byte[buf.limit()];
                buf.get(bytes);
                builder.append(new String(bytes));
                buf.clear();

                String tmp = builder.toString();
                if(tmp.contains("\n")){
                    if(tmp.endsWith("\n")){
                        String[] split = tmp.split("\n");
                        for(String m : split){
                            messages.add(m);
                        }
                        builder = new StringBuilder();
                    }else{
                        String[] split = tmp.split("\n");
                        for(int i = 0; i < split.length -1; i++){
                            messages.add(split[i]);
                        }
                        builder = new StringBuilder();
                        builder.append(split[split.length -1]);
                    }
                }
            }

            if(read < 0) {
                fireConnectionClosed(key);
                socketChannel.close();
            }else{
                String tmp = builder.toString();
                if(tmp.endsWith("\n")){
                    messages.add(tmp.split("\n")[0]);
                }
            }
        }catch (IOException e){
            fireConnectionClosed(key);
        }
    }

    private void write(SelectionKey key, String message){
        if(message == null)
            return;

        SocketChannel socketChannel = (SocketChannel) key.channel();

        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
        boolean remain = true;
        try{
            while(remain){
                if(socketChannel.write(buffer) > 0 && !buffer.hasRemaining()){
                    remain = false;
                }
            }
        }catch (IOException e){
            fireConnectionClosed(key);
        }
    }

    private void broadcast(String message){
        if(message == null)
            return;

        if(!message.endsWith("\n"))
            message += "\n";

        for(SelectionKey key : selector.keys()){
            if(key.isValid() && key.channel() instanceof SocketChannel){
                write(key, message);
            }
        }
    }

    public void addMessageListener(SocketChannelListener listener){
        listeners.add(SocketChannelListener.class, listener);
    }

    public void removeMessageListener(SocketChannelListener listener){
        listeners.remove(SocketChannelListener.class, listener);
    }

    private void fireNewMessage(SelectionKey key, String message){
        for(SocketChannelListener listener : listeners.getListeners(SocketChannelListener.class))
            listener.newMessage(key, message);
    }

    private void fireNewAccept(SelectionKey key){
        for(SocketChannelListener listener : listeners.getListeners(SocketChannelListener.class))
            listener.newAccept(key);
    }

    private void fireConnectionClosed(SelectionKey key){
        key.cancel();
        for(SocketChannelListener listener : listeners.getListeners(SocketChannelListener.class))
            listener.connectionClosed(key);
    }

    @Override
    public void sendMessage(String message) {}

    @Override
    public void sendMessage(SelectionKey key, String message) {
        write(key, message);
    }

    @Override
    public void broadcastMessage(String message) {
        broadcast(message);
    }
}

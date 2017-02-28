package com.towerdefense.game.net;


import com.towerdefense.game.net.listener.SocketChannelListener;
import com.towerdefense.game.system.listener.MessageManagerListener;
import com.towerdefense.game.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.event.EventListenerList;

public class Client implements Runnable, MessageManagerListener {

    public static final String LOG = "Client";

    private SocketChannel socketChannel;

    private Selector selector;

    private ByteBuffer buf = ByteBuffer.allocate(256);

    private final EventListenerList listeners = new EventListenerList();

    private StringBuilder builder = new StringBuilder();

    private LinkedList<String> messages = new LinkedList<String>();

    public Client(InetSocketAddress addr){
        try{
            socketChannel = SocketChannel.open();
            socketChannel.connect(addr);
            socketChannel.configureBlocking(false);

            selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_READ);

        }catch (IOException e){
            Log.e(LOG, "Error contructor", e);
        }
    }

    @Override
    public void run() {
        Log.d(LOG, "Client start");

        try{
            while(!socketChannel.finishConnect()){}
            fireConnectSucced();

            Iterator<SelectionKey> it;
            SelectionKey key;
            while(socketChannel.isConnected()){
                selector.select();
                it = selector.selectedKeys().iterator();
                while(it.hasNext()){
                    key = it.next();
                    it.remove();

                    if(!key.isValid()){
                        fireConnectionClosed();
                        continue;
                    }

                    if(key.isReadable()){
                        read(key);
                        while(!messages.isEmpty()){
                            fireNewMessage(messages.getFirst());
                            messages.removeFirst();
                        }
                    }
                }
            }
        }catch (IOException e){
            Log.e(LOG, "Error run", e);
        }

        Log.d(LOG, "Client end");
    }

    private void read(SelectionKey key){
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
                close();
            }else{
                String tmp = builder.toString();
                if(tmp.endsWith("\n")){
                    messages.add(tmp.split("\n")[0]);
                }
            }
        }catch (IOException e){
            Log.e(LOG, "Connection closed by server", e);
            key.cancel();
            close();
        }
    }

    private void write(String message){
        if(message == null)
            return;

        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
        boolean remain = true;
        try{
            while(remain){
                if(socketChannel.write(buffer) > 0 && !buffer.hasRemaining()){
                    remain = false;
                }
            }
        }catch (IOException e){
            Log.e(LOG, "Error write", e);
            close();
        }
    }

    public void close(){
        fireConnectionClosed();
        try {
            socketChannel.close();
        } catch (IOException e) {
            Log.e(LOG, "Error close socket channel", e);
        }
    }

    public void addMessageListener(SocketChannelListener listener){
        listeners.add(SocketChannelListener.class, listener);
    }

    public void removeMessageListener(SocketChannelListener listener){
        listeners.remove(SocketChannelListener.class, listener);
    }

    private void fireNewMessage(String message){
        for(SocketChannelListener listener : listeners.getListeners(SocketChannelListener.class))
            listener.newMessage(message);
    }

    private void fireConnectSucced(){
        for(SocketChannelListener listener : listeners.getListeners(SocketChannelListener.class))
            listener.connectSucced();
    }

    private void fireConnectionClosed(){
        for(SocketChannelListener listener : listeners.getListeners(SocketChannelListener.class))
            listener.connectionClosed(null);
    }

    @Override
    public void sendMessage(String message) {
        write(message);
    }

    @Override
    public void sendMessage(SelectionKey key, String message) {}

    @Override
    public void broadcastMessage(String message) {}
}

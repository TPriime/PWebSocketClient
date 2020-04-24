package com.prime.net;

import java.io.*;
import java.util.function.Consumer;

import com.neovisionaries.ws.client.*;

public class PWebSocket {
    private final String SERVER;
    private WebSocketAdapter adapter;
    private WebSocket webSocket;
    private Thread reconnectThread;


    public PWebSocket (String server, WebSocketAdapter wsa) {
        SERVER = server;
        adapter = wsa;
    }


    private void createFactory() throws IOException {
        webSocket = new WebSocketFactory()
                .setConnectionTimeout(5000)
                .createSocket(SERVER)
                .addListener(adapter)
                .addExtension(WebSocketExtension.PERMESSAGE_DEFLATE);
    }


    private void connectLive(long interval, Consumer onRetry){
        reconnectThread = new Thread(()->{
            while(true){
                if(Thread.interrupted()) return;
                try {
                    if(!webSocket.isOpen()) {
                        createFactory();
                        webSocket.connect(); //try to connect
                    }
                }
                catch(Exception e){
                    //handle exception...
                    onRetry.accept(e);
                }
                finally {
                    try{Thread.sleep(interval);} catch (Exception e){}
                }
            }
        });
        reconnectThread.start();
    }


    public void connect() throws WebSocketException, IOException {
        createFactory();
        webSocket.connect(); //try to connect
    }

    public void connect(long retryInterval, Consumer<Exception> onRetry) throws IOException {
        createFactory();
        connectLive(retryInterval, onRetry);
    }


    public void send(String text){
        webSocket.sendText(text);
    }

    public void send(byte[] message){
        webSocket.sendBinary(message);
    }


    public void disconnect(){
        webSocket.disconnect();
        while(reconnectThread.isAlive())
            reconnectThread.interrupt();
    }
}
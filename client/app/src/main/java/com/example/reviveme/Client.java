package com.example.reviveme;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client implements Runnable {

    static final String TAG = "IVAN";
    private DatagramSocket socket;
    private String ip;
    private Thread send, receive, listen;
    private int port;
    boolean currState;

    protected MainActivity context;

    // constructor
    public Client(String IP, int PORT, Context context) throws UnknownHostException {
        this.ip = IP;
        this.port = PORT;
        this.context = (MainActivity) context;
    }

    @Override
    public void run(){
        try {
            socket = new DatagramSocket();
            //ip = InetAddress.getByName(address);
            socket.setBroadcast(true);
            socket.setReuseAddress(true);
            String connection = "Master here!";
            send(connection);

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        listen();

    }


    // opening connection
    public boolean openConnection() {

        run();
        return true;
    }

    // receiving messages
    // waits for a packet
    public String receive() {
        byte[] data = new byte[1024];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        while (true) {
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String message = new String(packet.getData());
            return message;
        }

    }

    // sending message over UDP
    public void send(final String msg) {
        send = new Thread("Send") {
            @Override
            public void run() {
                DatagramPacket packet = null;
                byte[] bytes = msg.getBytes();
                try {
                    packet = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(ip),port);

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                try {
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        send.start();
    }

    // listen method
    public void listen() {
        Log.d(TAG, "Listening...");
        listen = new Thread("Listen") {
            @Override
            public void run() {
                while (true) {
                    final String message = receive();
                    Log.d(TAG, message);
                    if (message.contains("Shock completed!")) {
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                context.animation.setSpeed(1);
                                context.animation.playAnimation();
                            }
                        });
                    } else if(message.contains("T:")){
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String temp = message.split(":")[1];
                                context.tempData.setText(temp + "°C");
                            }
                        });
                    } else if(message.contains("P:")){
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String pres = message.split(":")[1];
                                context.presData.setText(pres + "hPa");
                            }
                        });
                    } else if(message.contains("H:")){
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String hum = message.split(":")[1];
                                context.humData.setText(hum);
                            }
                        });
                    }
                }
            }
        };
        listen.start();
    }

}

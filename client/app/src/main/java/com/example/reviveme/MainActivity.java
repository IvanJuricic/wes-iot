package com.example.reviveme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity{

    private final static String TAG = "6G Only";

    LottieAnimationView animation;
    Button shockBtn;

    private Client client;

    Thread listen, run, connect;

    String HOST_IP = "192.168.43.223";
    int HOST_PORT = 3000;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = MainActivity.this;

        shockBtn = findViewById(R.id.shock_button);

        animation = (LottieAnimationView)findViewById(R.id.animationView);

        Thread t = null;
        try {
            t = new Thread(client = new Client(HOST_IP, HOST_PORT,mContext));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        t.start();

    }

    public void shock(View view) {

        String message = "SHOCK!";

        send(message);

        shockBtn.setEnabled(false);



        //animation.playAnimation();

    }

    public void send(String message) {
        client.send(message);
    }


}

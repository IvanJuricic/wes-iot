package com.example.reviveme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity{

    private final static String TAG = "6G Only";

    LottieAnimationView animation;
    Button shockBtn;
    TextView humData, tempData, presData;

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

        tempData = findViewById(R.id.temperatureData);
        humData = findViewById(R.id.humidityData);
        presData = findViewById(R.id.pressureData);

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

        //animation.playAnimation();

    }

    public void send(String message) {
        client.send(message);
    }


    public void humidity(View view) {

        String message = "HUMIDITY!";

        send(message);
    }

    public void temperature(View view) {

        String message = "TEMPERATURE!";
        send(message);
    }

    public void pressure(View view) {

        String message = "PRESSURE!";
        send(message);
    }
}

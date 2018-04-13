package android.controller;

import java.io.PrintStream;
import java.util.List;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends Activity implements SensorEventListener{
    //Botones y Labels
    Button bshoot;
    //Cliente de Servidor
    Socket client;
    client c;
    BufferedReader input;
    PrintStream output;
    String messageOut="3";
    String messageOutOld="1";
    String messageIn;
    //Acelerometro
    private long last_update = 0, last_movement = 0;
    private float  prevY = 0;
    private float curY = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitNetwork().build());
        c = new client(this);
        c.start();

        bshoot= (Button) findViewById(R.id.Shot);
        bshoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageOut = "1";
            }
        });
    }
    public void init() {
        String ip = "192.168.1.106";
        int port = 9000;

        try {
            client = new Socket(ip, port);
            input = new BufferedReader( new InputStreamReader(client.getInputStream()));
            output = new PrintStream(client.getOutputStream());

        } catch (Exception e) {

        }
    }
    public boolean isRefreshed(){
        return (messageOut != messageOutOld);
    }
    public void infoTrade(){
        try{

                output.println(messageOut);
                messageOutOld=messageOut;



                messageIn = input.readLine();



        }catch (Exception e){

        }
    }
    private void finit(){
        try{
            input.close();
            output.close();
            client.close();
        }catch (Exception e){

        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (sensors.size() > 0) {
            sm.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    protected void onStop() {
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sm.unregisterListener(this);
        super.onStop();
        finit();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
            long current_time = event.timestamp;


            curY = event.values[1];


            if (prevY == 0) {

                last_update = current_time;
                last_movement = current_time;
                prevY = curY;

            }
            long time_difference = current_time - last_update;
            if (time_difference > 0) {

                float movement = Math.abs((curY) - (prevY)) / time_difference;
                int limit = 1500;
                float min_movement = 1E-6f;
                if (movement > min_movement) {
                    if (current_time - last_movement >= limit) {
                    }
                    last_movement = current_time;
                }
                prevY = curY;
                last_update = current_time;
            }
            if ((int)curY>0){
                messageOut="2";
            }else if((int)curY < 0){
                messageOut=("3");
            }else if((int)curY == 0){
                messageOut=("0");
            }

            ((TextView) findViewById(R.id.txtAccY)).setText("Acelerómetro Y: " + (int)curY);

        }

    }
}

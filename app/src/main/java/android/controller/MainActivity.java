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
    String Score;
    String Level;
    String Row;
    String Next;

    //Cliente de Servidor
    Socket client;
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


        bshoot= (Button) findViewById(R.id.Shot);
        bshoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageOut = "1";
                new Thread(new ClientThread()).start();
            }
        });
    }

    public void spliter(String str){
        String[] parts = str.split(",");
        Score = parts[0];
        Level = parts[1];
        Row = parts[2];
        Next = parts[3];

    }
    //Se tuvo que crear para pasar de lista de java a Lista normal y usarla
    public Lista toList(List<Sensor> oldList){
        Lista newList= new Lista();
        for (int i=0;i<oldList.size();i++){
            newList.add(oldList.get(i));
        }
        return newList;
    }







    class ClientThread implements Runnable {
        @Override
        public void run(){
            //Se necesita cambiar el Ip
            String ip = "192.168.1.106";
            int port = 9000;
            messageOutOld = messageOut;
            try {

                client = new Socket(ip, port);
                input = new BufferedReader( new InputStreamReader(client.getInputStream()));
                output = new PrintStream(client.getOutputStream());
                output.println(messageOut);


                messageIn = input.readLine();
                spliter(messageIn);
                input.close();
                output.close();
                client.close();



            } catch (Exception e) {

            }
        }
    }
    public boolean isRefreshed(){
        return (messageOut != messageOutOld);
    }
    @Override
    protected void onResume() {
        super.onResume();

        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        Lista sensors =   toList(sm.getSensorList(Sensor.TYPE_ACCELEROMETER));
        //Error de tipos, se tuvo que crear un método que pasara de lista util de java a una de creador
        if (sensors.getLength() > 0) {
            sm.registerListener(this, sensors.getValue(0), SensorManager.SENSOR_DELAY_GAME);
        }
    }
    @Override
    protected void onStop() {
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sm.unregisterListener(this);
        super.onStop();
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
            if ((int)curY == 1){
                messageOut="2";
                if (isRefreshed()){
                    new Thread(new ClientThread()).start();
                }
            }if((int)curY == -1){
                messageOut=("3");
                if (isRefreshed()){
                    new Thread(new ClientThread()).start();
                }
            }if((int)curY == 0){
                messageOut=("0");
                if (isRefreshed()){
                    new Thread(new ClientThread()).start();
                }
            }


            ((TextView) findViewById(R.id.txtAccY)).setText("Acelerómetro Y: " + (int)curY);
            ((TextView) findViewById(R.id.Score)).setText("Score: " + Score);
            ((TextView) findViewById(R.id.Level)).setText("Level: " + Level);
            ((TextView) findViewById(R.id.Row)).setText("Now: " + Row);
            ((TextView) findViewById(R.id.Next)).setText("Next: " + Next);

        }

    }
}

package com.example.androidstudiosmrdi.spaceshooter;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private GameView gameView;
    private float temp;

    DatabaseHandler db = new DatabaseHandler(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //vzdy zapnuty displej
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //ziskanie velkosti obrazovky
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        gameView = new GameView(this, point.x, point.y);
        setContentView(gameView);


        //Snímač akcelerometra
        SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
        manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);

    }


    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //temp = event.values[0];

        if (event.values[0] > 1){
            gameView.steerLeft(event.values[0]);
        }
        else if (event.values[0] < -1){
            gameView.steerRight(event.values[0]);
        }else{
            gameView.stay();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onBackPressed() {

        gameView.paused = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

               if(gameView.isGameOver == true && gameView.writedScore == false){

                   if(gameView.SCORE > 0){
                       //add score to database
                       db.addScore(new Score(String.valueOf(gameView.finalScore)));
                   }
                   gameView.writedScore = true;

               }

                break;
        }
        return super.onTouchEvent(event);
    }

}

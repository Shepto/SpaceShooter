package com.example.androidstudiosmrdi.spaceshooter;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

/**
 * Created by Sebastian on 17.11.2017.
 * DRAWABLE -----   https://opengameart.org/content/space-shooter-art
 */

public class GameView extends SurfaceView implements Runnable {

    private Thread gameThread;
    private volatile boolean isPlaying;
    private Player player;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private ArrayList<Laser> lasers;
    private ArrayList<Meteor> meteors;
    private ArrayList<Enemy> enemies;
    private int screenSizeX, screenSizeY;
    private int counter = 0;
    private ScoreManager scoremanager;
    public static int SCORE = 0;
    private Sound sound;

    private boolean shielded = false;
    private boolean usedShield = false;
    private boolean canUseShield = true;
    private boolean resumed = false;
    public boolean paused = false;

    public boolean writedScore = true;
    public volatile boolean isGameOver;


    private Rect r;
    Bitmap bitmap;

    public int finalScore = 0;

    public GameView(Context context, int screenSizeX, int screenSizeY) {

        super(context);

        this.screenSizeX = screenSizeX;
        this.screenSizeY = screenSizeY;
        scoremanager = new ScoreManager(context);
        sound = new Sound(context);

        paint = new Paint();
        surfaceHolder = getHolder();
        reset();

    }

    public static Context getAppContext() {
        return GameView.getAppContext();
    }

    void reset() {

        SCORE = 0;
        player = new Player(getContext(), screenSizeX, screenSizeY, sound);
        lasers = new ArrayList<>();
        meteors = new ArrayList<>();
        enemies = new ArrayList<>();
        isGameOver = false;
        canUseShield = true;
        shielded = false;
        resumed = true;

    }

    @Override
    public void run() {

        while (isPlaying) {
            if (!isGameOver && writedScore == true) {
                update();
                draw();
                control();
            }
            if(paused){
                draw();
                pause();
            }
        }
    }

    public void update() {


        player.update();

        if (counter % 200 == 0) {
            player.fire();
        }

        //shield control
        if(usedShield == true){

            if(shielded == true){
                canUseShield = false;
            }
            else if (counter % 8000 == 0) {
                canUseShield = true;
                usedShield = false;
            }

        }

        for (Meteor m : meteors) {

            m.update();

            if (Rect.intersects(m.getCollision(), player.getCollision())) {
                m.destroy();

                if(shielded == true){

                    shielded = false;
                }else{
                    player.lifes -= 1;
                }

                if(player.lifes == 0){

                    isGameOver = true;
                    writedScore = false;

                    finalScore = SCORE;


                    if (SCORE >= scoremanager.getHighScore()) {

                        scoremanager.saveHighScore(SCORE);
                    }
                }
            }

            for (Laser l : player.getLasers()) {
                if (Rect.intersects(m.getCollision(), l.getCollision())) {
                    m.hit();
                    l.destroy();
                }
            }
        }

        boolean deleting = true;
        while (deleting) {

            if (meteors.size() != 0) {

                if (meteors.get(0).getY() > screenSizeY) {
                    meteors.remove(0);
                }
            }

            if (meteors.size() == 0 || meteors.get(0).getY() <= screenSizeY) {
                deleting = false;
            }
        }
        if (counter % 1000 == 0) {
            meteors.add(new Meteor(getContext(), screenSizeX, screenSizeY, sound));
        }

        for (Enemy e : enemies) {

            e.update();

            if (Rect.intersects(e.getCollision(), player.getCollision())) {

                e.destroy();

                if(shielded == true){

                    shielded = false;
                }else{
                    player.lifes -= 1;

                }

                if(player.lifes == 0){

                    isGameOver = true;
                    writedScore = false;

                    finalScore = SCORE;

                    if (SCORE >= scoremanager.getHighScore()){
                        scoremanager.saveHighScore(SCORE);
                    }
                }

            }

            for (Laser l : player.getLasers()) {

                if (Rect.intersects(e.getCollision(), l.getCollision())) {

                    e.hit();
                    l.destroy();
                }
            }
        }

        deleting = true;

        while (deleting) {

            if (enemies.size() != 0) {
                if (enemies.get(0).getY() > screenSizeY) {
                    enemies.remove(0);
                }
            }

            if (enemies.size() == 0 || enemies.get(0).getY() <= screenSizeY) {
                deleting = false;
            }
        }
        if (counter % 2000 == 0) {
            enemies.add(new Enemy(getContext(), screenSizeX, screenSizeY, sound));
        }



    }

    public void draw() {

        if (surfaceHolder.getSurface().isValid()) {

            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);
            canvas.drawBitmap(player.getBitmap(), player.getX(), player.getY(), paint);


            for (Laser l : player.getLasers()) {
                canvas.drawBitmap(l.getBitmap(), l.getX(), l.getY(), paint);
            }

            for (Meteor m : meteors) {
                canvas.drawBitmap(m.getBitmap(), m.getX(), m.getY(), paint);
            }

            for (Enemy e : enemies) {
                canvas.drawBitmap(e.getBitmap(), e.getX(), e.getY(), paint);
            }

            drawLives();

            if (isGameOver) {
                drawGameOver();
            }

            if(paused && isGameOver == false){

                drawPause();
            }


            drawShield();
            drawScore();



            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    void drawScore() {
        Paint score = new Paint();
        score.setTextSize(40);
        score.setColor(Color.GRAY);
        canvas.drawText("Score : " + SCORE, screenSizeX/20, screenSizeY/18+10, score);
    }

    void drawLives(){

        int j = screenSizeX/4;

        for (int i= 0; i < player.lifes; i++) {

            canvas.drawBitmap(player.getBitmapLives(), (screenSizeX-(screenSizeX/2)+j),  screenSizeY/30, paint);
            j += 60;
        }
    }

    void drawShield(){

        if(canUseShield == true){
            Paint shield = new Paint();
            shield.setTextSize(40);
            shield.setColor(Color.RED);
            canvas.drawText("Shield charged!", screenSizeX/20, screenSizeY - 20, shield);
        }

        if(shielded == true){

            canvas.drawBitmap(player.getBitmapShield(), player.getX()-30,  player.getY() - 30, paint);
        }
    }

    void drawPause(){

        Paint pause = new Paint();
        pause.setTextSize(100);
        pause.setTextAlign(Paint.Align.CENTER);
        pause.setColor(Color.GRAY);
        canvas.drawText("PAUSED", screenSizeX / 2, screenSizeY / 2, pause);

        Paint rect = new Paint();
        rect.setColor(Color.GRAY);
        r = new Rect();
        //left, top, right, bottom
        r.set(getWidth()/3 - 60, getHeight()/2 + 20, getWidth()/2 + 180, getHeight()/2 + 120);
        canvas.drawRect(r, rect);

        Paint main = new Paint();
        main.setTextSize(60);
        main.setTextAlign(Paint.Align.CENTER);
        main.setColor(Color.WHITE);
        canvas.drawText("Go to menu", screenSizeX / 2 - 5, screenSizeY / 2 + 90, main);

    }

    void drawGameOver() {
        Paint gameOver = new Paint();
        gameOver.setTextSize(100);
        gameOver.setTextAlign(Paint.Align.CENTER);
        gameOver.setColor(Color.RED);
        canvas.drawText("GAME OVER", screenSizeX / 2, screenSizeY / 2, gameOver);
        Paint highScore = new Paint();
        highScore.setTextSize(50);
        highScore.setTextAlign(Paint.Align.CENTER);
        highScore.setColor(Color.GRAY);
        canvas.drawText("HighScore : " + scoremanager.getHighScore(), screenSizeX / 2, (screenSizeY / 2) + 60, highScore);
    }

    public void steerLeft(float speed) {

        player.steerLeft(speed);
    }

    public void steerRight(float speed) {

        player.steerRight(speed);
    }

    public void stay() {

        player.stay();
    }

    public void control() {

        try {
            if (counter == 10000) {
                counter = 0;
            }
            gameThread.sleep(20);
            counter += 20;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {

        isPlaying = false;
        resumed = true;

        try {
            gameThread.join();
            sound.pause();

        } catch (InterruptedException e) {
                e.printStackTrace();
        }
    }

    public void resume() {

        paused = false;
        resumed = true;
        isPlaying = true;
        sound.resume();
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                //vykreslenie rectanglu a spat do menu pri touch na neho
                if(paused == true){

                    if(x > getWidth()/3 - 60 && x < getHeight()/2 + 20 && y > getWidth()/2 + 180 && y < getHeight()/2 + 120){

                        Intent intent = new Intent(GameView.getAppContext(), Menu.class);
                        GameView.getAppContext().startActivity(intent);
                    }
                }

                //pauza a dont resume pri stlaceni tlacidla
                if(paused == true){

                    resume();
                }


                if (isGameOver && writedScore == true){

                    reset();
                }

                if(canUseShield == true){

                    if(resumed == false){

                        shielded = true;
                        usedShield = true;
                    }else{

                        resumed = false;
                    }

                }

                break;
        }
        return super.onTouchEvent(event);
    }

}

package com.example.androidstudiosmrdi.spaceshooter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.util.Log;
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
    private volatile boolean isGameOver;

    public GameView(Context context, int screenSizeX, int screenSizeY) {

        super(context);

        this.screenSizeX = screenSizeX;
        this.screenSizeY = screenSizeY;
        scoremanager = new ScoreManager(context);

        paint = new Paint();
        surfaceHolder = getHolder();

        reset();
    }

    void reset() {

        SCORE = 0;
        player = new Player(getContext(), screenSizeX, screenSizeY);
        lasers = new ArrayList<>();
        meteors = new ArrayList<>();
        enemies = new ArrayList<>();
        isGameOver = false;
    }

    @Override
    public void run() {

        while (isPlaying) {
            if (!isGameOver) {
                update();
                draw();
                control();
            }
        }
    }

    public void update() {

        player.update();

        if (counter % 200 == 0) {
            player.fire();
        }

        for (Meteor m : meteors) {

            m.update();

            if (Rect.intersects(m.getCollision(), player.getCollision())) {
                m.destroy();

                player.lifes -= 1;

                if(player.lifes == 0){

                    isGameOver = true;

                    if (SCORE>= scoremanager.getHighScore()){
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
            meteors.add(new Meteor(getContext(), screenSizeX, screenSizeY));
        }

        for (Enemy e : enemies) {

            e.update();

            if (Rect.intersects(e.getCollision(), player.getCollision())) {

                e.destroy();

                player.lifes -= 1;

                if(player.lifes == 0){

                    isGameOver = true;

                    if (SCORE>= scoremanager.getHighScore()){
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
            enemies.add(new Enemy(getContext(), screenSizeX, screenSizeY));
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


            drawScore();
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    void drawScore() {
        Paint score = new Paint();
        score.setTextSize(30);
        score.setColor(Color.WHITE);
        canvas.drawText("Score : " + SCORE, screenSizeX/20, screenSizeY/18, score);
    }

    void drawLives(){

        int j = screenSizeX/9;
        Paint lives = new Paint();
        lives.setTextSize(30);
        lives.setColor(Color.WHITE);

        canvas.drawText("Lives: ", screenSizeX-(screenSizeX/3), screenSizeY/18, lives);

        for (int i= 0; i < player.lifes; i++) {

            canvas.drawBitmap(player.getBitmapLives(), (screenSizeX-(screenSizeX/3)+j),  screenSizeY/30, paint);
            j += 50;
        }



    }

    void drawGameOver() {
        Paint gameOver = new Paint();
        gameOver.setTextSize(100);
        gameOver.setTextAlign(Paint.Align.CENTER);
        gameOver.setColor(Color.WHITE);
        canvas.drawText("GAME OVER", screenSizeX / 2, screenSizeY / 2, gameOver);
        Paint highScore = new Paint();
        highScore.setTextSize(50);
        highScore.setTextAlign(Paint.Align.CENTER);
        highScore.setColor(Color.WHITE);
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
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {

        isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isGameOver){
                    reset();
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}

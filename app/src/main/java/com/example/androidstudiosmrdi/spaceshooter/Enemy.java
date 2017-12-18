package com.example.androidstudiosmrdi.spaceshooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by Sebastian on 17.11.2017.
 */

public class Enemy {

    private Bitmap bitmap;
    private int x;
    private int y;
    private Rect collision;
    private int screenSizeX;
    private int screenSizeY;
    private int enemies[];
    private int maxX;
    private int maxY;
    private int HP;
    private int speed;
    private boolean isTurnRight;
    private Sound sound;

    private GameView view;

    public Enemy(Context context, int screenSizeX, int screenSizeY, Sound soundenemy) {

        this.screenSizeX = screenSizeX;
        this.screenSizeY = screenSizeY;
        sound = soundenemy;

        HP = 4;

        enemies = new int[]{R.drawable.enemyship, R.drawable.enemyufo, R.drawable.enemyship1};
        Random random = new Random();
        bitmap = BitmapFactory.decodeResource(context.getResources(), enemies[random.nextInt(3)]);
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * 3/5, bitmap.getHeight() * 3/5, false);

        speed = random.nextInt(3) + 1;

        maxX = screenSizeX - bitmap.getWidth();
        maxY = screenSizeY - bitmap.getHeight();

        x = random.nextInt(maxX);
        y = 0 - bitmap.getHeight();

        if (x < maxX){
            isTurnRight = true;
        }else{
            isTurnRight = false;
        }

        collision = new Rect(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
    }

    public void update(){

        y += 7 * speed;

        if (x <=0){
            isTurnRight = true;
        }else if (x >= screenSizeX - bitmap.getWidth()){
            isTurnRight = false;
        }

        if (isTurnRight){
            x += 7 * speed;
        }else{
            x -= 7 * speed;
        }

        collision.left = x;
        collision.top = y;
        collision.right = x + bitmap.getWidth();
        collision.bottom = y + bitmap.getHeight();
    }

    public Rect getCollision() {
        return collision;
    }

    public void hit(){
        if (--HP ==0){
            view.SCORE += 50;
            destroy();
        }
    }

    public void destroy(){

        y = screenSizeY + 1;
        sound.playExplode();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

package com.example.androidstudiosmrdi.spaceshooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;
import java.util.Random;

/**
 * Created by Sebastian on 17.11.2017.
 */

public class Meteor {

    private Bitmap bitmap;
    private int x;
    private int y;
    private int maxX;
    private int minX;
    private int maxY;
    private int minY;

    private int speed;
    private Rect collision;
    private int screenSizeX;
    private int screenSizeY;
    private int HP;

    private int meteors[];

    private GameView view;

    public Meteor(Context context, int screenSizeX, int screenSizeY){

        this.screenSizeX = screenSizeX;
        this.screenSizeY = screenSizeY;


        meteors = new int[]{R.drawable.meteorsmall, R.drawable.meteorbig};
        Random random = new Random();
        int ran = random.nextInt(2);

        bitmap = BitmapFactory.decodeResource(context.getResources(), meteors[ran]);
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * 3/5, bitmap.getHeight() * 3/5, false);

        maxX = screenSizeX - bitmap.getWidth();
        maxY = screenSizeY - bitmap.getHeight();
        minX = 0;
        minY = 0;

        if(ran == 0){
            HP = 1;
        }else {
            HP = 3;
        }

        speed = random.nextInt(3) + 1;

        x = random.nextInt(maxX);
        y = 0 - bitmap.getHeight();

        collision = new Rect(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
    }

    public void update(){
        y += 7 * speed;

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

            if(bitmap.equals(meteors[0])){
                view.SCORE += 10;
            }else{
                view.SCORE += 20;
            }

            destroy();
        }
    }

    public void destroy(){
        y = screenSizeY + 1;
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

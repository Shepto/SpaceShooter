package com.example.androidstudiosmrdi.spaceshooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * Created by Sebastian on 17.11.2017.
 */

public class Player {

    private Bitmap bitmap, bitmapLifes;
    private int x;
    private int y;
    private int speed;
    private int maxX;
    private int minX;
    private int maxY;
    private int minY;
    private boolean isSteerLeft, isSteerRight;
    private float steerSpeed;
    private Rect collision;
    private ArrayList<Laser> lasers;
    private Context context;
    private int screenSizeX, screenSizeY;

    public int lifes = 3;

    public Player(Context context, int screenSizeX, int screenSizeY) {

        this.screenSizeX = screenSizeX;
        this.screenSizeY = screenSizeY;
        this.context = context;

        speed = 1;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * 3/5, bitmap.getHeight() * 3/5, false);

        maxX = screenSizeX - bitmap.getWidth();
        maxY = screenSizeY - bitmap.getHeight();
        minX = 0;
        minY = 0;

        x = screenSizeX/2 - bitmap.getWidth()/2;
        y = screenSizeY - bitmap.getHeight() - 150;

        lasers = new ArrayList<>();

        collision = new Rect(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());

        bitmapLifes = BitmapFactory.decodeResource(context.getResources(), R.drawable.life);
        bitmapLifes = Bitmap.createScaledBitmap(bitmapLifes, bitmapLifes.getWidth() * 3/5, bitmapLifes.getHeight() * 3/5, false);

    }

    public void update(){

        if (isSteerLeft){
            x -= 10 * steerSpeed;
            if (x < minX){
                x = minX;
            }
        }else if (isSteerRight){
            x += 10 * steerSpeed;
            if (x > maxX){
                x = maxX;
            }
        }

        collision.left = x;
        collision.top = y;
        collision.right = x + bitmap.getWidth();
        collision.bottom = y + bitmap.getHeight();

        for (Laser l : lasers) {
            l.update();
        }

        boolean deleting = true;
        while (deleting) {
            if (lasers.size() != 0) {
                if (lasers.get(0).getY() < 0) {
                    lasers.remove(0);
                }
            }

            if (lasers.size() == 0 || lasers.get(0).getY() >= 0) {
                deleting = false;
            }
        }
    }

    public ArrayList<Laser> getLasers() {

        return lasers;
    }

    public void fire(){

        lasers.add(new Laser(context, screenSizeX, screenSizeY, x, y, bitmap, false));
    }

    public Rect getCollision() {

        return collision;
    }

    public void steerRight(float speed){

        isSteerLeft = false;
        isSteerRight = true;
        steerSpeed = Math.abs(speed);
    }

    public void steerLeft(float speed){

        isSteerRight = false;
        isSteerLeft = true;
        steerSpeed = Math.abs(speed);
    }

    public void stay(){

        isSteerLeft = false;
        isSteerRight = false;
        steerSpeed = 0;
    }

    public Bitmap getBitmap() {

        return bitmap;
    }

    // lives
    public Bitmap getBitmapLives() {

        return bitmapLifes;
    }

    public int getX() {

        return x;
    }

    public int getY() {

        return y;
    }

    public int getSpeed() {

        return speed;
    }
}

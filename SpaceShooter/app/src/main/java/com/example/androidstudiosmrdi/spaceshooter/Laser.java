package com.example.androidstudiosmrdi.spaceshooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

/**
 * Created by Sebastian on 17.11.2017.
 */

public class Laser {

    private Bitmap bitmap;
    private int x;
    private int y;
    private Rect collision;
    private int screenSizeX;
    private int screenSizeY;
    private boolean isEnemy;

    public Laser(Context context, int screenSizeX, int screenSizeY, int spaceShipX, int spaceShipY, Bitmap spaceShip, boolean isEnemy){

        this.screenSizeX = screenSizeX;
        this.screenSizeY = screenSizeY;
        this.isEnemy = isEnemy;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.laser);
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * 3/5, bitmap.getHeight() * 3/5, false);

        x = spaceShipX + spaceShip.getWidth()/2 - bitmap.getWidth()/2;

        if (this.isEnemy){
            y = spaceShipY + bitmap.getHeight() + 10;
        }else{
            y = spaceShipY - bitmap.getHeight() - 10;
        }

        collision = new Rect(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
    }

    public void update(){
        if (isEnemy){
            y += bitmap.getHeight() + 10;
            collision.left = x;
            collision.top = y;
            collision.right = x + bitmap.getWidth();
            collision.bottom = y + bitmap.getHeight();
        }else{
            y -= bitmap.getHeight() - 10;
            collision.left = x;
            collision.top = y;
            collision.right = x + bitmap.getWidth();
            collision.bottom = y + bitmap.getHeight();
        }

    }

    public boolean isEnemy() {

        return isEnemy;
    }

    public Rect getCollision() {

        return collision;
    }

    public void destroy(){
        if (isEnemy){
            y = screenSizeY;
        }else{
            y = 0 - bitmap.getHeight();
        }

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

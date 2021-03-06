package com.example.androidstudiosmrdi.spaceshooter;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sebastian on 17.11.2017.
 */

public class ScoreManager {

    private String mName = "SpaceShooter";
    private Context mContext;

    public ScoreManager(Context context) {
        mContext = context;
    }

    public void saveHighScore(int score){
        SharedPreferences sp = mContext.getSharedPreferences(mName, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putInt("high_score", score);
        e.commit();
    }

    public int getHighScore(){
        SharedPreferences sp = mContext.getSharedPreferences(mName, Context.MODE_PRIVATE);
        return sp.getInt("high_score", 0);
    }


}

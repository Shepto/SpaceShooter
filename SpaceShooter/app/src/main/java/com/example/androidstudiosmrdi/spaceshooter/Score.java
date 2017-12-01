package com.example.androidstudiosmrdi.spaceshooter;

/**
 * Created by AndroidStudio smrdi on 1.12.2017.
 */

public class Score {

    //private variables
    int _id;
    String _score;


    // Empty constructor
    public Score(){

    }
    // constructor
    public Score(int id, String score){
        this._id = id;
        this._score = score;

    }

    // constructor
    public Score(String score){
        this._score = score;

    }
    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting name
    public String get_Score(){
        return this._score;
    }

    // setting name
    public void setScore(String score){
        this._score = score;
    }

}

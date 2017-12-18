package com.example.androidstudiosmrdi.spaceshooter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.preference.Preference;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class ScoreView extends AppCompatActivity {

    ListView tv_scores;

    DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_view);


        tv_scores = (ListView) findViewById(R.id.listView);


        // Reading all scores
        Log.d("Reading: ", "Reading all scores..");
        List<Score> scoreDatabase = db.getAllScores();
        List<String> scores = new ArrayList<>();

        int i = 0;
        for (Score cn : scoreDatabase) {
            String log = "Id: " + cn.getID() + " ,Score: " + cn.get_Score();
            // Writing Contacts to log
            Log.d("Name: ", log);

            //vybratie atributu score a kopirovanie ho do noveho arraylistu cisto so score atributom
            scores.add(i, cn.get_Score());
        }

        ArrayAdapter<String> mHistory = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, scores);

        tv_scores.setAdapter(mHistory);

    }
}

package com.mymusic.www.mymusic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import  android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class searchactivity extends AppCompatActivity {

    EditText search;
    ListView search_list;
ImageButton search_button;
    String searchtoken;
    Context context;
    String Songs[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchactivity);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
context=getBaseContext();
        search=(EditText) findViewById(R.id.search);
        search_list=(ListView)findViewById(R.id.search_list);
        search_button=(ImageButton)findViewById(R.id.button1);
        Log.e("search","button set");
        //search_button.setBackgroundColor(getResources().getColor(R.color.button));

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchtoken=search.getText().toString();
                Log.e("search","button clicked");
                new getsearchList().execute(search_list);

            }
        });


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchtoken=search.getText().toString();
                Log.e("search","button clicked");
                new getsearchList().execute(search_list);
            }
        });

        search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                songList.putsongs(Songs);
                Intent intent=new Intent("songstarted");
                intent.putExtra("message","play");
                intent.putExtra("song",position);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

            }
        });
    }


    private  class getsearchList extends AsyncTask<ListView,Void,Void>
    {

        ArrayList<String> song1=new ArrayList<>();
        ArrayList<String> bitmaps=new ArrayList<>();
        String path,s;
ListView lv1;



        protected Void doInBackground(ListView... lv)
        {
            lv1=lv[0];

            int i=0;
            try {
                searchtoken=searchtoken.toLowerCase();
                for (String s1 : songList.songlist) {
                    s=s1;
                s=s.toLowerCase();


                    if (s.contains(searchtoken)) {
                        song1.add(s1);

                        songList.process(s);

                        bitmaps.add(songList.thumbs[i]);
                        i++;
                    }
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return  null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {


                String[] ss = song1.toArray(new String[song1.size()]);
                String[] bb = bitmaps.toArray(new String[bitmaps.size()]);
                Songs=ss;
                lv1.setAdapter(new baseadapter(context, bb, ss));
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }




}



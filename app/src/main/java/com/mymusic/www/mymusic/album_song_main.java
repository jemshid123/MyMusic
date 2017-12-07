package com.mymusic.www.mymusic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class album_song_main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_song_main);
        setTitle(getIntent().getExtras().getString("albumname"));
    }
}

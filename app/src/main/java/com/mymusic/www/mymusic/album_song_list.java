package com.mymusic.www.mymusic;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class album_song_list extends Fragment {


    public album_song_list() {
        // Required empty public constructor
    }
ListView lv;
String albumid,albumbitmap;
    Context context;
    ArrayList<String> songs1;
ImageView image;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
context=getActivity().getBaseContext();
        view=inflater.inflate(R.layout.fragment_album_song_list, container, false);
        lv=(ListView)view.findViewById(R.id.listview);
        image=(ImageView)view.findViewById(R.id.image);

        albumbitmap=getActivity().getIntent().getExtras().getString("albumbitmap");
        Log.e("albumbitmap",albumbitmap);
        if(!albumbitmap.equals("non"))
        {

            Picasso.with(getContext()).load(new File(albumbitmap)).into(image);
        }
        new getMusicList().execute(lv);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                songList.putsongs(songs1.toArray(new String[songs1.size()]));
                Intent intent=new Intent("songstarted");
                intent.putExtra("message","play");
                intent.putExtra("song",position);
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
            }
        });
    }


    private  class getMusicList extends AsyncTask<ListView,Integer,Integer>
    {
        ArrayList<String> songs;
String albumid;
        ListView lv1;
        String[] bitmap;
        int len;
        String path;
        protected Integer doInBackground(ListView... lv)
        {
            len=0;
            lv1=lv[0];
    albumid=getActivity().getIntent().getExtras().getString("albumid");
    songs=getsong(albumid);
            bitmap=new String[songs.size()];

for(String s:songs)
{
    try {
        songList.process(s);
        path = getActivity().getFilesDir() + "/" + songList.getID();
        if (new File(path).exists()) {
            bitmap[len++] = path;
        } else {
            bitmap[len++] =null;
            }
        publishProgress(len);



    }catch (Exception e)
    {
        e.printStackTrace();
    }
}


            return 1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            lv1.setAdapter(new baseadapter(context,bitmap,songs.toArray(new String[songs.size()])));
            songs1=songs;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

lv1.setAdapter(new baseadapter(context,bitmap,songs.toArray(new String[songs.size()])));
            songs1=songs;
        }


    }

    protected  ArrayList getsong(String albumId)
    {
        ArrayList<String> songs = new ArrayList<>();
        String selection = "is_music != 0";

        if (albumId != null) {
            selection = selection + " and album_id = " + albumId;
        }

        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM_ID
    };
    final String sortOrder = MediaStore.Audio.AudioColumns.TITLE + " COLLATE LOCALIZED ASC";

    Cursor cursor = null;
    try {
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        cursor = getActivity().getContentResolver().query(uri, projection, selection, null, sortOrder);
        while(cursor.moveToNext()) {
            songs.add(cursor.getString(0) + "|@@|"
                    + cursor.getString(1) + "|@@|"
                    + cursor.getString(2) + "|@@|"
                    + cursor.getString(3) + "|@@|"
                    + cursor.getString(4) + "|@@|"
                    + cursor.getString(5)+"|@@|"
            );
        }

    } catch (Exception e) {
        Log.e("Media", e.toString());
    } finally {
        if (cursor != null) {
            cursor.close();
        }
    }
        return  songs;
    }

}

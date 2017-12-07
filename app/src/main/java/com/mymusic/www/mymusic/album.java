package com.mymusic.www.mymusic;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class album extends Fragment {


    public album() {
        // Required empty public constructor
    }

    ListView albumlist;
ArrayList<String> albums1;
    String bitmap1[];
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_album, container, false);
        albumlist=(ListView)view.findViewById(R.id.albumlist);



        new getMusicList().execute(albumlist);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        albumlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("album","click 2");
                try {
                    Intent intent = new Intent(getContext(), album_song_main.class);
                    songList.process(albums1.get(position));
                    intent.putExtra("albumid", songList.getPath());
                    intent.putExtra("albumname",songList.getTitle());
                    if(bitmap1[position]!=null) {
                        intent.putExtra("albumbitmap", bitmap1[position]);
                    }
                    else
                    {
                        intent.putExtra("albumbitmap","non");
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }


    private  class getMusicList extends AsyncTask<ListView,Integer,Integer>
    {

        ListView albumlist;
        ArrayList<String> albums;
        String bitmap[];
        int i;
        protected Integer doInBackground(ListView... lv)
        {
            i=0;
            albumlist=lv[0];
            albums=getMusic(getActivity().getBaseContext());
             bitmap=new String[albums.size()];
            for(String  s:albums) {
                try {
                    songList.process(s);
                    Cursor cursor = getActivity().managedQuery(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                            new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                            MediaStore.Audio.Albums._ID + "=?",
                            new String[]{String.valueOf(songList.getPath())},
                            null);
                    Log.e("id", songList.getPath());
                    if (cursor.moveToFirst()) {
                        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                        // do whatever you need to do
                        bitmap[i++] = path;
                        Log.e("path", path);
                    }
                    publishProgress(i);
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
            albumlist.setAdapter(new baseadapter(getActivity().getBaseContext(),bitmap,albums.toArray(new String[albums.size()])));
albums1=albums;
            bitmap1=bitmap;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            albumlist.setAdapter(new baseadapter(getActivity().getBaseContext(),bitmap,albums.toArray(new String[albums.size()])));
            albums1=albums;
            bitmap1=bitmap;
        }


    }

    /** function to return list of songs */
    public ArrayList getMusic(Context context){
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
String prev="-1";
        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID,



        };

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                MediaStore.Audio.Media.TITLE);

        ArrayList<String> songs = new ArrayList<String>();
        while(cursor.moveToNext()) {
            if(!prev.equals(cursor.getString(1))) {
                songs.add("  " + "|@@|" + "  " + "|@@|" +
                        cursor.getString(0) + "|@@|"
                        + cursor.getString(1) + "|@@|"
                        + "  " + "|@@|" + "1" + "|@@|"
                );
                prev=cursor.getString(1);
            }
        }

        return songs;
    }

}

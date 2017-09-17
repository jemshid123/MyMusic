package com.mymusic.www.mymusic;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;


public class song extends Fragment {

    ListView songListView;
    ArrayList<String> arrayList;
    Bitmap[] thumb;
    AlertDialog.Builder alertBuilder ;
    AlertDialog alert;
Context context;
    public song() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
context=getContext();
        alertBuilder =new AlertDialog.Builder(context);
        alertBuilder.setCancelable(false);
        alertBuilder.setTitle("Please Wait");
        alertBuilder.setMessage("Loading Music");
        alertBuilder.setIcon(R.mipmap.ic_launcher);
        alert=alertBuilder.create();
        alert.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_song, container, false);
        songListView=(ListView)view.findViewById(R.id.songListView);
        new getMusicList().execute(songListView);
        return view ;
    }

    public void onStart()
    {
        super.onStart();



    }


    private  class getMusicList extends AsyncTask<ListView,Void,Integer>
    {


        protected Integer doInBackground(ListView... lv)
        {
            int i=0;
            arrayList=getMusic(context);
             thumb=new Bitmap[arrayList.size()];
            String[] songDetails;
           //getting bitmaps for songs thumb
            for (String song:arrayList) {
                songDetails=song.split(Pattern.quote("||"));
                if(new File(songDetails[3]).exists()) {
                    thumb[i++] = coverpicture(songDetails[3]);

                }
            }
            Log.e(" song ", arrayList.get(1));
            return arrayList.size();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            String[] details=arrayList.toArray(new String[arrayList.size()]);
            songListView.setAdapter(new baseadapter(context,thumb,details));
            alert.cancel();
        }



    }
    /** function to return list of songs */
    public ArrayList getMusic(Context context){
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION
        };

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null);

        ArrayList<String> songs = new ArrayList<String>();
        while(cursor.moveToNext()) {
            songs.add(cursor.getString(0) + "||"
                    + cursor.getString(1) + "||"
                    + cursor.getString(2) + "||"
                    + cursor.getString(3) + "||"
                    + cursor.getString(4) + "||"
                    + cursor.getString(5));
        }
        return songs;
    }
/** cover picture for music files */

    public  Bitmap coverpicture(String path) {

            MediaMetadataRetriever mr = new MediaMetadataRetriever();

            mr.setDataSource(path);

            byte[] byte1 = mr.getEmbeddedPicture();
            mr.release();
        if(byte1 != null)
                return BitmapFactory.decodeByteArray(byte1, 0, byte1.length);
        else
            return  null;

    }
}

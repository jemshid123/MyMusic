package com.mymusic.www.mymusic;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;


public class song extends Fragment {

    ListView songListView;
    ArrayList<String> arrayList;
    String[] thumb;
    ProgressDialog progressbuilder ;
    int prevSongIndex;

Context context;
    public song() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
context=getContext();

        //reciver

        progressbuilder =new ProgressDialog(context);
        progressbuilder.setCancelable(false);
        progressbuilder.setTitle("Please Wait");
        progressbuilder.setMessage("Loading Music");
        progressbuilder.setIcon(R.mipmap.ic_launcher);
        progressbuilder.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
     // progressbuilder.show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_song, container, false);
        songListView=(ListView)view.findViewById(R.id.songListView);
        new getMusicList().execute(songListView);
        Intent startmusicintent=new Intent(getActivity(),playmusic.class);
        getActivity().startService(startmusicintent);

        return view ;
    }

    public void onStart()
    {
        super.onStart();
songListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      try {

songList.putsongs(songList.getsongcomplete());
Intent  intent=new Intent("songstarted");
          intent.putExtra("message","play");
          intent.putExtra("song",position);
          sendMessage(intent);
           songList.process(songList.getsongs(position));
          Toast.makeText(getContext(),songList.getTitle(),Toast.LENGTH_LONG).show();



      }catch (Exception e)
      {
          e.printStackTrace();
      }

    }
});


    }


    private  class getMusicList extends AsyncTask<ListView,Integer,Integer>
    {


        protected Integer doInBackground(ListView... lv)
        {
            int i=0;
            String path;
            Bitmap tempbmp;
            arrayList=getMusic(context);
             thumb=new String[arrayList.size()];

           //getting bitmaps for songs thumb
            for (String song:arrayList) {
                try {
                    songList.process(song);
                    path = getActivity().getFilesDir() + "/" + songList.getID();
                    if (getpicture(path)) {
                        thumb[i++] = path;
                    } else {
                        tempbmp = coverpicture(songList.getPath());
                        if (tempbmp == null) {
                            thumb[i++] = null;
                        } else {
                            savepicture(tempbmp, path);
                            thumb[i++] = path;
                        }


                    }
                    publishProgress(thumb.length);
                }catch (Exception e)
                {
                    e.printStackTrace();
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
            songList.putsongs(details);
            songList.putsongscomplete(details);
            songList.putthumbs(thumb);
        //  progressbuilder.dismiss();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            String[] details=arrayList.toArray(new String[arrayList.size()]);
            songListView.setAdapter(new baseadapter(context,thumb,details));
           // progressbuilder.incrementProgressBy(1);

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
                MediaStore.Audio.Media.DURATION,

        };

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                MediaStore.Audio.Media.TITLE);

        ArrayList<String> songs = new ArrayList<String>();
        while(cursor.moveToNext()) {
            songs.add(cursor.getString(0) + "|@@|"
                    + cursor.getString(1) + "|@@|"
                    + cursor.getString(2) + "|@@|"
                    + cursor.getString(3) + "|@@|"
                    + cursor.getString(4) + "|@@|"
                    + cursor.getString(5)+"|@@|"
                    );
        }
        progressbuilder.setMax(songs.size());
        return songs;
    }
/** cover picture for music files */

    public  Bitmap coverpicture(String path) {

            MediaMetadataRetriever mr = new MediaMetadataRetriever();
        try{

            mr.setDataSource(path);

            byte[] byte1 = mr.getEmbeddedPicture();
            mr.release();
        if(byte1 != null)
                return BitmapFactory.decodeByteArray(byte1, 0, byte1.length);
        else
            return  null;

    }catch(Exception e)
    {
        e.printStackTrace();
        return null;
    }}

/**Local broadcast sender for player*/



    /**Local broadcast sender for song*/
    private void sendMessage(Intent intent) {

        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

   /** saving cover picture as png */
    public void savepicture(Bitmap bmp,String filename)
    {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filename);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

/** retrive cover picture */

public boolean getpicture(String filename) {
    if (new File(filename).exists())
        return true;
    return false;
}

}

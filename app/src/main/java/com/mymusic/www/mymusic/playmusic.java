package com.mymusic.www.mymusic;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;

public class playmusic extends Service {
    MediaPlayer mp;
    int position;
    String details;
    Handler handler;
    @Override
    public void onDestroy() {
        super.onDestroy();
        mp.stop();
        LocalBroadcastManager.getInstance(getBaseContext()).unregisterReceiver(mMessageReceiver);

    }
    public playmusic() {
        LocalBroadcastManager.getInstance(getBaseContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("songstarted"));
        mp = new MediaPlayer();
        handler=new Handler();
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

return START_STICKY;



    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       return null;
    }



    public  void playsong(int position)
    {
       final int p=position+1;
        try {

             stopsong();
            this.position=position;
            Log.e("play_music position",position+" ");
            Log.e("playmusic_number "," playing");
            details= songList.getsongs(position);
            songList.process(details);
            mp=new MediaPlayer();
            mp.reset();



                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        Log.e("play_music","song completed"+p);
                        Intent  intent=new Intent("songstarted");

                        if(p<songList.songs.length) {
                            intent.putExtra("message", "play");
                            intent.putExtra("song", p);
                            LocalBroadcastManager.getInstance(playmusic.this).sendBroadcast(intent);
                        }
                        else{
                            intent.putExtra("message", "pause_button");
                            intent.putExtra("song", p);
                            LocalBroadcastManager.getInstance(playmusic.this).sendBroadcast(intent);
                        }

                    }
                });

String fp=songList.getPath();
            File file=new File(songList.getPath());
            FileInputStream is=new FileInputStream(file);



            mp.setDataSource(is.getFD());

            mp.prepare();
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });


handler.postDelayed(seekbarprogress,100);

        }catch (Exception e)
        {




            Log.e("exc",songList.getPath());
            e.printStackTrace();
        }
    }

    public void stopsong()
    {
        Log.e("playsong "," stopping");
        try {
            if (mp.isPlaying()) {
                mp.stop();
                mp.release();

            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void seekPosition(int progress)
    {


            mp.seekTo(progress);
//Log.e("play_music","seek position changed successfully to "+progress);


    }

    /** seek bar functionality */

    private Runnable seekbarprogress=new Runnable() {
        @Override
        public void run() {

                if (mp.isPlaying()) {

                    Intent intent = new Intent("songstarted");
                    intent.putExtra("message", "seekbar_changed");
                    intent.putExtra("progress", mp.getCurrentPosition());
                    //  Log.e("play_music","current_seek_position send:"+mp.getCurrentPosition());
                    LocalBroadcastManager.getInstance(playmusic.this).sendBroadcast(intent);
                    handler.postDelayed(this, 250);
                }

        }
    };

    /** local brodcast reciver */
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
          //  Log.e("play_music", "Got message: " + message);

            if(message.equals("play"))
            {
                int position=intent.getExtras().getInt("song");
Log.e("play pos",songList.getsongs(position));
             playsong(position);

            }
            else if(message.equals("seekbar_changed_manually"))
            {
                int position=intent.getExtras().getInt("progress");
             seekPosition(position);
               // Log.e("play_music", "Got message: " + message+":"+position);
            }
            else if(message.equals("resume"))
            {
                if(!mp.isPlaying()) {
                    mp.start();
                    handler.postDelayed(seekbarprogress,100);
                }
            }
            else if(message.equals("pause"))
            {
                if(mp.isPlaying())
                mp.pause();
            }

        }
    };
}

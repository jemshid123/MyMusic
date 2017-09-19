package com.mymusic.www.mymusic;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class playmusic extends Service {
    MediaPlayer mp;
    int position;
    private IBinder mBinder ;
    String details;
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
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {


            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopSelf();
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
return START_STICKY;



    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       return mBinder;
    }
    public class MyBinder extends Binder {
        playmusic getService() {
            return playmusic.this;
        }
    }
    private void sendMessage(String message) {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("songstarted");
        // You can also include some extra data.
        intent.putExtra("message", message);
        intent.putExtra("song", position);
        LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);
    }

    public  void playsong(int position)
    {
        try {

             stopsong();
            Log.e("playsong position",position+" ");
            Log.e("playsong "," playing");
            details= songList.getsongs(position);
            songList.process(details);
            mp=new MediaPlayer();


            mp.setDataSource(songList.getPath());

            mp.prepare();
            mp.start();


        }catch (Exception e)
        {
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

    /** local brodcast reciver */
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.e("receiver", "Got message: " + message);
            if(message.equals("play"))
            {
                int position=intent.getExtras().getInt("song");

             playsong(position);

            }
        }
    };
}

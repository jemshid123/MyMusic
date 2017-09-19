package com.mymusic.www.mymusic;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link player.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link player newInstance} factory method to
 * create an instance of this fragment.
 */
public class player extends Fragment {
ImageButton previous,play,next;
    TextView songname;
boolean musicplaying;
    playmusic playmusicinstance;
    private ServiceConnection mServiceConnection;
    public player() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
musicplaying=false;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_player, container, false);
        TranslateAnimation animation = new TranslateAnimation(900.0f, -400.0f,  0.0f, 0.0f); // new TranslateAnimation (float fromXDelta,float toXDelta, float fromYDelta, float toYDelta);

        animation.setDuration(13000); // animation duration
        animation.setRepeatCount(1000000); // animation repeat count
        animation.setFillAfter(true);
        view.findViewById(R.id.songname).startAnimation(animation);
        songname=(TextView) view.findViewById(R.id.songname);
        play=(ImageButton)view.findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!musicplaying) {
                   playbuton();
                }
                else
                {
                   pausebuton();
                }
            }
        });

        //reciver
        mServiceConnection= new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                playmusic.MyBinder myBinder = (playmusic.MyBinder) service;
                playmusicinstance =myBinder.getService();
                Log.e("service","successfully binded");
            }
        };
        // local brodcast reciver
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("songstarted"));
        return view;
    }


    @Override
    public void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMessageReceiver);
        getActivity().unbindService(mServiceConnection);
        super.onDestroy();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }




public  void binder()
{
    Intent startmusicintent=new Intent(getActivity(),playmusic.class);
    boolean i=getActivity().bindService(startmusicintent,mServiceConnection,getContext().BIND_AUTO_CREATE);
    Log.e("service",i+" ");
}

    /** local brodcast reciver */
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.e("receiver", "Got message: " + message);
            if(message.equals("bind"))
            binder();
            else if(message.equals("pause"))
            {
                pausebuton();
            }
            else if(message.equals("play"))
            {
                playbuton();
               int position=intent.getExtras().getInt("song");
                Log.e("number2",position+" ");
songList.process(songList.getsongs(position));
                songname.setText(songList.getTitle());

            }
        }
    };

    //change play button state

    public void playbuton()
    {
        musicplaying=true;
        play.setImageResource(R.drawable.play);
    }
    public void pausebuton()
    {
        musicplaying=false;
        play.setImageResource(R.drawable.pause);
    }
}

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
import android.widget.SeekBar;
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
    SeekBar seekBar;
    TextView songname,total_time,current_time;
    int progress_prev,current_track;
boolean musicplaying;

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
        seekBar=(SeekBar) view.findViewById(R.id.seekbar);
        songname=(TextView) view.findViewById(R.id.songname);
        total_time=(TextView)  view.findViewById(R.id.total_time);
        current_time=(TextView)  view.findViewById(R.id.current_time);
        play=(ImageButton)view.findViewById(R.id.play);
        previous=(ImageButton)view.findViewById(R.id.previous);
        next=(ImageButton)view.findViewById(R.id.next);
        /** next and previous */
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent=new Intent("songstarted");
                intent.putExtra("message","play");
                intent.putExtra("song",++current_track);
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent=new Intent("songstarted");
                intent.putExtra("message","play");
                intent.putExtra("song",--current_track);
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
            }
        });
        /** play button */
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


        /** seek bar position changed */
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
int max=progress_prev+500;
                int min=progress_prev-500;
                Intent intent=new Intent("songstarted");
                if((progress<min)||(progress>max)) {
                    intent.putExtra("message", "seekbar_changed_manually");
                    intent.putExtra("progress", progress);

                    LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        // local brodcast reciver
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("songstarted"));
        return view;
    }


    @Override
    public void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMessageReceiver);

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





    /** local brodcast reciver */
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            int i;
            String message = intent.getStringExtra("message");
          //  Log.e("player", "Got message: " + message);
           if(message.equals("pause_button"))
            {
                pausebuton();
            }
            else if(message.equals("play"))
            {
                playbuton();
               int position=intent.getExtras().getInt("song");
               current_track= position;
                Log.e("play_number",position+" ");
                songList.process(songList.getsongs(position));
                songname.setText(songList.getTitle());
                total_time.setText(songList.getDuration());
                seekBar.setMax(songList.getrawduration());

            } else if(message.equals("seekbar_changed"))
           {

               i=intent.getExtras().getInt("progress");
              Log.e("player","current_seek_position recived:"+i);
               progress_prev=i;
               seekBar.setProgress(i);
               current_time.setText(songList.formatduration(i));
           }
        }
    };

    //change play button state

    public void playbuton()
    {
        musicplaying=true;
        play.setImageResource(R.drawable.play);
        Intent  intent=new Intent("songstarted");
        intent.putExtra("message","resume");
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

    }
    public void pausebuton()
    {
        musicplaying=false;
        play.setImageResource(R.drawable.pause);
        Intent  intent=new Intent("songstarted");
        intent.putExtra("message","pause");
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

    }
}

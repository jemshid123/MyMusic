package com.mymusic.www.mymusic;


import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class audio_recog extends AppCompatActivity {

    TextView mTextField;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recog);
        mTextField=(TextView)findViewById(R.id.text) ;

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        final ProgressBar spinner;

checkPermissionsAndStart();
        final MediaRecorder myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setAudioSamplingRate(16000);
        try {
            Toast.makeText(getBaseContext(),getFilesDir() + "/mymusic/sample.mp3",Toast.LENGTH_LONG).show();
            new File(getFilesDir() + "/mymusic/").mkdirs();
            myAudioRecorder.setOutputFile(getFilesDir() + "/mymusic/sample.mp3");
            myAudioRecorder.prepare();
            myAudioRecorder.start();

        }catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                mTextField.setText("     Recording : " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                mTextField.setText("            Done!");
                myAudioRecorder.stop();
                myAudioRecorder.release();

             MediaPlayer mp=new MediaPlayer();
                try {
                    mp.setDataSource(getBaseContext(), Uri.parse(getFilesDir() + "/mymusic/sample.mp3"));
                    mp.prepare();
                    mp.start();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

        }.start();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissionsAndStart();
                } else {
                    finish();
                }
        }

    }

    private void checkPermissionsAndStart() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO},
                    1);
        } else {

        }
    }






}

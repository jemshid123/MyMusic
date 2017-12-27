package com.mymusic.www.mymusic;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;


import com.android.volley.toolbox.Volley;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.kbeanie.multipicker.api.AudioPicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.AudioPickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenAudio;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import devlight.io.library.ntb.NavigationTabBar;

public class musiclist extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,GoogleApiClient.OnConnectionFailedListener{
    private  static GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    AudioPicker audioPicker;
    RequestQueue queue ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musiclist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
          setTitle("     My Music");
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        queue = Volley.newRequestQueue(getBaseContext());

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        /**tab setup */
        audioPicker = new AudioPicker(musiclist.this);

        /**  accessing users profile from facebook or google. */

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

                 if(MainActivity.isLoggedInfb())
                 {
                     try {

                         BufferedReader br = new BufferedReader(new FileReader(getFilesDir()+"/fb.json"));
                         Toast.makeText(getBaseContext(),br.readLine(),Toast.LENGTH_LONG).show();
                     }
                     catch (Exception e)
                     {
                         e.printStackTrace();
                     }
                 }else {

                     GoogleSignInAccount acct=MainActivity.Result.getSignInAccount();
                     Toast.makeText(getBaseContext(),acct.getDisplayName(),Toast.LENGTH_LONG).show();

                 }






        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.musiclist, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            startActivity(new Intent(this,searchactivity.class));
             return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.trending) {
            // Handle the camera action
        } else if (id == R.id.recommended) {

        } else if (id == R.id.share) {



                  //  Toast.makeText(getBaseContext(),"token="+token+"&uploadid="+uploadid+"&date="+date,Toast.LENGTH_LONG).show();
                //private or public
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
            alertDialog.setIcon(R.mipmap.ic_launcher);
            alertDialog.setTitle("Mode");
            alertDialog.setMessage("select upload mode");
            alertDialog.setPositiveButton("Private", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        int i = 1;
                        Random rand=new Random();
                        String uri = "http://jemshid.pythonanywhere.com/privateuploadmethod?";
                        String token, uploadid, date;
                        token = songList.getToken(getBaseContext());
                        uploadid = rand.nextInt(1000000000) + "";
                        date = Calendar.getInstance().getTime().toString();
                        uri = uri + "token=" + token + "&uploadid=" + uploadid + "&date=" + date;
                        uri = uri + "&mode=private";
                        uri = uri + "&path=" + uploadid + "_" + i;
                        i++;
                        Log.e("uri", uri);
                        //Intent intent=new Intent(musiclist.this,uploadwebview.class);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

                        intent.putExtra("uri", uri);
                        startActivity(intent);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        finish();
                    }

                }
            });
            alertDialog.setNegativeButton("Public", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
try {
    int i = 1;
    Random rand=new Random();
    String uri = "http://jemshid.pythonanywhere.com/publicuploadmethod?";
    String token, uploadid, date;
    token = songList.getToken(getBaseContext());
    uploadid = rand.nextInt(1000000000) + "";
    date = Calendar.getInstance().getTime().toString();
    uri = uri + "token=" + token + "&uploadid=" + uploadid + "&date=" + date;
    uri = uri + "&mode=public";
    uri = uri + "&path=" + uploadid + "_" + i;
    i++;
    Log.e("uri", uri);
    //Intent intent=new Intent(musiclist.this,uploadwebview.class);
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

    intent.putExtra("uri", uri);
    startActivity(intent);
}catch (Exception e)
{
    e.printStackTrace();
    finish();
}
                }
            });
            alertDialog.setCancelable(true);

   AlertDialog alert=alertDialog.create();
            alert.show();
        } else if (id == R.id.collection) {
            startActivity(new Intent(musiclist.this,collection.class));

        } else if (id == R.id.settings) {

        } else if (id == R.id.logout) {
            if(MainActivity.isLoggedInfb())
            {

                LoginManager.getInstance().logOut();
            }
            else
            {
               revokeAccess();
            }
            startActivity(new Intent(musiclist.this,MainActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.

    }


    /** handle google signin result */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

        }
        if (requestCode == Picker.PICK_AUDIO && resultCode == RESULT_OK) {
            audioPicker.submit(data);
        }

    }


    public  void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        File file = new File(getFilesDir() + "/registered.txt");
                       file.delete();
                        Log.e("logout",status.toString());
                        finish();
                    }
                });
    }



}

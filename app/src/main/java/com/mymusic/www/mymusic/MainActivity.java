package com.mymusic.www.mymusic;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import java.util.jar.Manifest;
import java.util.regex.Pattern;



public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {
AlertDialog.Builder build;
    private static final String TAG = "SignInActivity";
    Button google,fb;
     private  static GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    ImageView pic;
  public  static   GoogleSignInResult Result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        build=new AlertDialog.Builder(MainActivity.this);
        google = (Button) findViewById(R.id.google);
        fb = (Button) findViewById(R.id.fb);
        pic=(ImageView) findViewById(R.id.imageView);
        /** google signin */
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

signIn();
        /** alert box initializing */
        build.setTitle("Couldn't connect");
        build.setMessage("No Internet connection");
        build.setIcon(R.mipmap.ic_launcher);
        build.setCancelable(false);
        build.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
       final AlertDialog alert=build.create();
        /** google signup */
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    if(isNetworkAvailable(getBaseContext()))
                    {
signIn();
                    }else
                    {

                        alert.show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                if(isNetworkAvailable(getBaseContext()))
                {

                }else
                {

                    alert.show();
                }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
public Boolean permission(){
        final String[] per={android.Manifest.permission.GET_ACCOUNTS, android.Manifest.permission.GET_ACCOUNTS_PRIVILEGED, android.Manifest.permission.INTERNET, android.Manifest.permission.ACCESS_NETWORK_STATE};
        ActivityCompat.requestPermissions(MainActivity.this,per,1);
        if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.GET_ACCOUNTS)== PackageManager.PERMISSION_GRANTED) {
return true;
        }

return false;
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onClick(View v) {

    }
    /** handle google signin result */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            Result=result;
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
           String name=acct.getDisplayName();
            String id=acct.getId();
            String email=acct.getEmail();
            //Picasso.with(getBaseContext()).load(acct.getPhotoUrl()).into(pic);
            Toast.makeText(getBaseContext(),name+" "+id+" "+email,Toast.LENGTH_LONG).show();
            Toast.makeText(getBaseContext(),acct.getPhotoUrl()+" ",Toast.LENGTH_LONG).show();
        } else {
            // Signed out, show unauthenticated UI.

        }
    }

}

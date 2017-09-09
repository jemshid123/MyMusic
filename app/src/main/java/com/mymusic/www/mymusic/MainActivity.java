package com.mymusic.www.mymusic;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Arrays;
import java.util.jar.Manifest;
import java.util.regex.Pattern;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {
AlertDialog.Builder build;
    private static final String TAG = "SignInActivity";
    Button google,fb;
     private  static GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;

    ImageView pic;
    CallbackManager callbackManager;

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
        Result=null;
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

        //check if google is used to already login
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);

        if (opr.isDone()) {
            // signed in.
               signIn();

            handleSignInResult(opr.get());


        }else if(isLoggedInfb())
        {

            File file=new File(getFilesDir()+"/fb.json");
            if(file.exists())
            {
                startActivity(new Intent(MainActivity.this,musiclist.class));
                finish();
            }

        }
        /** facebook login */

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager= CallbackManager.Factory.create();


        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code

                 handlefbsignin(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
       //check if facebook is used to already login




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
                    if(true)
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
                    Toast.makeText(getBaseContext(),"fb",Toast.LENGTH_LONG).show();
                if(isNetworkAvailable(getBaseContext()))
                {

                    LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile", "email"));


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
        else
        {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            Result=result;
            Intent intent=new Intent(MainActivity.this,musiclist.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            getBaseContext().startActivity(intent);
            MainActivity.this.finish();
        } else {
            // Signed out, show unauthenticated UI.

        }
    }

    public static void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                      Log.e("logout",status.toString());
                    }
                });
    }
/** facebook event handle */
public static boolean isLoggedInfb() {
    AccessToken accessToken = AccessToken.getCurrentAccessToken();
    return accessToken != null;
}
    public void handlefbsignin(AccessToken token)
    {
        GraphRequest.newMeRequest(
                token, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject me, GraphResponse response) {
                        if (response.getError() != null) {
                            // handle error
                        } else {
                            try {


                                String id = response.getJSONObject().get("id").toString();
                                String name=response.getJSONObject().get("name").toString();

                                BufferedWriter bw=new BufferedWriter(new FileWriter(getFilesDir()+"/fb.json"));
                                bw.write(response.getRawResponse());
                                bw.close();
                                Bitmap bit=getFacebookProfilePicture(id);

                                saveImage(bit);
                                Intent intent=new Intent(MainActivity.this,musiclist.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                 Result=null;
                                getBaseContext().startActivity(intent);
                                MainActivity.this.finish();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            // send email and id to your web server
                            Log.e("Result1", response.getRawResponse());
                            Log.e("Result", me.toString());

                        }
                    }
                }).executeAsync();
    }
    public  Bitmap getFacebookProfilePicture(String userID)
    {
        try {
            String imageURL;

            Bitmap bitmap = null;
            imageURL = "https://graph.facebook.com/" + userID + "/picture?type=large";


            return bitmap;
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public boolean  saveImage(Bitmap image)
    {
        try {
            // Use the compress method on the Bitmap object to write image to
            // the OutputStream
            FileOutputStream fos = openFileOutput(getBaseContext().getFilesDir()+"/fb.png", Context.MODE_PRIVATE);

            // Writing the bitmap to the output stream
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

            return true;
        } catch (Exception e) {
            Log.e("saveToInternalStorage()", e.getMessage());
            return false;
        }
    }

}

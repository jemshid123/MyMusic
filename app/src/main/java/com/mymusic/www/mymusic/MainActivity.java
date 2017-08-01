package com.mymusic.www.mymusic;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.jar.Manifest;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {
AlertDialog.Builder build;

    Button google,fb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        build=new AlertDialog.Builder(MainActivity.this);
        google = (Button) findViewById(R.id.google);
        fb = (Button) findViewById(R.id.fb);
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
                    if (permission()) {
                        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
                        Account[] accounts = AccountManager.get(getBaseContext()).getAccounts();
                        for (Account account : accounts) {
                            if (emailPattern.matcher(account.name).matches()) {
                                String primaryEmailID = account.name;
                                Toast.makeText(getBaseContext(), primaryEmailID, Toast.LENGTH_LONG).show();

                            }
                        }
                    }
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
                if (permission()) {

                        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
                        Account[] accounts = AccountManager.get(getBaseContext()).getAccounts();
                        for (Account account : accounts) {
                            if (emailPattern.matcher(account.name).matches()) {
                                String primaryEmailID = account.name;
                                Toast.makeText(getBaseContext(), primaryEmailID, Toast.LENGTH_LONG).show();

                            }
                        }

                }
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

}

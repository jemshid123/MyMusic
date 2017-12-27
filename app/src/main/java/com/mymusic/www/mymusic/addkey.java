package com.mymusic.www.mymusic;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


public class addkey extends AppCompatActivity {
LinearLayout ll1,ll2;
    Button validate,attach;
    EditText keyval;
    WebView web;
    String uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addkey);
        ll1=(LinearLayout)findViewById(R.id.ll1);
        ll2=(LinearLayout)findViewById(R.id.ll2);
        validate=(Button)findViewById(R.id.Validate);
        attach=(Button)findViewById(R.id.Attach);
        keyval=(EditText) findViewById(R.id.keyvalue);
        web=(WebView) findViewById(R.id.web);
web.getSettings().setJavaScriptEnabled(true);


       // ll2.setVisibility(View.INVISIBLE);
validate.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        uri="http://jemshid.pythonanywhere.com/attachmethod?uploadid="+keyval.getText().toString();
        Toast.makeText(getBaseContext(),uri,Toast.LENGTH_LONG).show();
        web.loadUrl(uri);


    }
});


        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str=keyval.getText().toString();
                if(str.length()>3) {
                    pyServer.saveKey(str, getBaseContext());
                }
                Toast.makeText(getBaseContext(),pyServer.getKeys(getBaseContext()),Toast.LENGTH_LONG).show();
            }
        });

    }
}

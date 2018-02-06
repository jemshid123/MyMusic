package com.mymusic.www.mymusic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class settings extends AppCompatActivity {
ArrayAdapter<String> arrayAdapter;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

       final Intent intent=new Intent(settings.this,generalwebview.class);

        String[] options=new String[]{"Private Uploads","Public Uploads","Attached keys"};
        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,options);
        lv=(ListView)findViewById(R.id.lv);
        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
switch(position)
        {
            case 0:

                try {
                    intent.putExtra("url", "http://jemshid.pythonanywhere.com/get_my_prisong?token=" + songList.getToken(getBaseContext()));
                startActivity(intent);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            case 1:
            try {
                intent.putExtra("url", "http://jemshid.pythonanywhere.com/get_my_pubsong?token=" + songList.getToken(getBaseContext()));
                startActivity(intent);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            break;
        }
            }
        });

    }
}

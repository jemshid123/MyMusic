package com.mymusic.www.mymusic;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

public class server_search extends AppCompatActivity {

    EditText search;
    ListView search_list;
    ImageButton search_button;
    String searchtoken;
    Context context;
    String Songs[],thumbs[];
    ArrayList<String> songs1,thumbs1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_search);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        context=getBaseContext();
        search=(EditText) findViewById(R.id.search);
        search_list=(ListView)findViewById(R.id.search_list);
        search_button=(ImageButton)findViewById(R.id.button1);
        Log.e("search","button set");
        songs1=new ArrayList<String>();
        thumbs1=new ArrayList<String>();
        //search_button.setBackgroundColor(getResources().getColor(R.color.button));

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchtoken=search.getText().toString();
                Log.e("server_search","button clicked");
                RequestQueue queue= Volley.newRequestQueue(getBaseContext());
                String url = "http://jemshid.pythonanywhere.com/search?keyword="+searchtoken;
                JsonObjectRequest request= new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    songs1.clear();
                                    thumbs1.clear();
                                    int size = response.getInt("size");

                                    for(int i=0;i<size;i++)
                                    {

                                        songs1.add(response.getString("song"+i));
                                        songList.process(songs1.get(i));
                                        thumbs1.add(songList.getID()+".jpg");

                                    }
                                    Songs=songs1.toArray(new String[songs1.size()]);
                                    thumbs=thumbs1.toArray(new String[thumbs1.size()]);
                                    search_list.setAdapter(new serverbaseadapter(getBaseContext(),thumbs,Songs,"pub"));


                                }catch (Exception e)
                                {
                                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                queue.add(request);

                search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        songList.putsongs(Songs);
                        Intent intent=new Intent("songstarted");
                        intent.putExtra("message","play");
                        intent.putExtra("song",position);
                        LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);
                        songList.process(songList.getsongs(position));
                        Toast.makeText(getBaseContext(),songList.getTitle(),Toast.LENGTH_LONG).show();

                    }
                });

            }
        });
    }
}

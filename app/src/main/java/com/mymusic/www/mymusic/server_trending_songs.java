package com.mymusic.www.mymusic;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class server_trending_songs extends Fragment {


    public server_trending_songs() {
        // Required empty public constructor
    }
    ListView lv;
    String[] songs,thumbs;
    ArrayList<String> songs1,thumbs1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_server_trending_songs, container, false);
        lv=(ListView)view.findViewById(R.id.lv3);

        songs1=new ArrayList<String>();
        thumbs1=new ArrayList<String>();
        RequestQueue queue= Volley.newRequestQueue(getContext());
        String url = "http://jemshid.pythonanywhere.com/get_trending";
        JsonObjectRequest request= new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Intent intent=new Intent("songstarted");
                            intent.putExtra("message","stopload");
                            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                            int size = response.getInt("size");

                            for(int i=0;i<size;i++)
                            {

                                songs1.add(response.getString("song"+i));
                                songList.process(songs1.get(i));
                                thumbs1.add(songList.getID()+".jpg");

                            }
                            songs=songs1.toArray(new String[songs1.size()]);
                            thumbs=thumbs1.toArray(new String[thumbs1.size()]);
                            lv.setAdapter(new serverbaseadapter(getContext(),thumbs,songs,"pub"));


                        }catch (Exception e)
                        {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Intent intent=new Intent("songstarted");
                intent.putExtra("message","stopload");
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
            }
        });
        queue.add(request);

        Intent intent=new Intent("songstarted");
        intent.putExtra("message","load");
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                songList.putsongs(songs);
                Intent intent=new Intent("songstarted");
                intent.putExtra("message","play");
                intent.putExtra("song",position);
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);



                songList.process(songList.getsongs(position));
                Toast.makeText(getContext(),songList.getTitle(),Toast.LENGTH_LONG).show();

            }
        });

        return view;
    }

}

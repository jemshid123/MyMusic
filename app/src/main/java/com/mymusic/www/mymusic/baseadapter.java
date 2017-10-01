package com.mymusic.www.mymusic;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by others on 16-09-2017.
 */

public class baseadapter extends BaseAdapter {
    Context context;
    String Thumb[];
    String name[];
    songList sl;
    LayoutInflater li;

    public baseadapter(Context con,String Thumb[],String name[])
    {
        this.context=con;
        this.Thumb=Thumb;
        this.name=name;
    }
    public int getCount()
    {
        return name.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class holder
    {
        ImageView iv;
        TextView tv,artist;
        TextView tc;
    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent)
    {
        try{
            li=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view=li.inflate(R.layout.customgrid,null);
            holder hold=new holder();

            hold.iv=(ImageView)view.findViewById(R.id.thumb);
            hold.tv=(TextView)view.findViewById(R.id.name);
            hold.artist=(TextView)view.findViewById(R.id.artist);
            hold.tc=(TextView)view.findViewById(R.id.time) ;

            if( Thumb[position] != null) {
                Picasso.with(context).load(new File(Thumb[position])).into( hold.iv);

            }
            sl.process(name[position]);


            hold.tc.setText(sl.getDuration());
            hold.artist.setText(sl.getArtist());
            hold.tv.setText(sl.getTitle());
            hold.iv.setPadding(8,8,8,8);
            hold.iv.setCropToPadding(true);

            return  view;
        }catch (Exception e)
        {
            Log.d("Error in grid adaptor",e.getMessage());

            return  null;
        }
    }
}

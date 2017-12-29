package com.mymusic.www.mymusic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Jemshid on 27-12-2017.
 */

public class serverbaseadapter extends BaseAdapter {
    Context context;
    String Thumb[];
    String name[];
    songList sl;
    LayoutInflater li;

    public serverbaseadapter(Context con,String Thumb[],String name[])
    {
        this.context=con;
        this.Thumb=Thumb;
        this.name=name;
       // Toast.makeText(context,name[0], Toast.LENGTH_LONG).show();

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

        View view;
        serverbaseadapter.holder hold=new serverbaseadapter.holder();
        //  Log.e("position",position+" "+name[position]);
        try {
            li=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view=li.inflate(R.layout.customgrid,null);




            hold.iv=(ImageView)view.findViewById(R.id.thumb);
            hold.tv=(TextView)view.findViewById(R.id.name);
            hold.artist=(TextView)view.findViewById(R.id.artist);
            hold.tc=(TextView)view.findViewById(R.id.time) ;

String uri="http://jemshid.pythonanywhere.com/get_image?id=";



            sl.process(name[position]);
            uri=uri+sl.getID();

            hold.tc.setText(sl.getDuration());
            hold.artist.setText(sl.getArtist());
            hold.tv.setText(sl.getTitle());

            try {
                URL url = new URL(uri);
               // Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());

               //hold.iv.setImageBitmap(image);
                Picasso.with(context).load(uri).into(hold.iv);
            } catch(IOException e) {
                System.out.println(e);
            }
            hold.iv.setPadding(8, 8, 8, 8);
            hold.iv.setCropToPadding(true);

            return  view;
        }catch (Exception e)
        {
            Log.e("Error in grid adaptor1",e.getMessage());
            e.printStackTrace();
            Toast.makeText(context,e.getMessage(), Toast.LENGTH_LONG).show();
            return convertView;
        }
    }
}

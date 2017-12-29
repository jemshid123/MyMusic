package com.mymusic.www.mymusic;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by Jemshid on 26-12-2017.
 */

public class pyServer {

    public static void saveKey(String key, Context context)
    {
        try {
            String res=" ";
            String keys[];
            File file = new File(context.getFilesDir() + "/key.txt");
            if(file.exists())
            {
                Scanner fr=new Scanner(file);
                res=fr.nextLine();
                keys=res.split(Pattern.quote("||"));

                for(String key1:keys)
                {
                    if(key1.equals(key))
                    {

                        return;
                    }
                }
            }

            FileWriter fw = new FileWriter(file,true);
            fw.write(key+"||");
            fw.close();

        }catch (Exception e)
        {
            Toast.makeText(context,e.getMessage().toString(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    public static  String getKeys(Context context)
    {
        String res=" ";
        try{
        File file = new File(context.getFilesDir() +"/key.txt");
            if(!file.exists())
            {
                return " 0||";
            }
        Scanner fr=new Scanner(file);
           res=fr.nextLine();
        }catch (Exception e)
        {
            Toast.makeText(context,e.getMessage().toString(),Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }
        return  res;
    }

}

package com.mymusic.www.mymusic;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Scanner;

/**
 * Created by Jemshid on 26-12-2017.
 */

public class pyServer {

    public static void saveKey(String key, Context context)
    {
        try {
            File file = new File(context.getFilesDir() + "/key.txt");

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
        File file = new File(context.getFilesDir() + "/key.txt");
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

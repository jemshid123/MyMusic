package com.mymusic.www.mymusic;

import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Created by others on 17-09-2017.
 * used to extract song data from arraylist
 */

public  class songList {
  static String[] songs,songlist,temp;
    static String thumbs[];
   private static String[] details;
    public  static void putsongs(String[] details) {
     songs=details;
    }
    public  static String getsongs(int i) {
        return songs[i];
    }

    public  static void putsongscomplete(String[] details) {

        songlist=details;
    }

    public  static void putthumbs(String[] details) {
        thumbs=details;
    }

    public  static String[] getsongcomplete()
    {
        return songlist;
    }

    public  static boolean process(String string)

    {  //Log.e("pattern error",string);

        details=string.split(Pattern.quote("|@@|"));
temp=details;
           if(details.length != 6)
           {
               details=temp;
               Log.e("pattern error",string);
return  true;

           }
        return true;


    }

    public  static String getID()
    {

        return details[0];
    }

    public  static String getArtist()
    {

        return details[1];
    }

    public  static String getTitle()
    {

        return details[2];
    }

    public  static String getPath()
    {

        return details[3];
    }

    public  static String getDuration()
    {
       long millis=Integer.parseInt(details[5]);

        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        String sec=" ";
        try {
            seconds = seconds % 60;
            sec = "" + seconds;
            sec = sec.substring(0, 2);
            return minutes + ":" + sec;
        }catch (Exception e)
        {
            return minutes + ":" + sec;
        }
    }

    public  static int getrawduration()
    {
        int millis=Integer.parseInt(details[5]);
        return  millis;

    }

    public  static String formatduration(long millis)
    {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        String sec=" ";
try {

    seconds = seconds % 60;
    sec = "" + seconds;
    sec = sec.substring(0, 2);
    return minutes + ":" + sec;
}catch (Exception e)
{
    return minutes + ":" + sec;
}
    }
}

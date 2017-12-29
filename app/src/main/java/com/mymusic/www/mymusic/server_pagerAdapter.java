package com.mymusic.www.mymusic;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * Created by Jemshid on 29-12-2017.
 */

public class server_pagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
   static server_trending_songs tab11;
    static server_recommended_songs tab22;
    public server_pagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        tab11 = new server_trending_songs();
        tab22 = new server_recommended_songs();

    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Log.e("album","tab1");
                return tab11;
                //return null;
            case 1:
                Log.e("album","tab2");
                return tab22;
               // return null;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}

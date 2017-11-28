package com.mymusic.www.mymusic;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * Created by others on 17-06-2017.
 */

public class pagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    static song tab1,tab3;
static album tab2;
    public pagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        tab1 = new song();
        tab2 = new album();
        tab3 = new song();
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Log.e("album","tab1");
                return tab1;
            case 1:
                Log.e("album","tab2");
                return tab2;
            case 2:

                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}

package com.mymusic.www.mymusic;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by others on 17-06-2017.
 */

public class pagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    static song tab1,tab2,tab3;
    public pagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        tab1 = new song();
        tab2 = new song();
        tab3 = new song();
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:

                return tab1;
            case 1:

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

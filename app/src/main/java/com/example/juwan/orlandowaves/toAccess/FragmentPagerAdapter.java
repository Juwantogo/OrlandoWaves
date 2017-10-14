package com.example.juwan.orlandowaves.toAccess;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.juwan.orlandowaves.Fragments.ProfileFragment;
import com.example.juwan.orlandowaves.Fragments.ShopFragment;
import com.example.juwan.orlandowaves.Fragments.StartFragment;
import com.example.juwan.orlandowaves.Fragments.TicketsFragment;

/**
 * Created by Juwan on 10/12/2017.
 */

public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private Context mContext;

    //context = show on this page, FragmentManager is managing frags
    public FragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        //mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new StartFragment();
        } else if (position == 1){
            return new ShopFragment();
        } else if (position == 2){
            return new TicketsFragment();
        } else {
            return new ProfileFragment();
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 4;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return "Home";//start xml and java
            case 1:
                return "ShopFragment";
            case 2:
                return "TicketsFragment";
            case 3:
                return "ProfileFragment";
            default:
                return null;
        }
    }
}

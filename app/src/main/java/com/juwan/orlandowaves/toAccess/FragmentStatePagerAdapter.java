package com.juwan.orlandowaves.toAccess;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Juwan on 10/12/2017.
 */

public class FragmentStatePagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private Context mContext;
    private final List<Fragment> mFragmentList = new ArrayList();
    private final HashMap<Fragment, Integer> mFragments = new HashMap<>(); //set Fragment to Int
    private final HashMap<String, Integer> mFragmentNumbers = new HashMap<>(); //set String to Int
    private final HashMap<Integer, String> mFragmentNames = new HashMap<>(); //set Fragment to String


    //context = show on this page, FragmentManager is managing frags
    public FragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
        //mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String fragmentName){
        mFragmentList.add(fragment);
        mFragments.put(fragment, mFragmentList.size()-1); //put frags in hashmaps made above;
        mFragmentNumbers.put(fragmentName, mFragmentList.size()-1); //put frags in hashmaps made above;
        mFragmentNames.put(mFragmentList.size(), fragmentName); //put frags in hashmaps made above;
    }



    /**
     * returns the fragment with the name @param
     * @param fragmentName
     * @return
     */
    public Integer getFragmentNumber(String fragmentName){
        if(mFragmentNumbers.containsKey(fragmentName)){
            return mFragmentNumbers.get(fragmentName);
        }else{
            return null;
        }
    }

    public String getFragmentName(Integer fragmentNumber){
        if(mFragmentNames.containsKey(fragmentNumber)){
            return mFragmentNames.get(fragmentNumber);
        }else{
            return null;
        }
    }
    public Integer getFragmentNumber(Fragment fragment){
        if(mFragmentNumbers.containsKey(fragment)){
            return mFragmentNumbers.get(fragment);
        }else{
            return null;
        }
    }
}

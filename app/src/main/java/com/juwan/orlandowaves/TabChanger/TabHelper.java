package com.juwan.orlandowaves.TabChanger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;

import com.juwan.orlandowaves.ActivityClass.Home;
import com.juwan.orlandowaves.ActivityClass.Profile;
import com.juwan.orlandowaves.ActivityClass.Shop;
import com.juwan.orlandowaves.ActivityClass.Tickets;
import com.juwan.orlandowaves.R;

/**
 * Created by Juwan on 10/13/2017.
 */

public class TabHelper {
    private static final String TAB = "";


    //mode scrollable shouldn't be there
    //enable tab gets current screen and tablayout
    //checks number of tab and currectly opens the activity it SHOULD be assigned to
    public static void enableTabChange(final Context context, final Activity callingActivity, TabLayout view){
        //view.setTabMode(TabLayout.MODE_SCROLLABLE);
        view.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0:
                        Intent intent1 = new Intent(context, Home.class);
                        intent1.putExtra("tabNum", tab.getPosition());
                        context.startActivity(intent1);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case 1:
                        Intent intent2 = new Intent(context, Shop.class);
                        intent2.putExtra("tabNum", tab.getPosition());
                        context.startActivity(intent2);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case 2:
                        Intent intent3 = new Intent(context, Tickets.class);
                        intent3.putExtra("tabNum", tab.getPosition());
                        context.startActivity(intent3);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case 3:
                        Intent intent4 = new Intent(context, Profile.class);
                        intent4.putExtra("tabNum", tab.getPosition());
                        context.startActivity(intent4);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}

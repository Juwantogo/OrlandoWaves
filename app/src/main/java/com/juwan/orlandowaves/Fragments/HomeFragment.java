package com.juwan.orlandowaves.Fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.juwan.orlandowaves.R;
import com.juwan.orlandowaves.TabChanger.TabHelper;
import com.juwan.orlandowaves.toAccess.TicketListAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private DatabaseReference myRef;
    private int tabNum;
    TabLayout tabs;
    private Context mContext;
    private View view, schedule;
    private TicketListAdapter adapter;
    private ListView list;
            private ImageView roster, shop, logo;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_home, container, false);

        list = view.findViewById(R.id.list);
        roster = view.findViewById(R.id.roster);
        shop = view.findViewById(R.id.shop);
        logo = view.findViewById(R.id.logo);
        schedule = view.findViewById(R.id.schedule);
        tabNum = getArguments().getInt(getString(R.string.currentActivity));
        setUpTabs();
        myRef = FirebaseDatabase.getInstance().getReference();
        mContext = getActivity();

        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabs.getTabAt(1).select();
            }
        });
        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSchedule();
            }
        });
        roster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRoster();
            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSite();
            }
        });
        //getGameTickets();
        return view;
    }

    public void setUpTabs(){
        tabs = (TabLayout) view.findViewById(R.id.sliding_tabs);
        tabs.getTabAt(tabNum).select(); //select correct tab Number after finding the tabs but before setting enable change tabs
        TabHelper.enableTabChange(getActivity(), getActivity(),tabs);
    }

    public void gotoRoster(){
        RosterFragment fragment = new RosterFragment();
        Bundle args = new Bundle();
        args.putInt(getString(R.string.currentActivity), tabNum);
        fragment.setArguments(args);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragContainer, fragment);
        transaction.addToBackStack(getString(R.string.rosFrag));
        transaction.commit();

    }

    public void gotoSite(){
        Uri uri = Uri.parse("http://www.orlandowaves.com");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void gotoSchedule(){
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putInt(getString(R.string.currentActivity), tabNum);
        fragment.setArguments(args);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragContainer, fragment);
        transaction.addToBackStack(getString(R.string.scheduleFrag));
        transaction.commit();

    }

}

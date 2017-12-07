package com.juwan.orlandowaves.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.juwan.orlandowaves.R;
import com.juwan.orlandowaves.toAccess.ScheduleAdapter;
import com.juwan.orlandowaves.toAccess.TicketListAdapter;
import com.juwan.orlandowaves.toAccess.games;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {

    private DatabaseReference myRef;
    private int tabNum;
    private Context mContext;
    private View view;
    private TicketListAdapter adapter;
    private ListView list;
    private TextView name;
    public ScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view =  inflater.inflate(R.layout.fragment_roster, container, false);

        list = view.findViewById(R.id.list);
        name = view.findViewById(R.id.listName);
        name.setText("Orlando Waves 2017 Schedule");
        final View Back= view.findViewById(R.id.Back);

        tabNum = getArguments().getInt(getString(R.string.currentActivity));
//        setUpTabs();
        myRef = FirebaseDatabase.getInstance().getReference();
        setUpLists();
        //getGameTickets();

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm  = getFragmentManager();
                fm.popBackStack();
            }
        });
        return view;
    }

    public void setUpLists(){
        final ArrayList<String> name = new ArrayList<>();
        final ArrayList<String> opponent = new ArrayList<>();
        final ArrayList<String> time = new ArrayList<>();
        final ArrayList<String> location = new ArrayList<>();
        final ArrayList<String> imgURLs = new ArrayList<>();
        Query query = myRef;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot rosterSS = dataSnapshot.child("team").child("games");
                for(DataSnapshot singleSnapshot :  rosterSS.getChildren()){
                    games game = singleSnapshot.getValue(games.class);
                    name.add(game.getDate() + " - " + game.getEvent());
                    opponent.add("Orlando Waves vs " + game.getOpponent());
                    time.add("TipOff: " + game.getTime() + " Doors Open: " + game.getDoors_open());
                    location.add("Location: " + game.getLocation());
                    imgURLs.add(game.getImage_url());
                }
                ScheduleAdapter adapter = new ScheduleAdapter(getActivity(),name,opponent,time,location,imgURLs);
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    public void setUpTabs(){
//        TabLayout tabs = (TabLayout) view.findViewById(R.id.sliding_tabs);
//        tabs.getTabAt(tabNum).select(); //select correct tab Number after finding the tabs but before setting enable change tabs
//        TabHelper.enableTabChange(getActivity(), getActivity(),tabs);
//    }

}

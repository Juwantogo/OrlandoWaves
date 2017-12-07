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
import com.juwan.orlandowaves.toAccess.Players;
import com.juwan.orlandowaves.toAccess.RosterAdapter;
import com.juwan.orlandowaves.toAccess.TicketListAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RosterFragment extends Fragment {
    private DatabaseReference myRef;
    private int tabNum;
    private Context mContext;
    private View view;
    private TicketListAdapter adapter;
    private ListView list;
    private TextView name;

    public RosterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view =  inflater.inflate(R.layout.fragment_roster, container, false);

        list = view.findViewById(R.id.list);
        name = view.findViewById(R.id.listName);
name.setText("Orlando Waves Roster");
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
        final ArrayList<String> height = new ArrayList<>();
        final ArrayList<String> position = new ArrayList<>();
        final ArrayList<String> description = new ArrayList<>();
        final ArrayList<String> imgURLs = new ArrayList<>();
        Query query = myRef;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot rosterSS = dataSnapshot.child("team").child("roster");
                for(DataSnapshot singleSnapshot :  rosterSS.getChildren()){
                    Players player = singleSnapshot.getValue(Players.class);
                    name.add(player.getNumber() + " - " + player.getName());
                    height.add("Height: " + player.getHeight());
                    position.add("Position: " + player.getPosition());
                    description.add("Experience: " + player.getExperience());
                    imgURLs.add(player.getImage_url());
                }

                RosterAdapter adapter = new RosterAdapter(getActivity(),name,height,position,description,imgURLs);
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

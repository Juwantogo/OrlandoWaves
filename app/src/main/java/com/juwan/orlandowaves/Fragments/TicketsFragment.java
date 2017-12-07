package com.juwan.orlandowaves.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.juwan.orlandowaves.R;
import com.juwan.orlandowaves.TabChanger.TabHelper;
import com.juwan.orlandowaves.toAccess.Items;
import com.juwan.orlandowaves.toAccess.TicketListAdapter;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class TicketsFragment extends Fragment {
    private DatabaseReference myRef;
    private int tabNum;
    private Context mContext;
    private View view;
    private TicketListAdapter adapter;
    private ListView list;

    public TicketsFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_tickets, container, false);

        list = view.findViewById(R.id.list);
        tabNum = getArguments().getInt(getString(R.string.currentActivity));
        setUpTabs();
        myRef = FirebaseDatabase.getInstance().getReference();
        getGameTickets();
        return view;
    }


    public void setUpTabs(){
        TabLayout tabs = (TabLayout) view.findViewById(R.id.sliding_tabs);
        tabs.getTabAt(tabNum).select(); //select correct tab Number after finding the tabs but before setting enable change tabs
        TabHelper.enableTabChange(getActivity(), getActivity(),tabs);
    }

    private void getGameTickets(){
        final ArrayList<Items> itemList = new ArrayList<>();
        Query query = myRef.child("order").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){//singleSnapshot = each order in users dataSnapshot = orders in users
                    //interates through all childs in query(games node: 1, 2...)
                    //singleSnapshot.
                    //singleSnapshot.hasChild("")

                    for(DataSnapshot items : singleSnapshot.getChildren()) {//items = each each in order single.getChildren = items in order
                        String gameCheck = items.getKey();
                        gameCheck = gameCheck.replaceAll("[0-9]+", "");
                        if(gameCheck.equals("game") || gameCheck.equals("season")){
                            itemList.add(items.getValue(Items.class));
                        }
                        Log.e(TAG, "games" + itemList);

                        //if(game)

                        //itemList.add(items.getValue().toString());
                        //items.getValue().toString();
                        //Log.d(TAG, gameCheck);

                    }
                    //singleSnapshot.child();
                    //itemList.add(singleSnapshot.getValue(Items.class));
                    //add each entire node aka singleSnapshot=[i...i*n] to gameList array attach the games.class to it
                    //gameList.i -> i*n = each node taken
                }

                adapter = new TicketListAdapter(getActivity(), R.layout.single_ticket_item, itemList);
                list.setAdapter(adapter);
                Log.d(TAG, itemList.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query cancelled.");
                Toast.makeText(getActivity(), "NOT getting in",
                        Toast.LENGTH_SHORT).show();
            }

        });


    }
}

package com.juwan.orlandowaves.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.juwan.orlandowaves.ActivityClass.LoginActivity;
import com.juwan.orlandowaves.R;
import com.juwan.orlandowaves.toAccess.Config;

import com.juwan.orlandowaves.toAccess.GridAdapter;
import com.juwan.orlandowaves.toAccess.UniversalImageLoader;
import com.juwan.orlandowaves.toAccess.games;
import com.juwan.orlandowaves.toAccess.users;
import com.nostra13.universalimageloader.core.ImageLoader;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class BuyGameFragment extends Fragment {
    SharedPreferences preferences;
    private String fbc;
    private String Product;
    private games muser;
    private ImageView IMG;
    private TextView NAME, DES, Price;
    ArrayList<String> imgUrls;
    ArrayList<String> date; //date + " - " + opponent
    ArrayList<String> day;
    ArrayList<String> opponent;
    //ArrayList<String> info; //day + ", " + date + " at " + time;
    ArrayList<String> location;
    ArrayList<String> doorsopen;
    ArrayList<String> event;
    ArrayList<String> adultPrice;
    ArrayList<String> youthPrice;
    ArrayList<String> time, date_opponent_event, adult_youth;
    private Spinner Games, Type, Quantity;



    public BuyGameFragment() {
        // Required empty public constructor
        super();
        setArguments(new Bundle());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_buy_game, container, false);
        Games = rootview.findViewById(R.id.GamesSpinner);
        Type = rootview.findViewById(R.id.TypeSpinner);
        Quantity = rootview.findViewById(R.id.QuantitySpinner);
        Price= rootview.findViewById(R.id.PriceTV);
        DES= rootview.findViewById(R.id.DescriptionTV);
        final View Back= rootview.findViewById(R.id.Back);
        final View AddCart = rootview.findViewById(R.id.AddCart);
        IMG= rootview.findViewById(R.id.ProductImage);
        NAME = rootview.findViewById(R.id.ProductName);



        //SetUp ImageLoader
        //UniversalImageLoader universalImageLoader = new UniversalImageLoader(getActivity());
        //ImageLoader.getInstance().init(universalImageLoader.getConfig());

        //prefNOTneeded
        preferences = this.getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        fbc = preferences.getString(fbc, "");


        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm  = getFragmentManager();
                fm.popBackStack();
            }
        });

        setupLists();
        try{
            muser = getIMG();
            UniversalImageLoader.setImage(muser.getImage_url(),IMG,null,"");
            NAME.setText(getName());

        }catch(NullPointerException e){

        }

        //

        //name.setText(Product);
        AddCart.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }
        );
        return rootview;
    }

    //private

    private void logout(){
        //Add To CART **********
        preferences = this.getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        //Getting editor
        SharedPreferences.Editor editor = preferences.edit();

        //Puting the value false for loggedin
        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

        //Putting blank value to email
        editor.putString(Config.EMAIL_SHARED_PREF, "");

        //Saving the sharedpreferences
        editor.commit();

        //Starting login activity
        Intent intent = new Intent(this.getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void getGames(){
        //getGames **********

    }

    private void setupLists(){
 imgUrls = new ArrayList<>();
date = new ArrayList<>(); //date + " - " + opponent
day = new ArrayList<>();
opponent = new ArrayList<>();
        //ArrayList<String> info; //day + ", " + date + " at " + time;
location = new ArrayList<>();
doorsopen = new ArrayList<>();
event = new ArrayList<>();
adultPrice = new ArrayList<>();
youthPrice = new ArrayList<>();
time = new ArrayList<>();
        date_opponent_event = new ArrayList<>();
        adult_youth = new ArrayList<>();
        Log.d(TAG, "setupGridView: Setting up image grid.");

        //DB-team-games
        final ArrayList<games> gameList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("team").child("games"); //looks at game node
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    //interates through all childs in query(games node: 1, 2...)
                    gameList.add(singleSnapshot.getValue(games.class));
                    //add each entire node aka singleSnapshot=[i...i*n] to gameList array attach the games.class to it
                    //gameList.i -> i*n = each node taken
                }
                //Toast.makeText(getActivity(), "getting in",Toast.LENGTH_SHORT).show();
                //setup our image grid
                //int gridWidth = getResources().getDisplayMetrics().widthPixels;
                //int imageWidth = gridWidth/NUM_GRID_COLUMNS;
                //gridView.setColumnWidth(imageWidth);
                String game = getString(R.string.SingleGames);


                //new arraylists for everything listed in gridview
                for(int i = 0; i < gameList.size(); i++){
                    imgUrls.add(gameList.get(i).getImage_url()); //CHANGE - CHANGE
                    adultPrice.add(gameList.get(i).getAdult_price());
                    youthPrice.add(gameList.get(i).getYouth_price());
                    date.add(gameList.get(i).getDate());
                    day.add(gameList.get(i).getDay());
                    opponent.add(gameList.get(i).getOpponent());
                    time.add(gameList.get(i).getTime());
                    location.add(gameList.get(i).getAdult_price());
                    doorsopen.add(gameList.get(i).getDoors_open());
                    event.add(gameList.get(i).getEvent());
                    date_opponent_event.add(gameList.get(i).getDate() + " - " + gameList.get(i).getOpponent() + " - " + gameList.get(i).getEvent());
                }
                //set EverythingUp in the FIRST GRID VIEW
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.simple_list , date_opponent_event);
                Games.setAdapter(adapter);


                //setOnClickListener for each item AdapterView automatically gets gridViews adapter for positioning!!!!
                Games.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        setViews(gameList.get(position));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Games.setSelection(0);
                        setViews(gameList.get(0));
                    }
                });
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query cancelled.");
                Toast.makeText(getActivity(), "NOT getting in",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setViews(games selectedGame){
            adult_youth.clear();
            DES.setText("vs " + selectedGame.getOpponent() +"\nEvent: " + selectedGame.getEvent() + "\nWhen: " + selectedGame.getDay() +", " + selectedGame.getDate() + " at " + selectedGame.getTime() +
                    "\nWhere: " + selectedGame.getLocation() + "\nDoors Open: " + selectedGame.getDoors_open());
                Price.setText(selectedGame.getAdult_price());
                adult_youth.add("Adult - " + selectedGame.getAdult_price());
                adult_youth.add("Youth - " + selectedGame.getYouth_price());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.simple_list , adult_youth);
                Type.setAdapter(adapter);

                Type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(position == 0){
                            Price.setText(selectedGame.getAdult_price());
                        }else{
                            Price.setText(selectedGame.getYouth_price());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Type.setSelection(0);
                    }
                });
    }


    private String getName(){
        Bundle bundle = this.getArguments();
        if(bundle != null){
            return bundle.getString(getString(R.string.itemType));
        }
        else{
            return null;
        }
    }
    private games getIMG(){
        Bundle bundle = this.getArguments();
        if(bundle != null){
            return bundle.getParcelable(getString(R.string.itemName));
        }
        else{
            return null;
        }
    }
}

package com.juwan.orlandowaves.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.juwan.orlandowaves.R;
import com.juwan.orlandowaves.toAccess.CartDBHelper;
import com.juwan.orlandowaves.toAccess.Config;
import com.juwan.orlandowaves.toAccess.Currency;
import com.juwan.orlandowaves.toAccess.UniversalImageLoader;
import com.juwan.orlandowaves.toAccess.games;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class BuyGameFragment extends Fragment {
    SharedPreferences preferences;
    private long fbc;
    private String Product;
    private games muser;
    private ImageView IMG;
    private TextView NAME, DES, Price, StyleTV,TypeTV,QuantityTV;
    Currency currency;
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
    private Button AddCart;



    public BuyGameFragment() {
        // Required empty public constructor
        super();
        setArguments(new Bundle());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_buy_game, container, false);
        Games = rootview.findViewById(R.id.StyleSpinner);
        Type = rootview.findViewById(R.id.TypeSpinner);
        Quantity = rootview.findViewById(R.id.QuantitySpinner);
        Price= rootview.findViewById(R.id.PriceTV);
        DES= rootview.findViewById(R.id.DescriptionTV);
        final View Back= rootview.findViewById(R.id.Back);
        AddCart = rootview.findViewById(R.id.AddCart);
        IMG= rootview.findViewById(R.id.ProductImage);
        NAME = rootview.findViewById(R.id.ProductName);
        StyleTV = rootview.findViewById(R.id.StyleTV);
        TypeTV = rootview.findViewById(R.id.TypeTV);

        QuantityTV = rootview.findViewById(R.id.QuantityTV);

        currency = new Currency();



        //SetUp ImageLoader
        //UniversalImageLoader universalImageLoader = new UniversalImageLoader(getActivity());
        //ImageLoader.getInstance().init(universalImageLoader.getConfig());

        //prefNOTneeded
        preferences = this.getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        fbc = preferences.getLong(Config.fbc, 0);
        Log.e(TAG, "check FBC" +  fbc);


        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm  = getFragmentManager();
                fm.popBackStack();
            }
        });

        try{
//            muser = getIMG();
//            UniversalImageLoader.setImage(muser.getImage_url(),IMG,null,"");
            NAME.setText(getName());

        }catch(NullPointerException e){

        }
        if(getName().equals("Single Game Ticket")){
            setupListsForGames();
        }else if(getName().equals("Season Pass")){
            setupListsForSeason();
        }

        return rootview;
    }

    private void setupListsForGames(){
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
                    date_opponent_event.add(gameList.get(i).getDate() + " - " + gameList.get(i).getOpponent() + " - " + gameList.get(i).getEvent());
                }
                //set EverythingUp in the FIRST GRID VIEW
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.simple_list , date_opponent_event);
                Games.setAdapter(adapter);


                //setOnClickListener for each item AdapterView automatically gets gridViews adapter for positioning!!!!
                Games.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        setViewsForGames(gameList.get(position));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Games.setSelection(0);
                        setViewsForGames(gameList.get(0));
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

    private void setViewsForGames(final games selectedGame){
            adult_youth.clear();
        //muser = selectedGame;
           UniversalImageLoader.setImage(selectedGame.getImage_url(),IMG,null,"");
            DES.setText("vs " + selectedGame.getOpponent() +"\nEvent: " + selectedGame.getEvent() + "\nWhen: " + selectedGame.getDay() +", " + selectedGame.getDate() + " at " + selectedGame.getTime() +
                    "\nWhere: " + selectedGame.getLocation() + "\nDoors Open: " + selectedGame.getDoors_open());
                Price.setText(selectedGame.getAdult_price());
                if(fbc != 0L){
                    adult_youth.add("Adult - " + currency.stringPercentAndQuantity(selectedGame.getAdult_price(),10,Integer.parseInt(Quantity.getSelectedItem().toString())));
                    adult_youth.add("Youth - " + currency.stringPercentAndQuantity(selectedGame.getYouth_price(),10,Integer.parseInt(Quantity.getSelectedItem().toString())));
                }else{
                    adult_youth.add("Adult - " + currency.stringMultiply(selectedGame.getAdult_price(),Integer.parseInt(Quantity.getSelectedItem().toString())));
                    adult_youth.add("Youth - " + currency.stringMultiply(selectedGame.getYouth_price(),Integer.parseInt(Quantity.getSelectedItem().toString())));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.simple_list , adult_youth);
                Type.setAdapter(adapter);

                Type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(position == 0){
                            if(fbc != 0L){
                                Price.setText(currency.stringPercentAndQuantity(selectedGame.getAdult_price(),10,Integer.parseInt(Quantity.getSelectedItem().toString())));
                            }else{
                                Price.setText(currency.stringMultiply(selectedGame.getAdult_price(),Integer.parseInt(Quantity.getSelectedItem().toString())));
                            }
                        }else{
                            if(fbc != 0L){
                                Price.setText(currency.stringPercentAndQuantity(selectedGame.getYouth_price(),10,Integer.parseInt(Quantity.getSelectedItem().toString())));
                            }else{
                                Price.setText(currency.stringMultiply(selectedGame.getYouth_price(),Integer.parseInt(Quantity.getSelectedItem().toString())));
                            }

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Type.setSelection(0);
                    }
                });

        AddCart.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String price = selectedGame.getAdult_price();
                        if(Type.getSelectedItemPosition() == 0){
                            if(fbc != 0L){
                                price = (currency.stringPercent(selectedGame.getAdult_price(),10));

                            }
                            else{
                                price = selectedGame.getAdult_price();
                            }
                        }else{
                            if(fbc != 0L){
                                price = (currency.stringPercent(selectedGame.getYouth_price(),10));

                            }
                            else{
                               price = selectedGame.getYouth_price();
                            }
                        }
                        CartDBHelper cartHelper = new CartDBHelper(getActivity());
                        cartHelper.AddToCart(selectedGame, Type.getSelectedItem().toString(),price ,Price.getText().toString(), Quantity.getSelectedItem().toString());
                        FragmentManager fm  = getFragmentManager();
                        fm.popBackStack();
                    }
                }
        );

        Quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(fbc != 0L){
                        if(Type.getSelectedItemPosition() == 0) {
                            Price.setText(currency.stringPercentAndQuantity(selectedGame.getAdult_price(),10,Integer.parseInt(Quantity.getSelectedItem().toString())));

                        }else{
                            Price.setText(currency.stringPercentAndQuantity(selectedGame.getYouth_price(),10,Integer.parseInt(Quantity.getSelectedItem().toString())));
                        }
                    }else{
                        if(Type.getSelectedItemPosition() == 0) {
                            Price.setText(currency.stringMultiply(selectedGame.getAdult_price(),Integer.parseInt(Quantity.getSelectedItem().toString())));
                        }else{
                            Price.setText(currency.stringMultiply(selectedGame.getYouth_price(),Integer.parseInt(Quantity.getSelectedItem().toString())));
                        }
                    }


                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setupListsForSeason(){
        Games.setVisibility(View.GONE);
        Type.setVisibility(View.GONE);
        StyleTV.setVisibility(View.GONE);
        TypeTV.setVisibility(View.GONE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("items").child("season_pass"); //looks at game node
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String description = dataSnapshot.child("description").getValue().toString();
                String name = dataSnapshot.child("name").getValue().toString();
                String type = dataSnapshot.child("name").getValue().toString();
                String price = dataSnapshot.child("price").getValue().toString();
                String imgURL = dataSnapshot.child("image_url").getValue().toString();

                setViewsForSeason(name, description,type,price,imgURL);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setViewsForSeason(final String name, final String des, String type, final String price, final String imgURL){
        UniversalImageLoader.setImage(imgURL,IMG,null,"");
        DES.setText(des);
        Price.setText(price);

        Quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Price.setText(currency.stringMultiply(price,Integer.parseInt(Quantity.getSelectedItem().toString())));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

AddCart.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        CartDBHelper cartHelper = new CartDBHelper(getActivity());
        cartHelper.AddItemsToCart(name, name,des ,price, Price.getText().toString(),Quantity.getSelectedItem().toString(), imgURL);
        FragmentManager fm  = getFragmentManager();
        fm.popBackStack();
    }
});

    }
//These Functions set up the product Name and getIMG
    private String getName(){
        Bundle bundle = this.getArguments();
        if(bundle != null){
            return bundle.getString(getString(R.string.itemName));
        }
        else{
            return null;
        }
    }
//    private String getIMG(){
//        Bundle bundle = this.getArguments();
//        if(bundle != null){
//            return bundle.getString(getString(R.string.itemName));
//        }
//        else{
//            return null;
//        }
//    }
}

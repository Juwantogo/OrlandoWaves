package com.juwan.orlandowaves.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.juwan.orlandowaves.toAccess.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * A simple {@link Fragment} subclass.
 */
public class BuyGameFragment extends Fragment {
    SharedPreferences preferences;
    private String fbc;
    private String Product;

    public BuyGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_buy_game, container, false);
        final View Games = rootview.findViewById(R.id.GamesSpinner);
        final View Type = rootview.findViewById(R.id.TypeSpinner);
        final View Quantity = rootview.findViewById(R.id.QuantitySpinner);
        final View Price= rootview.findViewById(R.id.PriceTV);
        final View AddCart = rootview.findViewById(R.id.AddCart);

        //SetUp ImageLoader
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(getActivity());
        ImageLoader.getInstance().init(universalImageLoader.getConfig());

        //prefNOTneeded
        preferences = this.getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        fbc = preferences.getString(fbc, "");

        //
        TextView name = (TextView) rootview.findViewById(R.id.Name);
        getGames();
        name.setText(Product);
        AddCart.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }
        );
        return rootview;
    }

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
}

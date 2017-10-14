package com.example.juwan.orlandowaves.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.juwan.orlandowaves.toAccess.Config;
import com.example.juwan.orlandowaves.ActivityClass.LoginActivity;
import com.example.juwan.orlandowaves.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    SharedPreferences preferences;
    private String first;
    private String last;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_profile, container, false);
        final View button = rootview.findViewById(R.id.logout);
        preferences = this.getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        first = preferences.getString(Config.fName, "");
        last = preferences.getString(Config.lName, "");
        TextView name = (TextView) rootview.findViewById(R.id.Name);
        name.setText(first + " " + last);
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logout();
                    }
                }
        );
        return rootview;
    }

    private void logout(){
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

}

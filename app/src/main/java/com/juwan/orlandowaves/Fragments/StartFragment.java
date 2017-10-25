package com.juwan.orlandowaves.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.juwan.orlandowaves.R;


/**
 * Activities that contain this fragment must implement the
 */
public class StartFragment extends Fragment {
    public StartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Intent intent;
        return inflater.inflate(R.layout.fragment_start, container, false);
    }
}

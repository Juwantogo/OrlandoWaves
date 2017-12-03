package com.juwan.orlandowaves.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.juwan.orlandowaves.R;
import com.juwan.orlandowaves.toAccess.CartDBHelper;
import com.juwan.orlandowaves.toAccess.CartListAdapter;
import com.juwan.orlandowaves.toAccess.Items;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import static android.content.ContentValues.TAG;

/**

 */
public class Cart extends Fragment {
    //private OnFragmentInteractionListener mListener;
    CartDBHelper myDb;
    CartListAdapter adapter;
    TextView total;
    Double totalD = 0.00;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    int games = 1;
    int season = 1;
    int merch = 1;
    int count;

    public Cart() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_cart, container, false);
        final View Back= rootview.findViewById(R.id.Back);
        final View Pay= rootview.findViewById(R.id.pay);
        ListView list = rootview.findViewById(R.id.list);
        total = rootview.findViewById(R.id.total);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm  = getFragmentManager();
                fm.popBackStack();
            }
        });

        Pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDb.getAllData();
                String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                totalD= 0.00;


                myRef = FirebaseDatabase.getInstance().getReference();
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds: dataSnapshot
                                .child("order")
                                .child(user)
                                .getChildren()){
                                    count++;
                        }

                        while (res.moveToNext()) {

                            String currentPrice = res.getString(res.getColumnIndexOrThrow("finalprice"));
                            //change if for merch and season
                            if(res.getString(res.getColumnIndexOrThrow("name")).equals("Single Game Ticket")){
                                Items items = new Items(
                                        res.getString(res.getColumnIndexOrThrow("name")),
                                        ("Date: " + res.getString(res.getColumnIndexOrThrow("date")) + " Event: " + res.getString(res.getColumnIndexOrThrow("event")) + " - vs " + res.getString(res.getColumnIndexOrThrow("opponent"))),
                                        res.getString(res.getColumnIndexOrThrow("finalprice")),
                                        Integer.parseInt(res.getString(res.getColumnIndexOrThrow("quantity"))),
                                        res.getString(res.getColumnIndexOrThrow("type")),
                                        res.getString(res.getColumnIndexOrThrow("location"))
                                );
                                Log.e(TAG, "totalPRICE*********: " + items);
                                myRef.child("order")
                                        .child(user)
                                        .child("order" + count).child("game" + games).setValue(items);
                                games++;
                            }
                            currentPrice = currentPrice.replace("$","");
                            //Log.e(TAG, "PRICE*********: " + currentPrice);
                            Double dPrice = Double.parseDouble(currentPrice);
                            totalD = totalD + dPrice;
                            Log.e(TAG, "totalPRICE*********: " + totalD);



                        }

                        SimpleDateFormat formatter = new SimpleDateFormat();
                        myRef.child("order")
                                .child(user)
                                .child("order" + count).child("total").setValue(totalD);
                        myRef.child("order")
                                .child(user)
                                .child("order" + count).child("address").setValue("addy");
                        myRef.child("order")
                                .child(user)
                                .child("order" + count).child("date").setValue(formatter.format(Calendar.getInstance().getTime()));
                        myRef.child("order")
                                .child(user)
                                .child("order" + count).child("coupon").setValue("0");
                        myRef.child("order")
                                .child(user)
                                .child("order" + count).child("orderNUM").setValue(UUID.randomUUID().toString());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                //adapter.changeCursor(res);
                //total.setText(totalD.toString());
            }
        });

        myDb = new CartDBHelper(getActivity());
        Cursor res = myDb.getAllData();
        adapter = new CartListAdapter(getActivity(), res, this);
        //adapter.setButton(getActivity());
// Attach cursor adapter to the ListView
        list.setAdapter(adapter);
        //adapter.getTotal();
        //get arguments

        return rootview;
    }

    public void updateView(){

    }

    public void grabNewTotal(){
        total.setText(adapter.getTotal().toString());
    }

    //@Override
    public void afterLastCursor(){
        Cursor res = myDb.getAllData();
        totalD= 0.00;
        while (res.moveToNext()) {
            String currentPrice = res.getString(res.getColumnIndexOrThrow("finalprice"));
            currentPrice = currentPrice.replace("$","");
            Log.e(TAG, "PRICE*********: " + currentPrice);
            Double dPrice = Double.parseDouble(currentPrice);
            totalD = totalD + dPrice;
        }
        adapter.changeCursor(res);
        total.setText(totalD.toString());
    }
}

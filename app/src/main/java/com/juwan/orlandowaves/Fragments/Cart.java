package com.juwan.orlandowaves.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.juwan.orlandowaves.R;
import com.juwan.orlandowaves.toAccess.CartDBHelper;
import com.juwan.orlandowaves.toAccess.CartListAdapter;
import com.juwan.orlandowaves.toAccess.Currency;

import java.math.BigDecimal;

/**

 */
public class Cart extends Fragment {
    //private OnFragmentInteractionListener mListener;
    CartDBHelper myDb;
    CartListAdapter adapter;
    TextView total;
    Currency currency;
    BigDecimal totalD = BigDecimal.ZERO;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    int games = 1;
    int season = 1;
    int merch = 1;
    int count;
    String orderNUM, user, _id;



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
        currency = new Currency();

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
                if(res.moveToFirst()){
                    PaymentFragment fragment = new PaymentFragment();
                    //                Bundle args = new Bundle();
//                args.putString(getString(R.string.userID), user);
//                args.putString(getString(R.string.orderID), orderNUM);
//                //args.putString(getString(R.string.cartID), _id);
//                args.putString(getString(R.string.total), (currency.getBigDecimal(total.getText().toString())).toString());
//                fragment.setArguments(args);
//
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragContainer,fragment);
                    transaction.addToBackStack(getString(R.string.payFrag));
                    transaction.commit();
                }else{
                    Toast.makeText(getActivity(), "Cart Is Empty", Toast.LENGTH_SHORT).show();
                }
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
        totalD = BigDecimal.ZERO;
        while (res.moveToNext()) {
            BigDecimal big = currency.getBigDecimal(res.getString(res.getColumnIndexOrThrow("finalprice")));
            totalD = totalD.add(big);
        }
        adapter.changeCursor(res);;
        total.setText(currency.getMoneyString(totalD));
    }
}

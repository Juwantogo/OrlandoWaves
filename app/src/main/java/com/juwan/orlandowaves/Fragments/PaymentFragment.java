package com.juwan.orlandowaves.Fragments;


import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.juwan.orlandowaves.ActivityClass.Tickets;
import com.juwan.orlandowaves.R;
import com.juwan.orlandowaves.toAccess.CartDBHelper;
import com.juwan.orlandowaves.toAccess.Config;
import com.juwan.orlandowaves.toAccess.Currency;
import com.juwan.orlandowaves.toAccess.Items;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import static android.content.ContentValues.TAG;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends Fragment {
    EditText address, city, state, zip;
    TextView total;
    Button submit;
    private String paymentAmount;
    String userID, cartID, orderID, totalAMT;
    Currency currency;
    CartDBHelper myDb;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String orderNUM, user, randomOrder;
    int games = 1;
    int season = 1;
    int merch = 1;
    int count;


    public static final int PAYPAL_REQUEST_CODE = 123;


    //Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

    public PaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_payment, container, false);
        final View rootview = inflater.inflate(R.layout.fragment_payment, container, false);
        address = rootview.findViewById(R.id.address);
        city = rootview.findViewById(R.id.city);
        state = rootview.findViewById(R.id.state);
        zip = rootview.findViewById(R.id.ZipCode);
        total = rootview.findViewById(R.id.total);
        submit = rootview.findViewById(R.id.submit);
        myDb = new CartDBHelper(getActivity());

//        userID = getArguments().getString(getString(R.string.userID));
//        //cartID = getArguments().getString(getString(R.string.cartID));
//        orderID = getArguments().getString(getString(R.string.orderID));
//        totalAMT = getArguments().getString(getString(R.string.total));
        final View Back= rootview.findViewById(R.id.Back);
        currency = new Currency();


getCartTotal();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(address.getText().toString().equals("") || city.getText().toString().equals("") || state.getText().toString().equals("") || zip.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Fill in Address", Toast.LENGTH_SHORT).show();
                }
                else{
                    getPayment();
                }
            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dropLastOrder(0);
                FragmentManager fm  = getFragmentManager();
                fm.popBackStack();
            }
        });

        Intent intent = new Intent(getActivity(), PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        getActivity().startService(intent);
        return rootview;

    }

    @Override
    public void onDestroy() {
        getActivity().stopService(new Intent(getActivity(), PayPalService.class));
        super.onDestroy();
    }

    //0 = Delete 1 = Confirm
    private void dropLastOrder(final int zDoC){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query query = ref.child("order").child(userID).child(orderID);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(zDoC == 0){
                    dataSnapshot.getRef().removeValue();
                }else{
                    dataSnapshot.child("confirm").getRef().setValue("1");
                }
                Log.e(TAG, "onchange" + dataSnapshot.getRef());

//                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
//                    appleSnapshot.getRef().removeValue();
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }

    private void getPayment() {
        //Getting the amount from editText
        paymentAmount = totalAMT;

        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)), "USD", "Orlando Waves Order",
                PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(getActivity(), PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the result is from paypal
        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.i("paymentExample", paymentDetails);

                        //Starting a new activity for the payment details and also putting the payment details with intent

//                               .putExtra("PaymentDetails", paymentDetails)
//                                .putExtra("PaymentAmount", paymentAmount));
                        JSONObject json = new JSONObject(paymentDetails);
                        json = json.getJSONObject("response");
                        Toast.makeText(getActivity(), "id: " + json.getString("id"), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), "state: " + json.getString("state"), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), "price: " + paymentAmount+" USD", Toast.LENGTH_SHORT).show();

                        randomOrder = UUID.randomUUID().toString();
                        makeNotification(randomOrder,json.getString("id"));
                        addOrder();


//                        textViewId.setText(jsonDetails.getString("id"));
//                        textViewStatus.setText(jsonDetails.getString("state"));
//                        textViewAmount.setText(paymentAmount+" USD");

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
                //dropLastOrder(0);
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    private void getCartTotal(){
        BigDecimal totalD = BigDecimal.ZERO;
        Cursor res = myDb.getAllData();
        while (res.moveToNext()) {

            String currentPrice = res.getString(res.getColumnIndexOrThrow("finalprice"));
            //change if for merch and season
            BigDecimal big = currency.getBigDecimal(currentPrice);
            totalD = totalD.add(big);


        }
        totalAMT =(currency.getBigDecimal(totalD.toString()).toString());
        total.setText("$"+currency.getMoneyString(totalD));
        Log.e(TAG, "Sent to Paypal: " + totalAMT + "Show to Customer: " + currency.getMoneyString(totalD));
    }

    private void addOrder(){
        final Cursor res = myDb.getAllData();
        user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //totalD = BigDecimal.ZERO;
        count = 0;



        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot
                        .child("order")
                        .child(user)
                        .getChildren()){
                    count++;
                    Log.e(TAG, "order" +  ds.getValue());

                }

                while (res.moveToNext()) {

                    String currentPrice = res.getString(res.getColumnIndexOrThrow("finalprice"));
                    //change if for merch and season
                    if(res.getString(res.getColumnIndexOrThrow("name")).equals("Single Game Ticket")){
                        String type = res.getString(res.getColumnIndexOrThrow("type"));
                        String[] typeNAME = type.split("\\s+");
                        type = typeNAME[0];
                        Items items = new Items(
                                res.getString(res.getColumnIndexOrThrow("name")),
                                ("Date: " + res.getString(res.getColumnIndexOrThrow("date")) + " Event: " + res.getString(res.getColumnIndexOrThrow("event")) + " - vs " + res.getString(res.getColumnIndexOrThrow("opponent"))),
                                res.getString(res.getColumnIndexOrThrow("finalprice")),
                                Integer.parseInt(res.getString(res.getColumnIndexOrThrow("quantity"))),
                                type,
                                res.getString(res.getColumnIndexOrThrow("location"))
                        );
                        Log.e(TAG, "totalPRICE*********: " + items);
                        myRef.child("order")
                                .child(user)
                                .child("order" + count).child("game" + games).setValue(items);
                        games++;
                    }
                    else if (res.getString(res.getColumnIndexOrThrow("name")).equals("Season Pass")){
                        Items items = new Items(
                                res.getString(res.getColumnIndexOrThrow("name")),
                                (res.getString(res.getColumnIndexOrThrow("event"))),
                                res.getString(res.getColumnIndexOrThrow("finalprice")),
                                Integer.parseInt(res.getString(res.getColumnIndexOrThrow("quantity"))),
                                res.getString(res.getColumnIndexOrThrow("type")),
                                "N/A"
                        );
                        Log.e(TAG, "totalPRICE*********: " + items);
                        myRef.child("order")
                                .child(user)
                                .child("order" + count).child("season" + season).setValue(items);
                        season++;
                    }
//                    BigDecimal big = currency.getBigDecimal(currentPrice);
//                    totalD = totalD.add(big);
//                    Log.e(TAG, "totalPRICE*********: " + totalD);



                }

                SimpleDateFormat formatter = new SimpleDateFormat();
                myRef.child("order")
                        .child(user)
                        .child("order" + count).child("total").setValue(total.getText().toString());
                myRef.child("order")
                        .child(user)
                        .child("order" + count).child("address").setValue(address.getText().toString()+ " " + city.getText().toString() +", " + state.getText().toString() + " " + zip.getText().toString());
                myRef.child("order")
                        .child(user)
                        .child("order" + count).child("date").setValue(formatter.format(Calendar.getInstance().getTime()));
                myRef.child("order")
                        .child(user)
                        .child("order" + count).child("coupon").setValue("0");
                myRef.child("order")
                        .child(user)
                        .child("order" + count).child("orderNUM").setValue(randomOrder);
                myRef.child("order")
                        .child(user)
                        .child("order" + count).child("confirm").setValue("1");

                orderNUM = "order" + count;
                myDb.deleteALL();
                // _id = res.getString(res.getColumnIndexOrThrow("_id"));

//                PaymentFragment fragment = new PaymentFragment();
//                Bundle args = new Bundle();
//                args.putString(getString(R.string.userID), user);
//                args.putString(getString(R.string.orderID), orderNUM);
//                //args.putString(getString(R.string.cartID), _id);
//                args.putString(getString(R.string.total), (currency.getBigDecimal(total.getText().toString())).toString());
//                fragment.setArguments(args);
//
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragContainer,fragment);
//                transaction.addToBackStack(getString(R.string.payFrag));
//                transaction.commit();
                startNewActivity();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });


        //adapter.changeCursor(res);
        //total.setText(totalD.toString());
    }

    private void makeNotification(String confirmation, String order){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity());
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle("Your Order Number Is: " + order);
        mBuilder.setContentText("Your Confirmation Number Is: " + confirmation);

        PendingIntent pi = PendingIntent.getActivity(getActivity(),0,new Intent(getActivity(), Tickets.class),0);

        mBuilder.setContentIntent(pi);

        int mNotificationId = 001;

        NotificationManager mgr = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);

        mgr.notify(mNotificationId, mBuilder.build());
    }

    private void startNewActivity(){
        startActivity(new Intent(getActivity(), Tickets.class));
        getActivity().finish();
    }


}

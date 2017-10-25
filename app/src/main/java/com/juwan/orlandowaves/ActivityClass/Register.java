package com.juwan.orlandowaves.ActivityClass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.juwan.orlandowaves.R;
import com.juwan.orlandowaves.toAccess.RegisterUserClass;
import com.juwan.orlandowaves.toAccess.users;


import java.util.HashMap;

import static android.content.ContentValues.TAG;
import static com.juwan.orlandowaves.R.id.phone;

public class Register extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextFname;
    private EditText editTextLname;
    private EditText editTextPassword;
    private EditText editTextEmail;
    private EditText editTextPhone;

    private String fName;
    private String lName;
    private  long phoneN;
    private String password;
    private  String email;
    private  String fullname;


    private Button buttonRegister;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

 private String userID;

    private static final String REGISTER_URL = "http://70.127.41.113/OrlandoWaves/register.php"; //change once AWS is up


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        editTextFname = (EditText) findViewById(R.id.fName);
        editTextLname = (EditText) findViewById(R.id.lName);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.pass);
        editTextPhone = (EditText) findViewById(phone);

        buttonRegister = (Button) findViewById(R.id.registerBTN);

        buttonRegister.setOnClickListener(Register.this);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        setupFirebaseAuth();
    }
    public void gotoHome(View view)
    {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonRegister){
            registerUser();
        }
    }

    private boolean isStringNull(String string){
        Log.d(TAG, "isStringNull: checking string if null.");

        if(string.equals("")){
            return true;
        }
        else{
            return false;
        }
    }

    private void registerUser() {
        fName = editTextFname.getText().toString();
        lName = editTextLname.getText().toString();
        phoneN = Long.parseLong(editTextPhone.getText().toString());
        password = editTextPassword.getText().toString();
        email = editTextEmail.getText().toString();
        fullname = fName + " " + lName;

        if(checkInputs(email, fullname,  password)){
           // mProgressBar.setVisibility(View.VISIBLE);
            //loadingPleaseWait.setVisibility(View.VISIBLE);


            registerNewEmail(email, fullname ,password);
            Log.d(TAG, "blah");
        }
    }

    private boolean checkInputs(String email, String password, String fullname){
        Log.d(TAG, "checkInputs: checking inputs for null values.");
        if(email.equals("") || fullname.equals("") || password.equals("")){
            Toast.makeText(this, "All fields must be filled out.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void registerNewEmail(final String email, final String fullname, String password){
        if(email.contains("@") == false){
            Toast.makeText(Register.this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
        }
        else if(password.length() < 6){
            Toast.makeText(Register.this, "Password Must Be ATLEAST 6 Characters", Toast.LENGTH_SHORT).show();
        }
        else{
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(Register.this, "Email Already In Use: Enter a Different VALID Email", Toast.LENGTH_SHORT).show();
                        }
                        else if(task.isSuccessful()){
                            userID = mAuth.getCurrentUser().getUid();
                            Log.d(TAG, "onComplete: Authstate changed: " + userID);
                        }

                    }
                });
        }
    }


    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
       // if(mAuth.getCurrentUser() == null){
        //    mAuth.signOut();
        //}

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                //Toast.makeText(Register.this, mAuth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //1st check: Make sure the username is not already in use

                            //add new user to the database
                            addNewUser(email, fullname, phone, "", "");

                            Toast.makeText(Register.this, "Signup successful. Sending verification email.", Toast.LENGTH_SHORT).show();

                                mAuth.signOut();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    done();
                    //checkCurrentUser(mAuth.getCurrentUser());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_outRR");
                }
                // ...
            }
        };
        //checkCurrentUser(mAuth.getCurrentUser());
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void checkCurrentUser(FirebaseUser user){
        if (user == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private void done(){
        Toast.makeText(Register.this, "Before Intent", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
        Toast.makeText(Register.this, "After Finish", Toast.LENGTH_SHORT).show();

    }

    public void addNewUser(String email, String fullname, long phone, String website, String profile_photo){

        users user = new users( userID,  phone,  email, fullname);

        myRef.child("user_accounts")
                .child(userID)
                .setValue(user);



    }
    //userid
    //0augth
    //0fbc
    //name
    //phone

}

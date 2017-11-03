package com.juwan.orlandowaves.ActivityClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.juwan.orlandowaves.R;
import com.juwan.orlandowaves.toAccess.Config;
import com.juwan.orlandowaves.toAccess.users;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
//Defining views
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private long fbc, auth;

    //boolean variable to check user is logged in or not
    //initially it is false
    private boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupFirebaseAuth();
        //Initializing views
        editTextEmail = (EditText) findViewById(R.id.email);

        editTextPassword = (EditText) findViewById(R.id.password);

        buttonLogin = (Button) findViewById(R.id.signin);


        //Adding click listener
       // buttonLogin.setOnClickListener(this,{});
        //Adding click listener
        buttonLogin.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "Resume", Toast.LENGTH_SHORT).show();
        //In onresume fetching value from sharedpreference
        mAuth.addAuthStateListener(mAuthListener);

        if(mAuth.getCurrentUser() != null){
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
            finish();
        }
        //checkCurrentUser(mAuth.getCurrentUser());
        //SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences

        //If we will get true
       // if(loggedIn){
         //   //We will start the Activity
         //   Intent intent = new Intent(this, Home.class);
        //    startActivity(intent);
        //}
    }

    public void gotoReg(View view)
    {
       Intent reg = new Intent(this, Register.class);
       startActivity(reg);
        finish();
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

    private void init(){
                Log.d(TAG, "onClick: attempting to log in.");

                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if(isStringNull(email) || isStringNull(password)){
                    Toast.makeText(this, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                }else {
                    // mProgressBar.setVisibility(View.VISIBLE);
                    // mPleaseWait.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Log.w(TAG, "signInWithEmail:failed", task.getException());

                                        Toast.makeText(LoginActivity.this, "fail",
                                                Toast.LENGTH_SHORT).show();
                                        // mProgressBar.setVisibility(View.GONE);
                                        //mPleaseWait.setVisibility(View.GONE);
                                    } else {
                                        Log.d(TAG, "signInWithEmail: successful login" + mAuth.getCurrentUser());
                                        Toast.makeText(LoginActivity.this, "success",
                                                Toast.LENGTH_SHORT).show();
                                        //mProgressBar.setVisibility(View.GONE);
                                        // mPleaseWait.setVisibility(View.GONE);
                                        SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
                                        DatabaseReference myRef = mFirebaseDatabase.getReference();

                                        Query query = myRef.child("user_accounts").child(mAuth.getCurrentUser().getUid());
                                        query.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                users user = dataSnapshot.getValue(users.class);
                                                fbc = user.getFbc();
                                                auth = user.getAuth();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                System.out.println("The read failed: " + databaseError.getCode());
                                            }
                                        });
                                        //Adding values to editor
                                        editor.putLong(Config.fbc, fbc);
                                        editor.putLong(Config.auth, auth);
                                        Intent intent = new Intent(LoginActivity.this, Home.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    // ...
                                }
                            });
                }

                }



    @Override
    public void onClick(View v) {
        //Calling the login function

        init();
    }

    private void checkCurrentUser(FirebaseUser user){
        if (user != null){
            Toast.makeText(this, "Before Home Intent", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
            finish();
        }
    }

    private void setupFirebaseAuth(){
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:LL" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_outLL");
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(this, "Login Started", Toast.LENGTH_SHORT).show();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}


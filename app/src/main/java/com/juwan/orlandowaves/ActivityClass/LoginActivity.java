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
import com.juwan.orlandowaves.R;
import com.juwan.orlandowaves.toAccess.Config;

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

    private void login(){
        //Getting values from edit texts
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server
                        //Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            //JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
                            //JSONObject ourData = jsonObject.getJSONObject;
                            String success = jsonObject.getString("success");
                            //Toast.makeText(LoginActivity.this, "cc", Toast.LENGTH_LONG).show();

                            if(success.trim().equalsIgnoreCase(Config.LOGIN_SUCCESS)){
                                //Creating a shared preference
                                final String fName  = jsonObject.getString("fName");
                                final String lName  = jsonObject.getString("lName");
                                final String fbc  = jsonObject.getString("fbc");
                                final String auth  = jsonObject.getString("auth");
                                SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                Toast.makeText(LoginActivity.this,  success , Toast.LENGTH_LONG).show();
                                //Creating editor to store values to shared preferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                //Adding values to editor
                                editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                                editor.putString(Config.EMAIL_SHARED_PREF, email);
                                editor.putString(Config.fName, fName);
                                editor.putString(Config.lName, lName);
                                editor.putString(Config.fbc, fbc);
                                editor.putString(Config.auth, auth);
                                Toast.makeText(LoginActivity.this, Config.fbc, Toast.LENGTH_LONG).show();
                                //Saving values to editor
                                editor.commit();

                                //Starting profile activity
                                Intent intent = new Intent(LoginActivity.this, Home.class);
                                startActivity(intent);
                            }
                            else{
                                //If the server response is not success
                                //Displaying an error message on toast
                                Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                            }   }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        Toast.makeText(LoginActivity.this, "err", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                params.put(Config.KEY_EMAIL, email);
                params.put(Config.KEY_PASSWORD, password);

                //returning parameter
                return params;
            }
        };//protected Map is the inside of string request function

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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


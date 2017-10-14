package com.example.juwan.orlandowaves.ActivityClass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
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
import com.example.juwan.orlandowaves.R;
import com.example.juwan.orlandowaves.toAccess.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.juwan.orlandowaves.toAccess.Config.LOGIN_SUCCESS;

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

    //boolean variable to check user is logged in or not
    //initially it is false
    private boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);

        //If we will get true
        if(loggedIn){
            //We will start the Activity
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
        }
    }

    public void gotoReg(View view)
    {
       Intent reg = new Intent(this, Register.class);
       startActivity(reg);
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

                            if(success.trim().equalsIgnoreCase(LOGIN_SUCCESS)){
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

    @Override
    public void onClick(View v) {
        //Calling the login function

        login();
    }

}


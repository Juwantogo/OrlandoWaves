package com.example.juwan.orlandowaves.ActivityClass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.juwan.orlandowaves.R;
import com.example.juwan.orlandowaves.toAccess.RegisterUserClass;

import java.util.HashMap;

public class Register extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextFname;
    private EditText editTextLname;
    private EditText editTextPassword;
    private EditText editTextEmail;
    private EditText editTextPhone;

    private Button buttonRegister;

    private static final String REGISTER_URL = "http://70.127.41.113/OrlandoWaves/register.php"; //change once AWS is up


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextFname = (EditText) findViewById(R.id.fName);
        editTextLname = (EditText) findViewById(R.id.lName);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.pass);
        editTextPhone = (EditText) findViewById(R.id.phone);

        buttonRegister = (Button) findViewById(R.id.registerBTN);

        buttonRegister.setOnClickListener(Register.this);
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

    private void registerUser() {
        String fName = editTextFname.getText().toString().trim();
        String lName = editTextLname.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim().toLowerCase();

        register(fName,lName,phone,password,email);
    }

    private void register(String CUS_FNAME, String CUS_LNAME, String CUS_PHONE, String CUS_PASS, String CUS_EMAIL) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Register.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s.trim().trim().equalsIgnoreCase("successfully registered")){
                    Toast.makeText(getApplicationContext(),"Thanks For Registering With Orlando Waves!",Toast.LENGTH_LONG).show();
                    Intent ordering = new Intent(Register.this, LoginActivity.class);
                    ordering.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(ordering);
                }
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("CUS_FNAME",params[0]);
                data.put("CUS_LNAME",params[1]);
                data.put("CUS_EMAIL",params[2]);
                data.put("CUS_PASS",params[3]);
                data.put("CUS_PHONE",params[4]);

                String result = ruc.sendPostRequest(REGISTER_URL,data);

                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(CUS_FNAME,CUS_LNAME,CUS_EMAIL,CUS_PASS,CUS_PHONE);
    }
}

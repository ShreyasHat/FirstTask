package com.example.firsttask;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private static final String URL_REGIST ="http://192.168.10.38/android_login_register/register.php";
    EditText first_nameE, last_nameE, emailE, passE, conPassE;
    Button registerE;
    TextView log_viewE;
    boolean isEmailValid, isPasswordValid, isConfirmPasswordValid, isFirstnameValid, isLastnameValid;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        first_nameE = findViewById(R.id.first_name);
        last_nameE = findViewById(R.id.last_name);
        emailE = findViewById(R.id.email);
        passE = findViewById(R.id.password);
        conPassE = findViewById(R.id.confirm_password);
        registerE = findViewById(R.id.reg_buttn);
        log_viewE = findViewById(R.id.log_in);
        builder=new AlertDialog.Builder(RegisterActivity.this);
        log_viewE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
        registerE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetValidation();
            }
        });
    }

    public void SetValidation() {
        if (first_nameE.getText().toString().isEmpty()) {
            first_nameE.setError("Enter First Name");
            isFirstnameValid = false;
        } else if (!first_nameE.getText().toString().matches("[a-zA-Z ]+")) {
            first_nameE.setError("First Name is not valid");
        } else {
            isFirstnameValid = true;
        }
        if (last_nameE.getText().toString().isEmpty()) {
            last_nameE.setError("Enter First Name");
            isLastnameValid = false;
        } else if (!last_nameE.getText().toString().matches("[a-zA-Z ]+")) {
            last_nameE.setError("Last Name is not valid");
            isLastnameValid = false;
        } else {
            isLastnameValid = true;
        }
        if (emailE.getText().toString().isEmpty()) {
            emailE.setError("Enter Email");
            isEmailValid = false;
        } else if (!emailE.getText().toString().matches(emailPattern)) {
            emailE.setError("Email is not valid");
            isEmailValid = false;
        } else {
            isEmailValid = true;
        }
        if (passE.getText().toString().isEmpty()) {
            passE.setError("Enter the Password");
            isPasswordValid = false;
        } else if (passE.getText().length() < 6) {
            passE.setError("Please enter a minimum password of 6 characters");
            isPasswordValid = false;
        } else {
            isPasswordValid = true;
        }
        if (conPassE.getText().toString().isEmpty()) {
            conPassE.setError("Enter the Password");
            isConfirmPasswordValid = false;
        } else if (!conPassE.getText().toString().equals(passE.getText().toString())) {
            conPassE.setError("Password is not matching");
            isConfirmPasswordValid = false;
        } else  {
            isConfirmPasswordValid = true;
        }
        if (isEmailValid== true && isPasswordValid==true && isFirstnameValid==true && isLastnameValid==true && isConfirmPasswordValid==true) {
            Register();
        }
    }

    private void Register() {
        final String first_name=first_nameE.getText().toString();
        final String last_name=last_nameE.getText().toString();
        final String email=emailE.getText().toString();
        final String password=passE.getText().toString();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_REGIST,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String code=jsonObject.getString("code");
                    String message = jsonObject.getString("message");
                    if(code.equals("success")){
                        Toast.makeText(RegisterActivity.this,message,Toast.LENGTH_SHORT).show();
                        first_nameE.setText("");
                        last_nameE.setText("");
                        emailE.setText("");
                        passE.setText("");
                        conPassE.setText("");
                    }
                    else if(code.equals("failed")){
                        Toast.makeText(RegisterActivity.this,message,Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RegisterActivity.this, "Registration fail", Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this,"Register error!"+error.toString(),Toast.LENGTH_SHORT);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("first_name",first_name);
                params.put("last_name",last_name);
                params.put("email",email);
                params.put("password",password);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
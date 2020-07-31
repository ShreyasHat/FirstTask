package com.example.firsttask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
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

public class LoginActivity extends AppCompatActivity {
    EditText emailE,passE;
    TextView reg_viewE;
    Button loginE;
    boolean isEmailValid,isPasswordValid;
    private static final String URL_REGIST ="http://192.168.10.38/android_login_register/login.php";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailE=findViewById(R.id.email);
        passE=findViewById(R.id.password);
        loginE=findViewById(R.id.login_button);
        reg_viewE=findViewById(R.id.reg_view);
        loginE.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                    SetValidation();
            }
        });
        reg_viewE.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

    }


    public void SetValidation() {
        // Check for a valid email address.

        if (emailE.getText().toString().isEmpty()) {
            emailE.setError("Enter Email");
            isEmailValid = false;
        } else if (!emailE.getText().toString().matches(emailPattern)) {
            emailE.setError("Email is not valid");
            isEmailValid = false;
        } else  {
            isEmailValid = true;
        }

        // Check for a valid password.
        if (passE.getText().toString().isEmpty()) {
            passE.setError("Enter the Password");
            isPasswordValid = false;
        } else if (passE.getText().length() < 6) {
            passE.setError("Please enter a minimum password of 6 characters");
            isPasswordValid = false;
        } else  {
            isPasswordValid = true;
        }

        if (isEmailValid ==true && isPasswordValid==true) {
            login();
        }

    }

    public void login() {
        final String email=emailE.getText().toString().trim();
        final String password=passE.getText().toString().trim();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_REGIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        System.out.println(s);
                        try {
                            JSONArray jsonArray=new JSONArray(s);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code=jsonObject.getString("code");
                            String message = jsonObject.getString("message");

                            if (code.equals("success")) {
                                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }
                            else {
                                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,"Login Failed!"+error.toString(),Toast.LENGTH_SHORT);
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String>params=new HashMap<>();
                        params.put("email",email);
                        params.put("password",password);
                        return params;
                    }
                };
                RequestQueue requestQueue= Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);

    }
}
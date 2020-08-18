package com.example.gestionintervention;

import android.app.ProgressDialog;

import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;


import java.io.UnsupportedEncodingException;

import classes.User;
import classes.Utility;
import cz.msebera.android.httpclient.Header;

public class FirstFragment extends Fragment {
    ProgressDialog prgDialog;

    private String userName, email, password;
    EditText userNameInput, passwordInput, emailInput;
    Button registerUserButton;
    TextView linkToLogin;
    private View v;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
         v = inflater.inflate(com.example.gestionintervention.R.layout.fragment_first, container, false);

        return v;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        userNameInput = (EditText) v.findViewById(com.example.gestionintervention.R.id.userName);
        passwordInput = (EditText) v.findViewById(com.example.gestionintervention.R.id.password);
        emailInput = (EditText) v.findViewById(com.example.gestionintervention.R.id.email);
        prgDialog = new ProgressDialog(this.getContext());
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);
        registerUserButton = (Button) v.findViewById(com.example.gestionintervention.R.id.regesterUser);
        registerUserButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                userName = userNameInput.getText().toString();
                password = passwordInput.getText().toString();
                email = emailInput.getText().toString();
                registerUser(view);
                /*if (!User.userExists(user)) {
                    User.addUser(user);

                    System.out.println("New User Regestered with success;---------- " + userName);
                    userNameInput.setText("");
                    passwordInput.setText("");
                    emailInput.setText("");
                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_FirstFragment_to_SecondFragment);
                }
                else{
                    System.out.println("Username or email already exist---------------------------" );
                     TextView tv= v.findViewById(R.id.regesterError);
                    userNameInput.setText("");
                    passwordInput.setText("");
                    emailInput.setText("");
                     tv.setText("nom d\'utilisateur ou email déjà existant.");

                }*/
            }
        });
       /* view.findViewById(R.id.regesterUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });*/

        linkToLogin = (TextView) v.findViewById(com.example.gestionintervention.R.id.linkToLogin);
        linkToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("The user already have an account ");

                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(com.example.gestionintervention.R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }
    public void registerUser(View view){
        userName = userNameInput.getText().toString();
        password = passwordInput.getText().toString();
        email = emailInput.getText().toString();
        User user = new User(userName,password,email);
        RequestParams params = new RequestParams();
        if (Utility.isNotNull(userName)  && Utility.isNotNull(password) && Utility.isNotNull(email)){
            if (Utility.validate(email)){
                params.put("username", userName);
                params.put("password", password);
                params.put("email",email);
            System.out.println(params.toString());
                invokeWS(params);
            }else{
                Toast.makeText(this.getContext(), "Veuillez entrer un email valide", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this.getContext(), "Veuillez remplir les deux champs.", Toast.LENGTH_LONG).show();
        }

    }

    private void invokeWS(RequestParams params) {
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();

        client.post("http://192.168.8.132:8086/register/user",params ,new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                prgDialog.hide();
                try{
                    System.out.println(statusCode ) ;
                    String text = new String(responseBody, "UTF-8");
                    System.out.println(text) ;
                    JSONArray obj = new JSONArray(text);
                    if (statusCode == 200){  //statusCode == 200
                        Toast.makeText(FirstFragment.this.getContext(), "Your account was created successfully ", Toast.LENGTH_LONG).show();
                        NavHostFragment.findNavController(FirstFragment.this)
                                .navigate(com.example.gestionintervention.R.id.action_FirstFragment_to_SecondFragment);

                    }

                }catch (UnsupportedEncodingException | JSONException e){
                    Toast.makeText(FirstFragment.this.getContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                prgDialog.hide();
                if(statusCode == 404){
                    Toast.makeText(FirstFragment.this.getContext(), "Username already exists. Try another one", Toast.LENGTH_LONG).show();

                }else if(statusCode == 500){
                    Toast.makeText(FirstFragment.this.getContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(FirstFragment.this.getContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }

            }
        });

    }






}
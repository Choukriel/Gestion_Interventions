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



import java.io.UnsupportedEncodingException;

import classes.Utility;
import cz.msebera.android.httpclient.Header;

public class SecondFragment extends Fragment {

    private String username,password;
     EditText usernameLogin, passwordLogin;
    Button loginButton;
    ProgressDialog prgDialog;

    private View v;
    private   TextView tv;


    public SecondFragment(){

    }
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        System.out.println("Creating the view ----------------------");


        v = inflater.inflate(com.example.gestionintervention.R.layout.fragment_second, container, false);

        return v;
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        System.out.println("View created successfully ----------------------");
        usernameLogin = v.findViewById(com.example.gestionintervention.R.id.usernameLogin);
        passwordLogin = v.findViewById(com.example.gestionintervention.R.id.passwordLogin);
        loginButton = v.findViewById(com.example.gestionintervention.R.id.login);
        prgDialog = new ProgressDialog(this.getContext());
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                loginUser(view);



            }
    });


}
    public void loginUser(View view) {

        username = usernameLogin.getText().toString();
        password = passwordLogin.getText().toString();

        RequestParams params = new RequestParams();
        if(Utility.isNotNull(username) && Utility.isNotNull(password)) {
            params.put("username", username);
            params.put("password", password);

            invokeWS(params);

        } else{
            Toast.makeText(this.getContext(), "Veuillez remplir les deux champs.", Toast.LENGTH_LONG).show();

        }
    }

    private void invokeWS(RequestParams params) {

        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
         String url = "http://192.168.8.132:8086/login/user/"+username+"&"+password;
        client.get(url,params ,new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[]  responseBody) {
                prgDialog.hide();
                try{
                    System.out.println(statusCode ) ;
                    String text = new String(responseBody, "UTF-8");
                    System.out.println(text ) ;
                    //JSONObject obj = new JSONObject(text);
                    if (statusCode == 200 ){


                        Toast.makeText(SecondFragment.this.getContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();


                        Bundle bundle = new Bundle();
                        bundle.putString("username",username);

                        NavHostFragment.findNavController(SecondFragment.this)
                                .navigate(com.example.gestionintervention.R.id.action_LoginFragment_to_InterventionsFragment,bundle );
                    }

                }catch ( UnsupportedEncodingException e){
                    Toast.makeText(SecondFragment.this.getContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                prgDialog.hide();

                if(statusCode == 404){
                    System.out.println("the User name or password incorrect");
                    usernameLogin.setText("");
                    passwordLogin.setText("");
                    tv = v.findViewById(com.example.gestionintervention.R.id.errorMessage);
                    tv.setText(com.example.gestionintervention.R.string.loginFailedMsg);
                    Toast.makeText(SecondFragment.this.getContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                else if(statusCode == 500){
                    Toast.makeText(SecondFragment.this.getContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                 else{
                    Toast.makeText(SecondFragment.this.getContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }

        });
        }




}




package com.example.gestionintervention;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import classes.Intervention;
import classes.Utility;
import cz.msebera.android.httpclient.Header;


import static android.content.Context.LOCATION_SERVICE;


public class InterventionsFragment extends Fragment  {
    ProgressDialog prgDialog;
    private View v;
    private Button getLocButton,insertData,myinterventions;
    String nomTechText,simText,lvcanText,psnText,matriculeText,currLocText,username;
    private TextView nomTech,sim,lvcan,psn,matricule,currLoc;
    private Context mContext;
    private LocationManager locationManager;
    double longitude,latitude;
    Bundle bundle;
    LocationListener locationListener;
    private static ArrayList<Intervention> interventionArray;
    private Location location;

    public static ArrayList<Intervention> getInterventionArray() {

        return interventionArray;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        System.out.println("Creating the view ----------------------");
        mContext = this.getContext();
         bundle = getArguments();
        if (bundle != null){
            username = bundle.getString("username");

        }
        v = inflater.inflate(com.example.gestionintervention.R.layout.fragment_interventions, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        System.out.println("Interventions' View created succefully -----------");
        System.out.println(username);
        psn = v.findViewById(com.example.gestionintervention.R.id.psn);
        matricule = v.findViewById(com.example.gestionintervention.R.id.matricule);
        lvcan = v.findViewById(com.example.gestionintervention.R.id.lvcan);
        sim = v.findViewById(com.example.gestionintervention.R.id.sim);
        nomTech = v.findViewById(com.example.gestionintervention.R.id.nomTechnicien);
        getLocButton = v.findViewById(com.example.gestionintervention.R.id.getLocationButton);
        currLoc = v.findViewById(com.example.gestionintervention.R.id.currLocation);
        insertData = v.findViewById(com.example.gestionintervention.R.id.insertDataButton);
        myinterventions = v.findViewById(com.example.gestionintervention.R.id.myInterventions);
        prgDialog = new ProgressDialog(mContext);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        // To turn on location service

        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onLocationChanged(Location location) {
                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);

                if ( location!= null ){
                 longitude=location.getLongitude();
                 latitude=location.getLatitude();
                 currLoc.setText(getAddressDetails(longitude,latitude));

                }

                System.out.println(longitude  + " -----------  " + latitude);


            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }
            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET}, 10);
                return;
            }
        }

        // update the location every 5 seconds
         locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        getLocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationListener.onLocationChanged(location);
                getAddressDetails(longitude,latitude);
            }
        });
        //Add or update intervention
        insertData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOrUpdateIntervention(view);
            }
        });


        // show list of interventions
        myinterventions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserInterventions(view);

            }
        });
        if (bundle.containsKey("psn")){
            psn.setText(bundle.getString("psn"));
            psn.setEnabled(false);
        }
    }
// get all interventions of user
    private void getUserInterventions(View view) {
        prgDialog.show();
        RequestParams params= new RequestParams();
        params.put("username",username);
        params.setContentEncoding("UTF-8");
        AsyncHttpClient client = new AsyncHttpClient();

         client.addHeader("Accept", "application/json");
         client.addHeader("Content-type", "application/json;charset=utf-8");

        client.get("http://192.168.8.132:8086/intervention/allinterventions", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                prgDialog.hide();
                try{
                    System.out.println(statusCode ) ;
                    String text = new String(responseBody, "UTF-8");
                    System.out.println(text) ;
                    JSONArray obj = new JSONArray(text);
                    Gson gson = new Gson();
                    Type interventionListType = new TypeToken<ArrayList<Intervention>>(){}.getType();
                    interventionArray = gson.fromJson(text,interventionListType);
                    for (Intervention i: interventionArray){
                        System.out.println(i.toString());
                    }
                    if (statusCode == 200){  //statusCode == 200
                        Toast.makeText(InterventionsFragment.this.getContext(), "List of Interventions" , Toast.LENGTH_LONG).show();
                        NavHostFragment.findNavController(InterventionsFragment.this)
                                .navigate(com.example.gestionintervention.R.id.action_InterventionFrag_to_userInteventionsFragment, bundle);
                    }

                }catch (UnsupportedEncodingException | JSONException e){
                    Toast.makeText(InterventionsFragment.this.getContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                prgDialog.hide();

                if(statusCode == 404){
                    System.out.println("Something went wrong");
                    psn.setText("");
                    matricule.setText("");
                    lvcan.setText("");
                    sim.setText("");
                    nomTech.setText("");
                    currLoc.setText("");

                    Toast.makeText(InterventionsFragment.this.getContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                else if(statusCode == 500){
                    Toast.makeText(InterventionsFragment.this.getContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(InterventionsFragment.this.getContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }

        });
    }


    public void addOrUpdateIntervention(View view){
        psnText = psn.getText().toString();
        matriculeText = matricule.getText().toString();
        lvcanText = lvcan.getText().toString();
        simText = sim.getText().toString();
        nomTechText = nomTech.getText().toString();
        currLocText = currLoc.getText().toString();
        RequestParams params = new RequestParams();
        if (Utility.isNotNull(psnText)  && Utility.isNotNull(matriculeText) && Utility.isNotNull(lvcanText) && Utility.isNotNull(simText)
                && Utility.isNotNull(nomTechText) && Utility.isNotNull(currLocText)) {
            params.put("psn",psnText);
            params.put("matricule",matriculeText);
            params.put("lvcan",lvcanText);
            params.put("sim",simText);
            params.put("tech_name",nomTechText);
            params.put("current_location",currLocText);
            params.put("username",username);
            System.out.println(params.toString());
            invokeWS(params);
        }else{
            Toast.makeText(this.getContext(), "Veuillez remplir tous les champs.", Toast.LENGTH_LONG).show();
        }
        }

        //invocation of Web Services
    private void invokeWS(RequestParams params) {
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://192.168.8.132:8086/create_or_update/intervention", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                bundle.remove("psn");
                psn.setEnabled(true);
                psn.setText("");
                prgDialog.hide();
                try{
                    System.out.println(statusCode ) ;
                    String text = new String(responseBody, "UTF-8");
                    System.out.println(text) ;
                    JSONArray obj = new JSONArray(text);
                    if (statusCode == 200){  //statusCode == 200
                        Toast.makeText(InterventionsFragment.this.getContext(), "Votre intervention a été ajouté avec succés. ", Toast.LENGTH_LONG).show();
                        NavHostFragment.findNavController(InterventionsFragment.this)
                                .navigate(com.example.gestionintervention.R.id.action_InterventionFrag_self, bundle);

                    }

                }catch (UnsupportedEncodingException | JSONException e){
                    Toast.makeText(InterventionsFragment.this.getContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                prgDialog.hide();

                if(statusCode == 404){
                    System.out.println("the User name or password incorrect");
                    psn.setText("");
                    matricule.setText("");
                    lvcan.setText("");
                    sim.setText("");
                    nomTech.setText("");
                    currLoc.setText("");

                    Toast.makeText(InterventionsFragment.this.getContext(), "No, d\'utilisateur ou mot de passe incorrecte", Toast.LENGTH_LONG).show();
                }
                else if(statusCode == 500){
                    Toast.makeText(InterventionsFragment.this.getContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(InterventionsFragment.this.getContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }

        });
    }


    // get the details of the location using longitude and latitude

    public String getAddressDetails(double lon, double lat)  {
        String currAddress="";
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this.getContext(), Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);

            String address = addresses.get(0).getAddressLine(0);


            System.out.println(address);
            currAddress = address;
        }catch (IOException e){
            e.printStackTrace();

        }

     return currAddress;
    }


}

package com.example.gestionintervention;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import androidx.fragment.app.ListFragment;
import androidx.navigation.fragment.NavHostFragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;

import classes.Intervention;

import cz.msebera.android.httpclient.Header;



public class UserInteventionsFragment extends ListFragment {
    ProgressDialog prgDialog;
    View v;
    Bundle bundle;
    String username;
    ListView listView;
    List<Intervention> interventionArray;
    private String psnOfSelectedItem;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        System.out.println("Creating the view ----------------------");

        v = inflater.inflate(com.example.gestionintervention.R.layout.fragment_interventions_user, container, false);




        listView =  v.findViewById(android.R.id.list);

            interventionArray =InterventionsFragment.getInterventionArray();



        ArrayAdapter<Intervention>  listViewAdapter = new ArrayAdapter<Intervention>(
                getActivity(), android.R.layout.simple_list_item_1,
                interventionArray
        );
        listView.setAdapter(listViewAdapter);

        //get the username i.e ID of the user
        bundle = getArguments();
        username = bundle.getString("username");

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        System.out.println("View created successfully ---------------------- Username : " +username);



      (v.findViewById(com.example.gestionintervention.R.id.addIntervention)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.remove("psn");
                
                NavHostFragment.findNavController(UserInteventionsFragment.this)
                        .navigate(com.example.gestionintervention.R.id.action_userInteventionsFragment_to_InterventionFrag, bundle);
            }
        });


        prgDialog = new ProgressDialog(this.getContext());
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {




                    View popupView = LayoutInflater.from(UserInteventionsFragment.this.getContext()).inflate(com.example.gestionintervention.R.layout.fragment_interventions_user, null);
                    final PopupWindow popupWindow = new PopupWindow(popupView,
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);


                    popupWindow.setFocusable(true);

                    popupWindow.setOutsideTouchable(true);

                    popupWindow.update();
                    final AlertDialog alertDialog = new AlertDialog.Builder(UserInteventionsFragment.this.getContext()).create();
                    alertDialog.setTitle("Selected Intervention");
                    psnOfSelectedItem = interventionArray.get(i).getPsn();
                    alertDialog.setMessage("You select intervention " + interventionArray.get(i).toString());
                    alertDialog.setButton(Dialog.BUTTON_NEGATIVE,"SUPPRIMER",new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            deleteIntervention(psnOfSelectedItem);
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.setButton(Dialog.BUTTON_POSITIVE, "MODIFIER", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            updateIntervention(psnOfSelectedItem);
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            });

    }

    private void updateIntervention(String psnOfSelectedItem) {
        bundle.putString("psn",psnOfSelectedItem);
        NavHostFragment.findNavController(UserInteventionsFragment.this)
                .navigate(com.example.gestionintervention.R.id.action_userInteventionsFragment_to_InterventionFrag, bundle);
    }

    private void deleteIntervention(String psn) {
        prgDialog.show();
        RequestParams params= new RequestParams();
        params.put("psn",psn);
        params.setContentEncoding("UTF-8");
        AsyncHttpClient client = new AsyncHttpClient();

        client.addHeader("Accept", "application/json");
        client.addHeader("Content-type", "application/json;charset=utf-8");

        // http://domainname:port/uri  , since I am working in a Local Area Network - My Computer is the Server -  I put my ip address
        client.delete("http://192.168.8.132:8086/delete/intervention", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            prgDialog.hide();
                if (statusCode == 200){  //statusCode == 200
                    Toast.makeText(UserInteventionsFragment.this.getContext(), "Intervention a été supprimée avec succés" , Toast.LENGTH_LONG).show();
                    NavHostFragment.findNavController(UserInteventionsFragment.this)
                            .navigate(com.example.gestionintervention.R.id.action_userInteventionsFragment_to_InterventionFrag, bundle);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                prgDialog.hide();

                if(statusCode == 404){
                    System.out.println("Error 404" + error.getMessage());


                    Toast.makeText(UserInteventionsFragment.this.getContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                else if(statusCode == 500){
                    Toast.makeText(UserInteventionsFragment.this.getContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(UserInteventionsFragment.this.getContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }

        });
    }



}

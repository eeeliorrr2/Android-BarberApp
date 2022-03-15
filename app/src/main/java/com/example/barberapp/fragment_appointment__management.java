package com.example.barberapp;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_appointment__management#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_appointment__management extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView mlistView;
    private DatabaseReference barbersRef;
    private DatabaseReference customersRef;
    String barberPhone;
    String customerPhone;
    String curDateTime;
    boolean isCustomer;
    String userEmail;
    ArrayList<String> app;

    public fragment_appointment__management() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_appointment__management.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_appointment__management newInstance(String param1, String param2) {
        fragment_appointment__management fragment = new fragment_appointment__management();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewAppManagment =  inflater.inflate(R.layout.fragment_appointment__management, container, false);
        mlistView = viewAppManagment.findViewById(R.id.appManagmentListview);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        barbersRef = database.getReference("Barbers");
        customersRef = database.getReference("Customer");
        SharedPreferences pref1 = this.getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);// 0 - for private mode
        userEmail = pref1.getString("userEmail", "");
        app = new ArrayList<>();
        //String barber;
        barbersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        if(dataSnapshot1.child("email").getValue(String.class).equals(userEmail))
                        {
                            barberPhone = dataSnapshot1.getKey();
                            if(dataSnapshot1.child("Appointments").exists()) {
                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("Appointments").getChildren()) {
                                    LocalDateTime now = LocalDateTime.now();
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss d-L-yyyy");
                                    LocalDateTime appDateTime = (LocalDateTime.parse(dataSnapshot2.getKey(), formatter));
                                    if (appDateTime.isBefore(now)) {
                                        DatabaseReference temp = dataSnapshot2.getRef();
                                        temp.removeValue();
                                    } else {
                                        app.add(dataSnapshot2.getKey());
                                        isCustomer = false;
                                    }
                                }
                                ArrayAdapter adapter = new ArrayAdapter(getContext().getApplicationContext(), android.R.layout.simple_list_item_1, app);
                                mlistView.setAdapter(adapter);
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "there are no appointments.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        customersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        if(dataSnapshot1.child("email").getValue(String.class).equals(userEmail))
                        {
                            customerPhone = dataSnapshot1.getKey();
                            if(dataSnapshot1.child("Appointments").exists()) {
                                app.clear();
                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("Appointments").getChildren()) {
                                    LocalDateTime now = LocalDateTime.now();
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss d-L-yyyy");
                                    LocalDateTime appDateTime = (LocalDateTime.parse(dataSnapshot2.getKey(), formatter));
                                    if (appDateTime.isBefore(now)) {
                                        DatabaseReference temp = dataSnapshot2.getRef();
                                        temp.removeValue();
                                    } else {
                                        app.add(dataSnapshot2.getKey());
                                        isCustomer = true;
                                    }


                                }
                                ArrayAdapter adapter = new ArrayAdapter(getContext().getApplicationContext(), android.R.layout.simple_list_item_1, app);
                                mlistView.setAdapter(adapter);
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "there are no appointments.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                customersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(isCustomer) {
                                            curDateTime = parent.getItemAtPosition(position).toString();
                                            String barberPhoneTemp = dataSnapshot.child(customerPhone).child("Appointments").child(curDateTime).child("phoneNumber").getValue(String.class);
                                            customersRef.child(customerPhone).child("Appointments").child(curDateTime).removeValue();
                                            barbersRef.child(barberPhoneTemp).child("Appointments").child(curDateTime).removeValue();
                                            Toast.makeText(getActivity(), "Appointment been canceled.", Toast.LENGTH_SHORT).show();
                                            Navigation.findNavController(viewAppManagment).navigate((R.id.action_fragment_appointment__management_to_fragment_User_Actions));


                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                barbersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(!isCustomer) {
                                            String customerPhoneTemp = dataSnapshot.child(barberPhone).child("Appointments").child(curDateTime).child("phoneNumber").getValue(String.class);
                                            customersRef.child(customerPhoneTemp).child("Appointments").child(curDateTime).removeValue();
                                            barbersRef.child(barberPhone).child("Appointments").child(curDateTime).removeValue();
                                            Toast.makeText(getActivity(), "Appointment been canceled.", Toast.LENGTH_SHORT).show();
                                            Navigation.findNavController(viewAppManagment).navigate((R.id.action_fragment_appointment__management_to_fragment_User_Actions));


                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to cancel appointment?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        return viewAppManagment;
    }

}
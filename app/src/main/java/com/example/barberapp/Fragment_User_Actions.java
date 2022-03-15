package com.example.barberapp;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_User_Actions#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_User_Actions extends Fragment {
    //SharedPreferences pref;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatabaseReference barbersActionRef;
    private DatabaseReference customersActionRef;


    public Fragment_User_Actions() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_User_Actions.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_User_Actions newInstance(String param1, String param2) {
        Fragment_User_Actions fragment = new Fragment_User_Actions();
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

    void checkUserType (String email, View currentView){


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        barbersActionRef = database.getReference("Barbers");
        customersActionRef = database.getReference("Customer");

        barbersActionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        if(dataSnapshot1.child("email").getValue(String.class).equals(email))
                        {
                            Navigation.findNavController(currentView).navigate(R.id.action_fragment_User_Actions_to_barberAppointmentManagment);
                        }
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        customersActionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        if(dataSnapshot1.child("email").getValue(String.class).equals(email))
                        {
                            Navigation.findNavController(currentView).navigate(R.id.action_fragment_User_Actions_to_fragment_appointment__management);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View userActions_view = inflater.inflate(R.layout.fragment__user__actions, container, false);
        EditText tfBarberPhoneNumber = userActions_view.findViewById(R.id.tfBarberPhoneNumber);
        SharedPreferences pref = this.getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);// 0 - for private mode
        String barberPhoneNumber = pref.getString("barberPhoneNumber", "");
        SharedPreferences pref1 = this.getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);// 0 - for private mode
        String userEmail = pref1.getString("userEmail", "");


        tfBarberPhoneNumber.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_UP == event.getAction())
                    tfBarberPhoneNumber.setText("");

                return false;
            }
        });

        Button appointmentButton = userActions_view.findViewById(R.id.buttonAddAppointment);
        appointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tfBarberPhoneNumber.getText().toString().equals("")|| tfBarberPhoneNumber.getText().toString().equals("Phone number here"))
                {
                    Toast.makeText(getActivity(), "You have to fill barber number", Toast.LENGTH_SHORT).show();
                }
                else if (tfBarberPhoneNumber.getText().toString().length()<9){
                    Toast.makeText(getActivity(), "Barber number is too short", Toast.LENGTH_SHORT).show();
                }
                else if (tfBarberPhoneNumber.getText().toString().length()>10){
                    Toast.makeText(getActivity(), "Barber number is too long", Toast.LENGTH_SHORT).show();
                }
                else {
                    Navigation.findNavController(userActions_view).navigate((R.id.action_fragment_User_Actions_to_appointmentFragment2));
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("barberPhoneNumber", tfBarberPhoneNumber.getText().toString());
                    editor.commit();
                }
            }
        });

        Button manageAppointmentsButton = userActions_view.findViewById(R.id.ButtonManageAppointments);
        manageAppointmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View userActions_view) {
                checkUserType(userEmail,userActions_view);
            }
        });

        return userActions_view;
    }



}

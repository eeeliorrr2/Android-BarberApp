package com.example.barberapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BarberAppointmentManagment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BarberAppointmentManagment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ListView barberlistView;
    private DatabaseReference barbersAppRef;
    private DatabaseReference customersAppRef;
    private DatabaseReference barberDaysOffRef;
    String barberAppPhone;
    String customerAppPhone;
    String curAppDateTime;
    DatePickerDialog picker;
    String mdayOff;
    String userEmail;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BarberAppointmentManagment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BarberAppointmentManagment.
     */
    // TODO: Rename and change types and number of parameters
    public static BarberAppointmentManagment newInstance(String param1, String param2) {
        BarberAppointmentManagment fragment = new BarberAppointmentManagment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    ToggleButton tS;
    ToggleButton tM;
    ToggleButton tT;
    ToggleButton tW;
    ToggleButton tTi;
    ToggleButton tF;
    ToggleButton tSi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    void setDayOff(String barberPhoneNumber) {


        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mdayOff = (String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(year));
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                barberDaysOffRef = database.getReference("Barbers/"+barberPhoneNumber);
                barberDaysOffRef.child("daysOff").child(mdayOff).setValue(mdayOff);
                Toast.makeText(getActivity(), "Off day has been added.", Toast.LENGTH_SHORT).show();

            }
            }, year, month, day);
        picker.show();
    }



    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Fragment_Register newFragmentRegister = new Fragment_Register();

        View barber_appointment_managment_view = inflater.inflate(R.layout.fragment_barber_appointment_managment, container, false);
        EditText start =  barber_appointment_managment_view.findViewById(R.id.pickStartTimeButton);
        EditText end = barber_appointment_managment_view.findViewById(R.id.pickEndTimeButton);
        SharedPreferences pref = this.getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);// 0 - for private mode
        String barberPhoneNumber = pref.getString("barberPhoneNumber", "");

        Button setDatebtn = barber_appointment_managment_view.findViewById(R.id.pickDateBtn);
        setDatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDayOff(barberAppPhone);
            }
        });

        Button changeWorkDays = barber_appointment_managment_view.findViewById(R.id.buttonChangeWorkDays);
        tS = barber_appointment_managment_view.findViewById(R.id.tS);
        tM = barber_appointment_managment_view.findViewById(R.id.tM);
        tT = barber_appointment_managment_view.findViewById(R.id.tT);
        tW = barber_appointment_managment_view.findViewById(R.id.tW);
        tTi = barber_appointment_managment_view.findViewById(R.id.tTi);
        tF = barber_appointment_managment_view.findViewById(R.id.tF);
        tSi = barber_appointment_managment_view.findViewById(R.id.tSi);

        changeWorkDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String days = newFragmentRegister.daysOfWork(tS.isChecked(),tM.isChecked(),tT.isChecked(),tW.isChecked(),tTi.isChecked(),tF.isChecked(),tSi.isChecked());
                barbersAppRef.child(barberAppPhone).child("workDays").setValue(days);
                Toast.makeText(getActivity(), "Work days have been changed.", Toast.LENGTH_SHORT).show();

            }
        });

        start.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_UP == event.getAction())
                    start.setText("");
                return false;
            }
        });

        end.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_UP == event.getAction())
                    end.setText("");
                return false;
            }
        });

        Button changeHoursbt = barber_appointment_managment_view.findViewById(R.id.buttonChangeWorkHoure);
        changeHoursbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                barbersAppRef.child(barberAppPhone).child("startTime").setValue(start.getText().toString());
                barbersAppRef.child(barberAppPhone).child("endtime").setValue(end.getText().toString());
                Toast.makeText(getActivity(), "Work hours have been changed.", Toast.LENGTH_SHORT).show();

            }
        });
        barberlistView = barber_appointment_managment_view.findViewById(R.id.barberAppManagmentListview);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        barbersAppRef = database.getReference("Barbers");
        customersAppRef = database.getReference("Customer");
        SharedPreferences pref1 = this.getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);// 0 - for private mode
        userEmail = pref1.getString("userEmail", "");
        ArrayList<String> app = new ArrayList<>();
        //String barber;
        barbersAppRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        if(dataSnapshot1.child("email").getValue(String.class).equals(userEmail))
                        {
                            barberAppPhone = dataSnapshot1.getKey();
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
                                    }
                                }
                                ArrayAdapter adapter = new ArrayAdapter(getContext().getApplicationContext(), android.R.layout.simple_list_item_1, app);
                                barberlistView.setAdapter(adapter);
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

        customersAppRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        if(dataSnapshot1.child("email").getValue(String.class).equals(userEmail))
                        {
                            customerAppPhone = dataSnapshot1.getKey();
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
                                    }


                                }
                                ArrayAdapter adapter = new ArrayAdapter(getContext().getApplicationContext(), android.R.layout.simple_list_item_1, app);
                                barberlistView.setAdapter(adapter);
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

        barberlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                curAppDateTime = parent.getItemAtPosition(position).toString();
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked


                                barbersAppRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.child(barberAppPhone).child("Appointments").child(curAppDateTime).exists()) {
                                            String customerPhoneTemp = dataSnapshot.child(barberAppPhone).child("Appointments").child(curAppDateTime).child("phoneNumber").getValue(String.class);
                                            customersAppRef.child(customerPhoneTemp).child("Appointments").child(curAppDateTime).removeValue();
                                            barbersAppRef.child(barberAppPhone).child("Appointments").child(curAppDateTime).removeValue();
                                            Toast.makeText(getActivity(), "Appointment been canceled.", Toast.LENGTH_SHORT).show();
                                            Navigation.findNavController(barber_appointment_managment_view).navigate((R.id.action_barberAppointmentManagment_to_fragment_User_Actions));

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








        return barber_appointment_managment_view;
    }
}
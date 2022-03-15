package com.example.barberapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppointmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppointmentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CalendarView mCalendarView;
    private ListView mlistView;
    private String curDate;
    private String curTime;
    private String customerName;
    private String customerPhoneNumber;
    Appointment appCustomer;
    Appointment appBarber;
    private DatabaseReference barberAppRef;
    private DatabaseReference customerRef;
    private DatabaseReference customerAppRef;
    private DatabaseReference barberWorkDaysRef;
    private String barberDaysOffString = "";


    public AppointmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AppointmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppointmentFragment newInstance(String param1, String param2) {
        AppointmentFragment fragment = new AppointmentFragment();
        Bundle args = new Bundle();
        ArrayList<Appointment> appointments = new ArrayList<>();
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

    String getDaysOff(String barberPhoneNumber, View listViewLayout, int dayOfWeek, String date){


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        barberWorkDaysRef = database.getReference("Barbers/"+barberPhoneNumber);
        barberWorkDaysRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                barberDaysOffString = dataSnapshot.child("workDays").getValue(String.class);
                String wd = "";
                switch (dayOfWeek) {
                    case 1:
                        wd = ("Sunday");
                        break;
                    case 2:
                        wd = ("Monday");
                        break;
                    case 3:
                        wd = ("Tuesday");
                        break;
                    case 4:
                        wd = ("Wednesday");
                        break;
                    case 5:
                        wd = ("Thursday");
                        break;
                    case 6:
                        wd = ("Friday");
                        break;
                    case 7:
                        wd = ("Saturday");
                        break;
                }

                if(!barberDaysOffString.contains(wd) || dataSnapshot.child("daysOff").child(date).exists()) {
                    listViewLayout.setVisibility(LinearLayout.INVISIBLE);
                    Toast.makeText(getActivity(), "Barber doesn't work on this day.", Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        listViewLayout.setVisibility(LinearLayout.VISIBLE);

        return barberDaysOffString;
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View appointment_view = inflater.inflate(R.layout.fragment_appointment, container, false);

        SharedPreferences pref = this.getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);// 0 - for private mode
        String barberPhoneNumber = pref.getString("barberPhoneNumber", "");
        SharedPreferences pref1 = this.getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);// 0 - for private mode
        String userEmail = pref1.getString("userEmail", "");

        View lvLayout = appointment_view.findViewById(R.id.AppointmentListViewLayout);
        lvLayout.setVisibility(LinearLayout.INVISIBLE);

        mlistView = appointment_view.findViewById(R.id.listview);
        mCalendarView = appointment_view.findViewById(R.id.calendarView);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                curDate = (String.valueOf(dayOfMonth) + "-" +String.valueOf(month+1) + "-" +String.valueOf(year));
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference barberHours = database.getReference("Barbers/"+barberPhoneNumber);
                barberHours.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth);
                        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                        String start = dataSnapshot.child("startTime").getValue().toString();
                        String end = dataSnapshot.child("endtime").getValue().toString();
                        DateFormat formatter = new SimpleDateFormat("HH:mm");
                        Time endTime = null;
                        Time startTime = null;
                        try {
                            startTime = new Time(formatter.parse(start).getTime());
                            endTime = new Time(formatter.parse(end).getTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        startTime.getTime();
                        ArrayList<String> list = new ArrayList<>();
                        long milli = startTime.getTime();
                        Time t = new Time(milli);
                        while(t.before(endTime) || t.equals(endTime))
                        {
                            list.add(t.toString());
                            milli+=900000l;
                            t.setTime(milli);
                        }


                        ArrayAdapter adapter = new ArrayAdapter(getContext().getApplicationContext(), android.R.layout.simple_list_item_1, list);
                        mlistView.setAdapter(adapter);
                        getDaysOff(barberPhoneNumber,lvLayout,dayOfWeek, curDate);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

        });

        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                curTime = parent.getItemAtPosition(position).toString();
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                barberAppRef = database.getReference("Barbers/"+barberPhoneNumber+"/Appointments");

                                customerRef = FirebaseDatabase.getInstance().getReference("Customer");
                                customerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists())
                                        {
                                            for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){


                                               if (dataSnapshot1.child("email").getValue(String.class).equals(userEmail))
                                                {
                                                    customerPhoneNumber = dataSnapshot1.child("phoneNumber").getValue(String.class);
                                                    customerName = dataSnapshot1.child("name").getValue(String.class);
                                                     customerAppRef = database.getReference("Customer/"+customerPhoneNumber+"/Appointments").child( curTime+ " " + curDate);
                                                    appCustomer = new Appointment(parent.getItemAtPosition(position).toString(),curDate,barberPhoneNumber,customerName);
                                                    appBarber = new Appointment(parent.getItemAtPosition(position).toString(),curDate,customerPhoneNumber,customerName);

                                                    LinkedList<String> barberApp = new LinkedList<String>();
                                                    barberAppRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                            for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                                                                barberApp.add(dataSnapshot2.getKey().toString());
                                                            }

                                                            if (barberApp.contains(curTime + " " + curDate)) {
                                                                Toast.makeText(getActivity(), "Appointment not available", Toast.LENGTH_SHORT).show();
                                                            }
                                                            else {
                                                                customerAppRef.setValue(appCustomer);
                                                                barberAppRef.child( curTime+ " " + curDate).setValue(appBarber);
                                                                Toast.makeText(getActivity(), "Appointment scheduled successfully", Toast.LENGTH_SHORT).show();
                                                            }

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                                    break;
                                                }
                                                else if(dataSnapshot1.getValue() == null)
                                                {
                                                    continue;
                                                }
                                            }
                                        }
                                        else
                                        {
                                            Toast.makeText(getActivity(), "error making appointment", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to schedule?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });



                return appointment_view;
    }

}

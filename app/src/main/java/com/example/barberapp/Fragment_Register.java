package com.example.barberapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Register#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Register extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Register() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Register.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Register newInstance(String param1, String param2) {
        Fragment_Register fragment = new Fragment_Register();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private FirebaseAuth mAuth;
    CheckBox cbMale,cbFemale,cbOther,cbBarber,cbCustomer;
    LinearLayout barberLayout;
    String gender = null;
    String accountType = "";
    //Day buttons
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


    void Register(String name, String age, String gender, String phoneNumber,String email,String accountType,String shopName,String shopAddress,String password,String workDays, String start,String end){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Registered successfully", Toast.LENGTH_SHORT).show();
                            if (accountType.equals("Barber")){
                                Barber barber = new Barber(name,age,gender,phoneNumber,email,shopName,shopAddress,workDays, start,end);
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("Barbers").child(barber.phoneNumber);
                                myRef.setValue(barber);
                            }
                            else {
                                User user = new User(name, age, gender, phoneNumber, email);
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("Customer").child(user.phoneNumber);
                                myRef.setValue(user);

                            }
                        }
                        else {
                            Toast.makeText(getActivity(), "register failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }




    String daysOfWork(Boolean S,Boolean M,Boolean T,Boolean W,Boolean Ti,Boolean F,Boolean Si){


        String workDays = "";
        if (S)
            workDays += "Sunday,";
        if (M)
            workDays += "Monday,";
        if (T)
            workDays += "Tuesday,";
        if (W)
            workDays += "Wednesday,";
        if (Ti)
            workDays += "Thursday,";
        if (F)
            workDays += "Friday,";
        if (Si)
            workDays += "Saturday";

        return workDays;
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View register_view= inflater.inflate(R.layout.fragment__register, container, false);

        mAuth = FirebaseAuth.getInstance();

        cbMale = register_view.findViewById(R.id.checkBoxMale);
        cbFemale = register_view.findViewById(R.id.checkBoxFemale);
        cbOther = register_view.findViewById(R.id.checkBoxOther);

        cbMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbFemale.setChecked(false);
                cbOther.setChecked(false);
                gender="Male";
            }
        });

        cbFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbMale.setChecked(false);
                cbOther.setChecked(false);
                gender="Female";
            }
        });

        cbOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbFemale.setChecked(false);
                cbMale.setChecked(false);
                gender="Other";
            }
        });

        cbBarber = register_view.findViewById(R.id.checkBoxBarber);
        cbCustomer = register_view.findViewById(R.id.checkBoxCustomer);
        barberLayout = register_view.findViewById(R.id.BarberLayout);
        barberLayout.setVisibility(LinearLayout.INVISIBLE);

        cbBarber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbCustomer.setChecked(false);
                barberLayout.setVisibility(LinearLayout.VISIBLE);
                accountType = "Barber";
            }
        });

        cbCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbBarber.setChecked(false);
                barberLayout.setVisibility(LinearLayout.INVISIBLE);
                accountType = "Customer";
            }
        });

        tS = register_view.findViewById(R.id.tS);
        tM = register_view.findViewById(R.id.tM);
        tT = register_view.findViewById(R.id.tT);
        tW = register_view.findViewById(R.id.tW);
        tTi = register_view.findViewById(R.id.tTi);
        tF = register_view.findViewById(R.id.tF);
        tSi = register_view.findViewById(R.id.tSi);


        EditText pickStartTime = register_view.findViewById(R.id.pickStartTimeButton);
        pickStartTime.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_UP == event.getAction()) {
                    pickStartTime.setText("");
                }
                return false;
            }
        });

        EditText pickEndTime = register_view.findViewById(R.id.pickEndTimeButton);
        pickEndTime.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_UP == event.getAction())
                    pickEndTime.setText("");
                return false;
            }
        });


        Button button_register = register_view.findViewById(R.id.button_register);

        button_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){


                String Vname = ((EditText) register_view.findViewById(R.id.editTextPersonNameRegister)).getText().toString();
                String Vage = ((EditText) register_view.findViewById(R.id.editTextAgeRegister)).getText().toString();
                String VphoneNumber = ((EditText) register_view.findViewById(R.id.editTextPhoneRegister)).getText().toString();
                String Vemail = ((EditText) register_view.findViewById(R.id.editTextTextEmailAddressRegister)).getText().toString();
                String Vpassword = ((EditText) register_view.findViewById(R.id.editTextTextPasswordRegister)).getText().toString();
                String VshopName = ((EditText) register_view.findViewById(R.id.editTextBarberShopNameRegister)).getText().toString();
                String VshopAddress = ((EditText) register_view.findViewById(R.id.editTextTextPostalAddressShop)).getText().toString();
                String Vstart = ((EditText) register_view.findViewById(R.id.pickStartTimeButton)).getText().toString();
                String Vend = ((EditText) register_view.findViewById(R.id.pickEndTimeButton)).getText().toString();


                if ( Vemail.equals("")||
                        Vpassword.equals("")||
                        Vage.equals("")||
                        Vname.equals("")||
                        VphoneNumber.equals("")||
                        Vstart.equals("")||
                        Vend.equals(""))
                    Toast.makeText(getActivity(), "You have to fill all fields", Toast.LENGTH_SHORT).show();

                else if(cbBarber.isChecked())
                {
                    if ( VshopName.equals("") ||
                            VshopAddress.equals(""))
                    {
                        Toast.makeText(getActivity(), "You have to fill all fields", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Register(Vname, Vage, gender, VphoneNumber, Vemail, accountType, VshopName, VshopAddress,Vpassword,
                                daysOfWork(tS.isChecked(),tM.isChecked(),tT.isChecked(),tW.isChecked(),tTi.isChecked(),tF.isChecked(),tSi.isChecked()), Vstart,Vend);
                    }
                }
                else {
                    Register(Vname, Vage, gender, VphoneNumber, Vemail, accountType, VshopName, VshopAddress,Vpassword,
                            daysOfWork(tS.isChecked(),tM.isChecked(),tT.isChecked(),tW.isChecked(),tTi.isChecked(),tF.isChecked(),tSi.isChecked()),Vstart,Vend);
                }
            }
        });

        return register_view;
    }


}
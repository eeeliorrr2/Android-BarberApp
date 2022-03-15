package com.example.barberapp;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Signin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Signin extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Signin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Signin.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Signin newInstance(String param1, String param2) {
        Fragment_Signin fragment = new Fragment_Signin();
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


    private FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View signin_view= inflater.inflate(R.layout.fragment__signin, container, false);
        SharedPreferences pref = this.getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);// 0 - for private mode
        String userEmail = pref.getString("userEmail", "");
        mAuth = FirebaseAuth.getInstance();



        Button button_signin = signin_view.findViewById(R.id.button_signin);
        button_signin.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v){

                String email = ((EditText) signin_view.findViewById(R.id.editTextEmailAddressSignIn)).getText().toString();
                String password = ((EditText) signin_view.findViewById(R.id.editTextPasswordSignIn)).getText().toString();
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("userEmail",email );
                editor.commit();

                if ( email == null || email.isEmpty() || password == null || password.isEmpty())
                    Toast.makeText(getActivity(), "You have to fill Email and password", Toast.LENGTH_SHORT).show();
                else {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        Toast.makeText(getActivity(), "Signed in successfully", Toast.LENGTH_SHORT).show();
                                        Navigation.findNavController(signin_view).navigate((R.id.action_fragment_Signin_to_fragment_User_Actions));
                                    }
                                    else {

                                        Toast.makeText(getActivity(), "Sign in failed, Email or password are incorrect", Toast.LENGTH_LONG).show();
                                    }
                                }});
                }
            }

        });

        Button buttonResetPass = signin_view.findViewById(R.id.button_signin_resetpass);
        buttonResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = ((EditText) signin_view.findViewById(R.id.editTextEmailAddressSignIn)).getText().toString();


                if (email == null || email.isEmpty())
                    Toast.makeText(getActivity(), "Enter your Email to reset password", Toast.LENGTH_SHORT).show();
                else {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Email sent", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                        Toast.makeText(getActivity(), "Email is incorrect or doesn't exist", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });


        return signin_view;
    }
}
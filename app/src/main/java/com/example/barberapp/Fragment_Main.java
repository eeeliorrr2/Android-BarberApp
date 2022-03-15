package com.example.barberapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Main#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Main extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Main() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Main.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Main newInstance(String param1, String param2) {
        Fragment_Main fragment = new Fragment_Main();
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
        View main_view= inflater.inflate(R.layout.fragment__main, container, false);

        Button button_main_signin = main_view.findViewById(R.id.button_main_signin);
        Button button_main_register =  main_view.findViewById(R.id.button_main_register);





        button_main_signin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View main_view){
                Navigation.findNavController(main_view).navigate((R.id.action_fragment_Main_to_fragment_Signin));
            }
        });


        button_main_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View main_view){
                Navigation.findNavController(main_view).navigate((R.id.action_fragment_Main_to_fragment_Register));
            }
        });


        return main_view;
    }
}
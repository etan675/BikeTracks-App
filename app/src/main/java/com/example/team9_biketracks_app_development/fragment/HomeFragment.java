package com.example.team9_biketracks_app_development.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.team9_biketracks_app_development.R;
import com.example.team9_biketracks_app_development.SensorActivity;

public class HomeFragment extends Fragment {
    Spinner spinner;
    public static String transport;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        spinner = rootView.findViewById(R.id.activity_spinner);
        initspinnerfooter();

        //Start button
        Button startSessionButton = rootView.findViewById(R.id.startSessionButton);
        startSessionButton.setOnClickListener(view -> startActivity(new Intent(getActivity(), SensorActivity.class)));
        return rootView;
    }



    private void initspinnerfooter() {
        String[] items = new String[]{
                "No Selection", "Bike", "E-scooter", "Run", "Walk", "Car"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                transport = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), transport, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


    }

}
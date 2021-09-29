package com.example.team9_biketracks_app_development.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.team9_biketracks_app_development.MapActivity;
import com.example.team9_biketracks_app_development.R;
import com.example.team9_biketracks_app_development.SensorActivity;

public class RouteFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        startActivity(new Intent(getActivity(), MapActivity.class));
        return inflater.inflate(R.layout.fragment_route, container, false);
    }
}

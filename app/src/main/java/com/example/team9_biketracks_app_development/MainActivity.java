package com.example.team9_biketracks_app_development;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Intent sessionIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startSessionButton = (Button) findViewById(R.id.startSessionButton);
        startSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStartSession();
            }
        });
    }

    public void openStartSession() {
        sessionIntent = new Intent(this, ActivityManager.class);
        startActivity(sessionIntent);
    }
}

package com.example.ar_final;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen_layout);

        Button activity1_btn = (Button)findViewById(R.id.HomeScreen_button);

        activity1_btn.setOnClickListener(v -> {
            Log.v("myApp", "Button has been clicked");

            //Intent intent =new Intent(HomeScreen.this, SizeSelection.class);
            Intent intent =new Intent(HomeScreen.this, MainActivity.class);

            Bundle bundle = new Bundle();

            intent.putExtras(bundle);
            startActivity(intent);
        });
    }
}

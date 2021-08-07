package com.example.ar_final;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); // hides action bar
        setContentView(R.layout.home_screen_layout);

        Button activity1_btn = (Button)findViewById(R.id.HomeScreen_button);

        activity1_btn.setOnClickListener(v -> {
            Log.v("myApp", "Button has been clicked");

            //Intent intent =new Intent(HomeScreen.this, SizeSelection.class);
            Intent intent =new Intent(HomeScreen.this, SizeSelection.class);

            Bundle bundle = new Bundle();

            intent.putExtras(bundle);
            startActivity(intent);
        });

        Button htu_btn = findViewById(R.id.howToUse);
        htu_btn.setOnClickListener(v -> {
            Intent htuIntent =  new Intent(HomeScreen.this,UserTutorial.class);
            startActivity(htuIntent);
        });

        Button about_btn = findViewById(R.id.aboutPage);
        about_btn.setOnClickListener(v -> {
            Intent aboutIntent =  new Intent(HomeScreen.this,About.class);
            startActivity(aboutIntent);
        });
    }
}

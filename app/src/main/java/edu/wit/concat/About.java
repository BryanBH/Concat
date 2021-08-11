package edu.wit.concat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.wit.concat.R;

public class About extends AppCompatActivity {

    private TextView textView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.about);

        Log.v("Arrived!", "Made it to About!");
        textView = findViewById(R.id.aboutInfo);
        textView.setText("Developed By:\n\n William Zhu\n Bryan Benjumea\n Rich Colorusso\n\n COMP3660\n\n Professor Jones Yu\n\n Wentworth Institute of Technology\n\n\n VERSION 1.0\n\n\n" +
                "August 2021");


        Button btn = findViewById(R.id.aboutBackButton);
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(About.this, HomeScreen.class);
            startActivity(intent);
        });

    }

}

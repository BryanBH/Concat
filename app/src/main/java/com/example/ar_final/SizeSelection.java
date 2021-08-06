package com.example.ar_final;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class SizeSelection extends AppCompatActivity implements  AdapterView.OnItemSelectedListener{

    private Spinner dropdown;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); // hides action bar
        setContentView(R.layout.size_selection);

        // get imageview
        imageView = findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.kitchenchair);

        // get the spinner from the xml
        dropdown = findViewById(R.id.spinner);
        //create an adapter to describe how the items are displayed
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item,getResources().getStringArray(R.array.modelNames));
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        // set the spinner adapter to the previously created one
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);



        try {
            Log.v("myApp", "BUNDLE ARRIVED FROM MAIN ACTIVITY");
            Bundle bundleIn = this.getIntent().getExtras();
            //String modelName = bundle.getString("model");

            //TODO:USE BUNDLE ITEMS
            //loading bundle items
            String modelName = bundleIn.getString("model");
            double lengthIn = bundleIn.getDouble("length");
            double widthIn = bundleIn.getDouble("width");
            double heightIn = bundleIn.getDouble("height");

            Log.v("myApp", "Chosen Model: " + modelName);
            Log.v("myApp", "Length: " + lengthIn);
            Log.v("myApp", "Width: " + widthIn);
            Log.v("myApp", "Height: " + heightIn);

            EditText editLength = findViewById(R.id.lengthInput);
            editLength.setText(String.valueOf(lengthIn));

            EditText editWidth = findViewById(R.id.widthInput);
            editWidth.setText(String.valueOf(widthIn));

            EditText editHeight = findViewById(R.id.heightInput);
            editHeight.setText(String.valueOf(heightIn));


        }catch (Exception e){

        }


        Button nextBtn = findViewById(R.id.sizeSelectionBtn);
        // Next button which bundles info and sends to main activity
        nextBtn.setOnClickListener(v -> {
            Intent nextIntent = new Intent(SizeSelection.this, MainActivity.class);
            int index = dropdown.getSelectedItemPosition();
            String model = dropdown.getSelectedItem().toString();

            EditText editLength = findViewById(R.id.lengthInput);
            EditText editWidth = findViewById(R.id.widthInput);
            EditText editHeight = findViewById(R.id.heightInput);

            double length = Double.parseDouble(editLength.getText().toString());
            double width = Double.parseDouble(editWidth.getText().toString());
            double height = Double.parseDouble(editHeight.getText().toString());

            // pass in selected object
            Bundle bundle = new Bundle();
            bundle.putString("model",model);
            bundle.putDouble("length",length);
            bundle.putDouble("width",width);
            bundle.putDouble("height",height);

            nextIntent.putExtras(bundle);
            startActivity(nextIntent);
        });

        Button backBtn = findViewById(R.id.sizeBackButton);
        backBtn.setOnClickListener(v -> {
            Intent backIntent = new Intent(SizeSelection.this,HomeScreen.class);
            startActivity(backIntent);
        });
    }

    // dropdown on item selected to update imageview with respective model image
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();

        //takes the text from the drop down menu and parses it into lower case and remove spaces
        text = text.toLowerCase();
        text = text.replaceAll("\\s+","");

        //looks inside the resource folder for matching words
        Resources res = getResources();
        int objectId = res.getIdentifier(text, "drawable", getPackageName());
        imageView.setImageResource(objectId);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }
}

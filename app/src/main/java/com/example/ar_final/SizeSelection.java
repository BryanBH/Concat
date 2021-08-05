package com.example.ar_final;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class SizeSelection extends AppCompatActivity implements  AdapterView.OnItemSelectedListener{

    private Spinner dropdown;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.size_selection);

        // get imageview
        imageView = findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.kitchenchair);

        // get the spinner from the xml
        dropdown = findViewById(R.id.spinner);
        //create an adapter to describe how the items are displayed
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.modelNames));
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        // set the spinner adapter to the previously created one
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);

        Button btn = findViewById(R.id.sizeSelectionBtn);
        // Next button which bundles info and sends to main activity
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(SizeSelection.this, MainActivity.class);
            int index = dropdown.getSelectedItemPosition();
            String model = dropdown.getSelectedItem().toString();

            EditText editLength = findViewById(R.id.lengthInput);
            EditText editWidth = findViewById(R.id.widthInput);
            EditText editHeight = findViewById(R.id.heightInput);

            int length = Integer.parseInt(editLength.getText().toString());
            int width = Integer.parseInt(editWidth.getText().toString());
            int height = Integer.parseInt(editHeight.getText().toString());

            // pass in selected object
            Bundle bundle = new Bundle();
            bundle.putString("model",model);
            bundle.putInt("length",length);
            bundle.putInt("width",width);
            bundle.putInt("height",height);

            intent.putExtras(bundle);
            startActivity(intent);
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

package com.example.ar_final;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SizeSelection extends AppCompatActivity {

    private Spinner dropdown;
    private Button btn;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.size_selection);

        // get the spinner from the xml
        dropdown = (Spinner) findViewById(R.id.spinner);
        textView = (TextView) findViewById(R.id.test);
        //create an adapter to describe how the items are displayed
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.modelNames));
        // set the spinner adapter to the previously created one
        dropdown.setAdapter(adapter);

        btn = (Button) findViewById(R.id.sizeSelectionBtn);
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

            //pass in selected object
            Bundle bundle = new Bundle();
            bundle.putString("model",model);
            bundle.putInt("length",length);
            bundle.putInt("width",width);
            bundle.putInt("height",height);

            textView.setText("Model: "+ model + "\nlength: "+ length+" " + "width: "+width+ " " + "height: " +height);

            intent.putExtras(bundle);
            startActivity(intent);
        });
    }
}

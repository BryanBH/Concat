package edu.wit.concat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.wit.concat.R;

import java.util.ArrayList;
import java.util.List;

public class UserTutorial extends AppCompatActivity {

    private TextView textView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); // hides action bar
        setContentView(R.layout.user_tutorial);

        textView = findViewById(R.id.explanation);
        textView.setText("Welcome to Concat! \n" +
                "Online shopping for furniture and other items can be difficult if you don’t know how well an item will fit, or look in a room.\n");

        // testing
        Bitmap item1Image = BitmapFactory.decodeResource(getResources(),R.drawable.concat);
        Bitmap item2Image = BitmapFactory.decodeResource(getResources(),R.drawable.kitchenchair);
        Bitmap item3Image = BitmapFactory.decodeResource(getResources(),R.drawable.clearoff);
        Bitmap item4Image = BitmapFactory.decodeResource(getResources(),R.drawable.back);

        List<ListItem> list = new ArrayList<>();

        ListItem item1 = new ListItem();
        item1.image = item1Image;
        item1.description = "This app is designed to allow users to input specific length, width, and height inputs of an object, and display it in Augmented reality, let it be by one of pre-made models, or by a simple cube or sphere.";
        list.add(item1);

        ListItem item2 = new ListItem();
        item2.image = item2Image;
        item2.description = "In the Size Selection screen, choose which model you want to be displayed, as well the length, width, and height in meters. Click next to continue.";
        list.add(item2);

        ListItem item3 = new ListItem();
        item3.image = item3Image;
        item3.description = "This clear button is located in the top left corner of your screen when you are in your camera. When the button is pressed the next model that gets tapped gets deleted.";
        list.add(item3);

        ListItem item4 = new ListItem();
        item4.image = item4Image;
        item4.description = "When this button is pressed, it returns you to the previous screen.";
        list.add(item4);


        ListItemAdapter adapter = new ListItemAdapter(this, 0, list);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        Button btn = findViewById(R.id.htuBackButton);
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(UserTutorial.this, HomeScreen.class);
            startActivity(intent);
        });
    }
}

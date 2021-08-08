package com.example.ar_final;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;

import com.google.android.filament.ColorGrading;
import com.google.ar.core.Anchor;
import com.google.ar.core.Config;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Session;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.Sceneform;
import com.google.ar.sceneform.collision.Box;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.EngineInstance;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.Renderer;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.lang.ref.WeakReference;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements
        FragmentOnAttachListener,
        BaseArFragment.OnTapArPlaneListener,
        BaseArFragment.OnSessionConfigurationListener,
        ArFragment.OnViewCreatedListener {

    private static final String TAG = "MyActivity";
    private ArFragment arFragment;
    private Renderable model;
    private ViewRenderable viewRenderable;

    private Button clear;
    private Button back;
    private String globalModel;


    private double lengthIn;
    private double widthIn;
    private double heightIn;
    boolean deleteOn = false; //toggles the clear button being able to delete a model
    int objectId; //global variable used to determine id of raw file

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("myApp", "HELLO");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v("myApp", "BUNDLE ARRIVED");
        Bundle bundle= this.getIntent().getExtras();
        //String modelName = bundle.getString("model");


        //loading bundle items
        String modelName = bundle.getString("model");
        double length = bundle.getDouble("length");
        double width = bundle.getDouble("width");
        double height = bundle.getDouble("height");

        lengthIn = length;
        heightIn = height;
        widthIn = width;

        Log.v("myApp", "Chosen Model: " + modelName);
        Log.v("myApp", "Length: " + length);
        Log.v("myApp", "Width: " + width);
        Log.v("myApp", "Height: " + height);

        TextView chosenObjectTV = (TextView) findViewById(R.id.ChosenObject);
        chosenObjectTV.setText(modelName + " L x W x H: " + length + "x" + width + "x" + height);

        //parse object used for resource file searching
        String objectUsed = modelName;
        objectUsed = objectUsed.toLowerCase();
        objectUsed = objectUsed.replaceAll("\\s+","");

        globalModel = objectUsed;

        Resources res = getResources();

        objectId = res.getIdentifier(objectUsed, "raw", getPackageName());

        //Initializes clear button
        //When button is pressed the next model that gets tapped gets deleted
        clear = (Button) findViewById(R.id.clear_model_button);
        clear.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteOn = true;
                clear.setText("Clear on");
                clear.setBackgroundColor(Color.BLUE);
                arFragment.getArSceneView().getScene().addOnPeekTouchListener(new Scene.OnPeekTouchListener() {
                    @Override
                    public void onPeekTouch(HitTestResult hitTestResult, MotionEvent motionEvent) {
                        if (hitTestResult.getNode() != null && deleteOn == true) {
                            Node hitnode = hitTestResult.getNode();
                            hitnode.setParent(null);
                          }
                        deleteOn = false; //reset the delete on so that you can still tap on objects
                        clear.setText("Clear off");
                        clear.setBackgroundColor(Color.RED);

                    }
                });

             }
        });

        back = (Button) findViewById(R.id.back_button);

        //When back button is clicked the screen gets sent to size selection
        //Bundle passes in current length width height and model name
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this, SizeSelection.class);


                Bundle bundle = new Bundle();
                bundle.putString("model",modelName);
                bundle.putDouble("length",length);
                bundle.putDouble("width",width);
                bundle.putDouble("height",height);

                Log.v("myApp", "Sent to back");

                Log.v("myApp", "Chosen Model: " + modelName);
                Log.v("myApp", "Length: " + length);
                Log.v("myApp", "Width: " + width);
                Log.v("myApp", "Height: " + height);

                intent.putExtras(bundle);
                 startActivity(intent);
            }
        });


         getSupportFragmentManager().addFragmentOnAttachListener(this);

        if (savedInstanceState == null) {
            if (Sceneform.isSupported(this)) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.ar_fragment, ArFragment.class, null)
                        .commit();
            }
        }

        loadModels();
    }

    @Override
    public void onAttachFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
        if (fragment.getId() == R.id.ar_fragment) {
            arFragment = (ArFragment) fragment;
            arFragment.setOnSessionConfigurationListener(this);
            arFragment.setOnViewCreatedListener(this);
            arFragment.setOnTapArPlaneListener(this);
        }
    }

    @Override
    public void onSessionConfiguration(Session session, Config config) {
        if (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
            config.setDepthMode(Config.DepthMode.AUTOMATIC);
        }
    }

    @Override
    public void onViewCreated(ArFragment arFragment, ArSceneView arSceneView) {
        Renderer renderer = arSceneView.getRenderer();
        if (renderer != null) {
            renderer.getFilamentView().setColorGrading(
                    new ColorGrading.Builder()
                            .toneMapping(ColorGrading.ToneMapping.FILMIC)
                            .build(EngineInstance.getEngine().getFilamentEngine())
            );
        }
         arSceneView.setFrameRateFactor(SceneView.FrameRate.FULL);
    }

    //model source handled here
    public void loadModels() {
        WeakReference<MainActivity> weakActivity = new WeakReference<>(this);
        ModelRenderable.builder()
                .setSource(this, objectId)
                .setIsFilamentGltf(true)
                .setAsyncLoadEnabled(true)
                .build()
                .thenAccept(model -> {
                    MainActivity activity = weakActivity.get();
                    if (activity != null) {
                        activity.model = model;
                    }
                })
                .exceptionally(throwable -> {
                    Toast.makeText(
                            this, "Unable to load model", Toast.LENGTH_LONG).show();
                    return null;
                });
        ViewRenderable.builder()
                .setView(this, R.layout.view_model_title)
                .build()
                .thenAccept(viewRenderable -> {
                    MainActivity activity = weakActivity.get();
                    if (activity != null) {
                        activity.viewRenderable = viewRenderable;
                    }
                })
                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load model", Toast.LENGTH_LONG).show();
                    return null;
                });
    }

    @Override
    public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
        if (model == null || viewRenderable == null) {
            Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create the Anchor.
        Anchor anchor = hitResult.createAnchor();
        AnchorNode anchorNode = new AnchorNode(anchor);
        anchorNode.setParent(arFragment.getArSceneView().getScene());

        // Create the transformable model and add it to the anchor.
        TransformableNode model = new TransformableNode(arFragment.getTransformationSystem());
        model.getScaleController().setEnabled(false);
        model.setRenderable(this.model);

        //Setting the min and max to large params
        model.getScaleController().setMinScale(0.001f);
        model.getScaleController().setMaxScale(200f);

        //USER INPUTED X Y AND Z TODO: CHANGE FROM HARDCODE TEST TO USER INPUT FROM BUNDLE;
        float x;
        float y;
        float z;
        Log.v("myApp", "Global Model: " + globalModel);

        if (globalModel.equals("cube") || globalModel.equals("sphere")) {
            x = 0.01f/6.5f;
            y = 0.01f/6.5f; // default = 7 inches sets it to 1 inch
            z = 0.01f/6.5f;

            x = x*(float)widthIn;
            y = y*(float)heightIn;
            z = z*(float)lengthIn;
        }else if (globalModel.equals("monitor")){

            x = 1/29f;//normal sized //length = 29 inches
            y = 1/13f; //normal height = 13 inches
            z = 1;
            Log.v("myApp", "Monitor inialized with dims " + x);

            x = x*(float)widthIn;
            y = y*(float)heightIn;
            z = z*(float)lengthIn;

        }
        else{
            x = 1;
            y = 1;
            z = 1;
        }



        Log.v("myApp", "Global Model X: " + x);
        Log.v("myApp", "Global Model Y: " + y);
        Log.v("myApp", "Global Model Z: " + z);

        model.setLocalScale(new Vector3(x,y,z));
        model.getScaleController().setSensitivity(0); //remove pinch scaling
        model.setParent(anchorNode);
        model.select();


        Box box = (Box) model.getRenderable().getCollisionShape();


    }

}
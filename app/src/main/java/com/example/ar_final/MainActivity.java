package com.example.ar_final;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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
    private Button clear;
    private ArFragment arFragment;
    private Renderable model;
    private ViewRenderable viewRenderable;
    boolean deleteOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("myApp", "HELLO");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v("myApp", "BUNDLE ARRIVED");
        Bundle bundle= this.getIntent().getExtras();

        // loading bundle items
        String modelName = bundle.getString("model");
        int length = bundle.getInt("length");
        int width = bundle.getInt("width");
        int height = bundle.getInt("height");


        //on tap means it allows the next node touch to be deleted
        //CHANGE TO TOGGLE??

        clear = (Button) findViewById(R.id.clear_model_button);
        clear.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOn = true;
                arFragment.getArSceneView().getScene().addOnPeekTouchListener(new Scene.OnPeekTouchListener() {
                    @Override
                    public void onPeekTouch(HitTestResult hitTestResult, MotionEvent motionEvent) {
                        if (hitTestResult.getNode() != null && deleteOn == true) {
                            Node hitnode = hitTestResult.getNode();
                            hitnode.setParent(null);
                        }
                        deleteOn = false;
                    }
                });

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
                .setSource(this,R.raw.round_table)
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


        //units are in meters btw
        model.getScaleController().setMinScale(0.001f);
        model.getScaleController().setMaxScale(200f);

        //USER INPUTED X Y AND Z
        float x = 0.1f;
        float y = 0.9f;
        float z = 0.1f;
        model.setLocalScale(new Vector3(x,y,z));
        model.getScaleController().setSensitivity(0); //remove pinch scaling
        model.setParent(anchorNode);
        model.select();


        Box box = (Box) model.getRenderable().getCollisionShape();
        Vector3 renderableSize = box.getSize();

        Log.v("myApp", "HELLO MONITOR SIZE:" + renderableSize.toString());

        //Log.i(TAG, "MONITOR SIZE: " + renderableSize.toString());


//        Node titleNode = new Node();
//        titleNode.setParent(model);
//        titleNode.setEnabled(false);
//        titleNode.setLocalPosition(new Vector3(0.0f, 1.0f, 0.0f));
//        titleNode.setRenderable(viewRenderable);
//        titleNode.setEnabled(true);
    }




}
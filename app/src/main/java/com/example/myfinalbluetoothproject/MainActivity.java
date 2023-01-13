package com.example.myfinalbluetoothproject;

import static android.provider.MediaStore.ACTION_VIDEO_CAPTURE;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> permissionsList;

    String[] str = {Manifest.permission.BLUETOOTH_SCAN,
    Manifest.permission.BLUETOOTH,
    Manifest.permission.BLUETOOTH_CONNECT,
    Manifest.permission.BLUETOOTH_ADMIN,
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.CAMERA,
    Manifest.permission.RECORD_AUDIO,
    Manifest.permission.USE_FINGERPRINT,
    Manifest.permission.RECORD_AUDIO
    };

    private Button camera;
    private Button mic;
    private ImageView imageView;
    private static int code = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionsList = new ArrayList<>();
        permissionsList.addAll(Arrays.asList(str));
        askForUserPermissions(permissionsList);
        camera = findViewById(R.id.button);
        mic = findViewById(R.id.button2);
        imageView = findViewById(R.id.image);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videoIntent = new Intent(ACTION_VIDEO_CAPTURE);
                someActivityResultLauncher.launch(videoIntent);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                someActivityResultLauncher.launch(cameraIntent);

            }
        });

        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent micIntent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                someActivityResultLauncher.launch(micIntent);
            }
        });
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                    }
                }
            });

    public void askForUserPermissions(ArrayList<String> permissions){
        String [] newPermissionList = new String[permissions.size()];
        for (int i = 0;i<newPermissionList.length;i++){
            newPermissionList[i] = permissions.get(i);
        }
        if (newPermissionList.length > 0){
            permissionsLauncher.launch(newPermissionList);
        }else {
            showDialog();
        }
    }

    ActivityResultLauncher<String[]> permissionsLauncher = registerForActivityResult(new
                    ActivityResultContracts.RequestMultiplePermissions(),
            new ActivityResultCallback<Map<String, Boolean>>() {
                @Override
                public void onActivityResult(Map<String, Boolean> result) {

                }
            });

    AlertDialog alertDialog;
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("permission required")
                .setMessage("some permissions have been denied " +
                        "by you that are important to use this app")
                .setPositiveButton("Setting",((dialog, which) -> {
                    dialog.dismiss();
                }));
        if (alertDialog == null){
            alertDialog = builder.create();
            if (!alertDialog.isShowing()){
                alertDialog.show();
            }
        }
    }
}
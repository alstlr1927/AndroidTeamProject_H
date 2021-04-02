package com.cookandroid.androidteamproject_h;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnCamera;
    private ImageView imgPhoto;
    private EditText edtContent;

    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        findViewByIdFunc();
    }

    private void findViewByIdFunc() {
        btnCamera = findViewById(R.id.btnCamera);
        imgPhoto = findViewById(R.id.imgPhoto);
        edtContent = findViewById(R.id.edtContent);

        btnCamera.setOnClickListener(this);
        imgPhoto.setOnClickListener(this);
        edtContent.setOnClickListener(this);

        title = getIntent().getStringExtra("title");
    }

    @Override
    public void onClick(View v) {

    }

}
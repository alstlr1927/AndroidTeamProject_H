package com.cookandroid.androidteamproject_h;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnCamera;
    private ImageView imgPhoto;
    private EditText edtContent;

    private Uri photoUri;
    private String title;
    private String imageFile;
    private String imageFilepath;

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
        switch(v.getId()) {
            case R.id.btnCamera :

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if(intent.resolveActivity(getPackageManager()) != null) {

                    File file = null;

                    try {
                        file = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(file != null) {
                        photoUri = FileProvider.getUriForFile(CameraActivity.this, getPackageName(), file);
                    }
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);


                }
                break;
        }
    }

    private File createImageFile() throws IOException {

        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd_hhmmss");

        String str = date.format(new Date());
        imageFile = "pic_" + str + "_";

        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFile, ".jpg", dir);
        imageFilepath = image.getAbsolutePath();

        return image;
    }

}
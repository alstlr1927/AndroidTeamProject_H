package com.cookandroid.androidteamproject_h;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnCamera, btnAlbumSave;
    private ImageView imgPhoto;
    private EditText edtContent;

    private Uri photoUri;
    private String title;
    private String imageFile;
    private String imageFilepath;
    private static final int REQUEST_CODE = 109;

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
        btnAlbumSave = findViewById(R.id.btnAlbumSave);

        btnCamera.setOnClickListener(this);
        btnAlbumSave.setOnClickListener(this);

        title = getIntent().getStringExtra("title");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnCamera :

                int permissionCheck = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA);
                if(permissionCheck == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 0);
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    if(intent.resolveActivity(getPackageManager()) != null) {

                        File file = null;

                        try {
                            file = createImageFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if(file != null) {
                            photoUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName(), file);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                            startActivityForResult(intent, REQUEST_CODE);
                        }
                    }
                }
                break;
            case R.id.btnAlbumSave :
                MainActivity.db = MainActivity.dbHelper.getWritableDatabase();

                MainActivity.db.execSQL("UPDATE checker_" + LoginActivity.userID + " SET "
                        + "picture='" + imageFilepath
                        + "', contents='" + edtContent.getText().toString()
                        + "', complete=" + 1
                        + " WHERE title='" + title + "';");

                Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilepath);
            ExifInterface exifInterface = null;

            try {
                exifInterface = new ExifInterface(imageFilepath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if(exifInterface != null) {
                exifOrientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exiforToDe(exifOrientation);
            } else {
                exifDegree =0;
            }
            Bitmap bitmap1 = rotate(bitmap, exifDegree);
            imgPhoto.setImageBitmap(bitmap1);
        }
    }

    private Bitmap rotate(Bitmap bitmap, int exifDegree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(exifDegree);
        Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bitmap1;
    }

    private int exiforToDe(int exifOrientation) {
        switch (exifOrientation) {
            case ExifInterface.ORIENTATION_ROTATE_90 :
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180 :
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270 :
                return 270;
        }
        return 0;
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
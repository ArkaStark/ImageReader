package com.example.ocr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.job.JobInfo;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private String currentPhotoPath = "";
    private static final int REQUEST_IMAGE_CAPTURE = 0;
    private ImageView on_click_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button clicked = (Button) findViewById(R.id.capture_button);
        on_click_id = (ImageView) findViewById(R.id.click_image);
    }



    public void captureImage(View view) throws IOException {

        Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();

        } catch (IOException e) {
            e.printStackTrace();
        }
        takepic.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));

        startActivityForResult(takepic,REQUEST_IMAGE_CAPTURE);
        }
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        //ImageView on_click_id = (ImageView) findViewById(R.id.click_image);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            setReducedImageSize();
        }
    }

     File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) ;
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    public void openFolder(View view){
        Intent intent = new Intent();
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
                +  File.separator + "Pictures" + File.separator);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivity(intent);
    }
    void setReducedImageSize()
    {
        int targetImageViewWidth = on_click_id.getWidth();
        int targetImageViewHeight = on_click_id.getHeight();
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        int cameraImageWidth = bmOptions.outWidth;
        int cameraImageHeight = bmOptions.outHeight;
        int scaleFactor = Math.min(cameraImageWidth/targetImageViewWidth, cameraImageHeight/targetImageViewHeight);
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inJustDecodeBounds = false;
        Bitmap photoReducedSizeBitmp = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        on_click_id.setImageBitmap(photoReducedSizeBitmp);
    }
}




   

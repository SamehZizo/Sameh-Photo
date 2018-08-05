package com.sameh.photo.camera;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;
import com.sameh.photo.R;
import java.io.IOException;
import static android.app.Activity.RESULT_OK;

public class Camera {

    private Activity activity;
    private final int TAKE_PHOTO_PERMISSION = 1000;
    private final int TAKE_PHOTO = 1500;
    private Uri imageUri;

    public Camera(Activity activity) {
        this.activity = activity;
    }

    public void takePhoto(){
        checkPermission(activity,TAKE_PHOTO_PERMISSION);
    }

    private void checkPermission(Activity activity , int REQUEST_CODE) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ) {
            // Permission is not granted
            requestPermission(activity , REQUEST_CODE);
        }
        else {
            // Permission is granted
            imageUri = takeShot(activity,TAKE_PHOTO);
        }
    }

    private void requestPermission(Activity activity , int REQUEST_CODE) {
        ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}
        , REQUEST_CODE);
    }

    private Uri takeShot(Activity activity , int PICTURE_RESULT){
        ContentValues values;
        values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = activity.getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        activity.startActivityForResult(intent, PICTURE_RESULT);

        return imageUri;
    }

    public void onRequestCameraPermission(int requestCode, String[] permissions, int[] grantResults){
        if (requestCode == TAKE_PHOTO_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED  &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED  &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED  ) {
                imageUri = takeShot(activity,TAKE_PHOTO);
            } else {
                // User refuse permission
                Toast.makeText(activity, R.string.camera_permission_denied, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onActivityCameraResult(int requestCode, int resultCode, Completed complete){
        if (requestCode == TAKE_PHOTO && resultCode == RESULT_OK && imageUri != null){
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                        activity.getContentResolver(), imageUri);
                complete.onCompleted(bitmap,imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public interface Completed {
        void onCompleted(Bitmap bitmap , Uri imageUri);
    }
}

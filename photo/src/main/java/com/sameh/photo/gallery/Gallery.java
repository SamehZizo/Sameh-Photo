package com.sameh.photo.gallery;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.telecom.StatusHints;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class Gallery {

    private Activity activity;
    private final int CHOOSE_PHOTO = 2500;
    private Uri imageUri;
    private Intent data;

    public Gallery(Activity activity) {
        this.activity = activity;
    }

    public void chooseImage(){
        Intent intent = new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent,CHOOSE_PHOTO);
    }

    public void chooseImage(Intent data){
        this.data = data;
        Intent intent = new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent,CHOOSE_PHOTO);
    }

    public void chooseImageV2(String title){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, title), CHOOSE_PHOTO);
    }

    public void chooseImageV2(String title, Intent data){
        this.data = data;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, title), CHOOSE_PHOTO);
    }

    public void onActivityGalleryResult(int requestCode, int resultCode, Intent data , Completed completed){
        if (requestCode == CHOOSE_PHOTO && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            completed.onCompleted(bitmap , imageUri, this.data);
        }
    }

    public interface Completed {
        void onCompleted(Bitmap bitmap , Uri imageUri, @Nullable Intent data);
    }
}

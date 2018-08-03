package com.sameh.photo.gallery;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class Gallery {

    private Activity activity;
    private final int CHOOSE_PHOTO = 150;
    private Uri imageUri;

    public Gallery(Activity activity) {
        this.activity = activity;
    }

    public void chooseImage(){
        Intent intent = new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent,CHOOSE_PHOTO);
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
            completed.onCompleted(bitmap , imageUri);
        }
    }

    public interface Completed {
        void onCompleted(Bitmap bitmap , Uri imageUri);
    }
}

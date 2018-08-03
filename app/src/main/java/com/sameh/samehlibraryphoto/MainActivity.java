package com.sameh.samehlibraryphoto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import com.sameh.photo.camera.Camera;
import com.sameh.photo.gallery.Gallery;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView image;

    Camera camera;
    Gallery gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = findViewById(R.id.image);
        image.setOnClickListener(this);

        camera = new Camera(this);
        gallery = new Gallery(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.image :
                camera.takePhoto();
                //gallery.chooseImage();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        camera.onRequestCameraPermission(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        camera.onActivityCameraResult(requestCode, resultCode, new Camera.Completed() {
            @Override
            public void onCompleted(Bitmap bitmap, Uri imageUri) {
                image.setImageBitmap(bitmap);
            }
        });
        gallery.onActivityGalleryResult(requestCode, resultCode, data, new Gallery.Completed() {
            @Override
            public void onCompleted(Bitmap bitmap, Uri imageUri) {
                image.setImageBitmap(bitmap);
            }
        });
    }
}

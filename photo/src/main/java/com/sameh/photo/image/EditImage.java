package com.sameh.photo.image;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

public class EditImage {

    public static Bitmap Resize(Bitmap bitmap , int newWidth , int newHeight){

        Bitmap resized = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);

        return  resized;
    }

    public static Bitmap EditImageRotate(Activity activity , Uri imageUri){

        Bitmap rotatedBitmap = null;
        Bitmap thumbnail;
        String imageUrl;

        try {
            thumbnail = MediaStore.Images.Media.getBitmap(
                    activity.getContentResolver(), imageUri);

            imageUrl = getRealPathFromURI(imageUri,activity);

            ExifInterface ei = new ExifInterface(imageUrl);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            switch(orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(thumbnail, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(thumbnail, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(thumbnail, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = thumbnail;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return rotatedBitmap;

    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private static String getRealPathFromURI(Uri contentUri , Activity activity) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = activity.managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}

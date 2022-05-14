package com.course.labs.dailyselfie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class ImgHelper {

    private static final String LOG_TAG = ImgHelper.class.getSimpleName();

    public static void setImgFromFilePath(String imgPath, ImageView imgView, int targetW, int targetH) {
        BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
        bmpOptions.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imgPath, bmpOptions);

        int photoW = bmpOptions.outWidth;
        int photoH = bmpOptions.outHeight;
        int scaleFactor = Math.max(photoW / targetW, photoH / targetH);

        bmpOptions.inJustDecodeBounds = false;
        bmpOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, bmpOptions);
        imgView.setImageBitmap(bitmap);
    }

    public static void setImgFromFilePath(String imgPath, ImageView imgView) {
        setImgFromFilePath(imgPath, imgView, 120, 160);
    }
}

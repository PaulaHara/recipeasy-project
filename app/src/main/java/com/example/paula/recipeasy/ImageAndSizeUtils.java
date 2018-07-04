package com.example.paula.recipeasy;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.TypedValue;

public class ImageAndSizeUtils {

    public static Bitmap resize(Bitmap bitmap, Resources r){
        int width = Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 312,r.getDisplayMetrics()));
        int height = Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 95,r.getDisplayMetrics()));

        Bitmap imageResized = Bitmap.createScaledBitmap(bitmap, width, height, false);

        return imageResized;
    }

    public static int getWidth(int widthInDIP, Resources r) {
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, widthInDIP,r.getDisplayMetrics()));
    }
}

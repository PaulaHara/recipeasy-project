package com.example.paula.recipeasy;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.util.TypedValue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PictureUtils {

    public static Bitmap getScaleBitmap(String path, Activity activity){
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);

        return getScaleBitmap(path, size.x, size.y);
    }

    public static Bitmap getScaleBitmap(String path, int destWidth, int destHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        int inSampleSize = 1;

        if(srcHeight > destHeight || srcWidth > destWidth) {
            float heightScale = srcHeight / destHeight;
            float widthScale = srcWidth /destWidth;

            inSampleSize = Math.round(heightScale > widthScale ? heightScale : widthScale);
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(path, options);
    }

    public static Bitmap loadBitmap2(Context context, String picName, int destWidth, int destHeight){
        Bitmap b = null;
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(picName);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(fis, null, options);

            float srcWidth = options.outWidth;
            float srcHeight = options.outHeight;

            int inSampleSize = 2;

            if(srcHeight > destHeight || srcWidth > destWidth) {
                float heightScale = srcHeight / destHeight;
                float widthScale = srcWidth /destWidth;

                inSampleSize = Math.round(heightScale > widthScale ? heightScale : widthScale);
            }

            options = new BitmapFactory.Options();
            options.inSampleSize = inSampleSize;

            b = BitmapFactory.decodeStream(fis, null, options);
            fis.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return b;
    }

    public static Bitmap loadBitmap(Context context, String picName){
        Bitmap b = null;
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(picName);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;

            b = BitmapFactory.decodeStream(fis, new Rect(), options);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return b;
    }
}

package com.example.cliff.customlisttest.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

/**
 * Created by Cliff on 8/11/2015.
 */
public class StaticUtils {
    private static final int LINE_THICKNESS = 2;

    // Should be moved to a utilities JAR when appropriate.

    // TODO: -- cw: Pick a use case and drop the other.

    // region Use case 1
    public static Bitmap loadBitmapFromView(View v) {
        return loadBitmapFromView(v, null);
    }

    public static Bitmap loadBitmapFromView(View v, Integer line_weight) {
        Bitmap b = Bitmap.createBitmap(v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        Rect rect = new Rect(0, 0, b.getWidth(), b.getHeight());
        Paint p = new Paint();

        p.setStyle(Paint.Style.STROKE);
        if (line_weight != null) {
            p.setStrokeWidth(line_weight);
        }
        p.setColor(Color.BLACK);

        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        c.drawBitmap(b, 0, 0, null);
        c.drawRect(rect, p);

        return b;
    }
    // endregion


    // region Use case 2
    public static Bitmap getBitmapWithBorder(View v) {
        return getBitmapWithBorder(v, LINE_THICKNESS);
    }

    /** Draws a black border over the screenshot of the view passed in. */
    public static Bitmap getBitmapWithBorder(View v, int thickness) {
        Bitmap bitmap = getBitmapFromView(v);
        Canvas can = new Canvas(bitmap);

        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(thickness);
        paint.setColor(Color.BLACK);

        can.drawBitmap(bitmap, 0, 0, null);
        can.drawRect(rect, paint);

        return bitmap;
    }

    /** Returns a bitmap showing a screenshot of the view passed in. */
    public static Bitmap getBitmapFromView(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas (bitmap);
        v.draw(canvas);

        return bitmap;
    }
    // endregion

}

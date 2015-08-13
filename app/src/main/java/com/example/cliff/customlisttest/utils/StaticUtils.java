package com.example.cliff.customlisttest.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by Cliff on 8/11/2015.
 */
public class StaticUtils {

    // Should be moved to a utilities JAR when appropriate.
    public static Bitmap loadBitmapFromView(View v) {
        return loadBitmapFromView(v, null);
    }

    public static Bitmap loadBitmapFromView(View v, Integer line_weight) {
        Bitmap b = Bitmap.createBitmap( v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
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

}

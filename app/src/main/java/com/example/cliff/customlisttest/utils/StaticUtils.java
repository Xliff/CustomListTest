package com.example.cliff.customlisttest.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;

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

    public static Pair<Integer,Integer> measureView(View v) {
        ViewGroup.LayoutParams lp = v.getLayoutParams();
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(lp.width, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(lp.height, View.MeasureSpec.EXACTLY);

        v.measure(widthMeasureSpec, heightMeasureSpec);
        int h = v.getMeasuredHeight();
        int w = v.getMeasuredWidth();

        ViewGroup p;
        ViewGroup.MarginLayoutParams mlp;

        // cw: I hate using literals, but that's what I'm left with.
        if (h == 0x00ffffff) {
            p = (ViewGroup)v.getParent();
            mlp = (ViewGroup.MarginLayoutParams)p.getLayoutParams();

            h = p.getHeight()
                - mlp.topMargin
                - mlp.bottomMargin
                - p.getPaddingTop()
                - p.getPaddingBottom();
        }

        if (w == 0x00ffffff) {
            p = (ViewGroup)v.getParent();
            mlp = (ViewGroup.MarginLayoutParams)p.getLayoutParams();

            w = p.getWidth()
                - mlp.leftMargin
                - mlp.rightMargin
                - p.getPaddingLeft()
                - p.getPaddingRight();
        }

        return new Pair<>(w, h);
    }


    // region Use case 2
    public static Bitmap getBitmapWithBorder(View v) {
        int w = v.getWidth();
        int h = v.getHeight();

        // cw: Hail mary special case to obtain width and height
        if (w==0 && h==0) {
            Pair wh = measureView(v);
            w = (Integer)wh.first;
            h = (Integer)wh.second;
        }

        return getBitmapWithBorder(v, w, h, LINE_THICKNESS);
    }

    public static Bitmap getBitmapWithBorder(View v, int width, int height) {
        return getBitmapWithBorder(v, width, height, LINE_THICKNESS);
    }

    /** Draws a black border over the screenshot of the view passed in. */
    public static Bitmap getBitmapWithBorder(View v, Integer width, Integer height, int thickness) {
        if (width == null || height == null || width == 0 || height == 0) {
            throw new IllegalArgumentException(
                "Width and Height must be non-null AND non-zero in call to getBitmapWithBorder()"
            );
        }
        Bitmap bitmap = getBitmapFromView(v, width, height);
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
    public static Bitmap getBitmapFromView(View v, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas (bitmap);
        v.draw(canvas);

        return bitmap;
    }
    // endregion

}

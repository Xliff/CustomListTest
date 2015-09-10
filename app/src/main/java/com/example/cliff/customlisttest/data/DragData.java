package com.example.cliff.customlisttest.data;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cliff.customlisttest.DragDropListView;

/**
 * Created by Cliff on 8/20/2015.
 */
public class DragData {
    public ImageView i_Team = null;
    public TextView tv_pos, tv_fn, tv_ln, tv_bye, tv_rank;
    public PlayerData pd = null;
    public Bitmap b = null;
    public int origListPosition;
    public View itemView;
    public DragDropListView originView;
}

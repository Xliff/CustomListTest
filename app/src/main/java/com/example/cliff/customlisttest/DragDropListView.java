package com.example.cliff.customlisttest;

import android.content.Context;
import android.content.res.TypedArray;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by Cliff on 8/12/2015.
 */
public class DragDropListView extends ListView {
    private Boolean m_DragTarget = false;
    private Boolean m_DrawItemBorder = false;
    private int m_BorderSize = 0;
    private int m_BorderColor;
    private ImageView m_DragImage;

    // CONSTRUCTOR
    public DragDropListView(Context context) {
        super(context);

        // Set default border color.
        TypedArray array = context.getTheme().obtainStyledAttributes(
            new int[]{ android.R.attr.colorBackground }
        );
        m_BorderColor = array.getColor(0, 0xFF00FF);
        m_DragImage = null;
    }

    //region GETTERS AND SETTERS
    //
    public Boolean getDragTarget() {
        return m_DragTarget;
    }

    public void setDragTarget(Boolean m_DragTarget) {
        this.m_DragTarget = m_DragTarget;
    }

    public Boolean getDrawItemBorder() {
        return m_DrawItemBorder;
    }

    public void setDrawItemBorder(Boolean m_DrawItemBorder) {
        this.m_DrawItemBorder = m_DrawItemBorder;
    }

    public int getBorderSize() {
        return m_BorderSize;
    }

    public void setBorderSize(int m_BorderSize) {
        this.m_BorderSize = m_BorderSize;
    }

    public int getBorderColor() {
        return m_BorderColor;
    }

    public void setBorderColor(int m_BorderColor) {
        this.m_BorderColor = m_BorderColor;
    }

    public ImageView getDragImage() {
        return m_DragImage;
    }

    public void setDragImage(ImageView m_DragImage) {
        this.m_DragImage = m_DragImage;
    }
    //endregion



}


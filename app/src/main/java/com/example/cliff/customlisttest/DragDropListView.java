package com.example.cliff.customlisttest;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.cliff.customlisttest.data.DragData;
import com.example.cliff.customlisttest.utils.StaticUtils;

/* Much of this cribbed from the following sources:
 *
 *      https://github.com/mtparet/Drag-And-Drop-Android
 *      https://www.youtube.com/watch?v=_BZIvjMgH-Q  [DevBytes]
 *      http://stackoverflow.com/questions/5002049/ontouchevent-vs-ontouch
 *
 * .... many, many, many thanks!
 *
 *  - Cliff
 */


/*
 * cw:
 * Initially, we started out with a RelativeLayout with all of the decendant views under it.
 * Now I think we need to have a custom parentView (which sub-classes View) we can use so that we
 * can override its dispatchDraw() method to implement the hoverCell. Right now we have a ImageView
 * that we are using to accomplish this, but I worry about the performace with drawing a View vs
 * a BitmapDrawable.
 */

/*
 * cw:
 * Y'know, it just occurred to me that most of the DevBytes example deals with INTERNAL SWAPPING of
 * a list view, where as here, there will be no swapping, just animation of a list view. The only thing
 * I need from the DevBytes project is the scrolling and the animation bits.
 */

/**
 * Created by Cliff on 8/12/2015.
 */


public class DragDropListView extends ListView {
    private final int SMOOTH_SCROLL_AMOUNT_AT_EDGE = 15;
    private final int MOVE_DURATION = 150;
    private final int INVALID_ID = -1;

    private Boolean m_DragTarget = false;
    private Boolean m_DrawItemBorder = false;
    private boolean m_SelectionMobile = false;
    private boolean m_IsMobileScrolling = false;
    // TODO: Set below to true to limit control to sorting ONLY.
    private boolean m_LimitBounds = false;

    private int mTotalOffset = 0;
    private int mSmoothScrollAmountAtEdge = 0;

    private long mAboveItem = INVALID_ID;
    private long mMobileItem = INVALID_ID;
    private long mBelowItem = INVALID_ID;

    private MainActivity m_A;
    private Resources m_R;

    private int mScrollState = OnScrollListener.SCROLL_STATE_IDLE;

    private float m_DragX;
    private float m_DragY;
    private int m_ActivePointerId;


    // region CONSTRUCTORS
    public DragDropListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public DragDropListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        setOnItemLongClickListener(mOnItemLongClickListener);
        // TODO: The scroll listener will take a bit of work.
        //setOnScrollListener(mScrollListener);
        m_R = getResources();
        DisplayMetrics metrics = m_R.getDisplayMetrics();
        mSmoothScrollAmountAtEdge = (int)(SMOOTH_SCROLL_AMOUNT_AT_EDGE / metrics.density);

        if (!this.isInEditMode()) {
            m_A = (MainActivity) context;
        }
    }
    //endregion

    //region LONG CLICK LISTENER
    private AdapterView.OnItemLongClickListener mOnItemLongClickListener =
        new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {

            View selectedView = getChildAt(pos);

            // Stores the data associated with the selected view.
            DragData dd = ((DragData)selectedView.getTag());
            // cw: This will be used by the SELECTED list when we need to delete an item.
            dd.origListPosition = pos;

            // TODO: Must check to see if the Canvas returned belongs to the control or the parent.
            Bitmap b = StaticUtils.getBitmapWithBorder(selectedView);
            if (dd.b == null) {
                dd.b = b;
                // cw: May not be necessary.
                selectedView.setTag(dd);
            }

            m_A.setDragViewOrigin(selectedView);

            return true;
            }
        };
    //endregion

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

    public boolean getLimitBounds() {
        return m_LimitBounds;
    }

    public void setLimitBounds(boolean m_LimitBounds) {
        this.m_LimitBounds = m_LimitBounds;
    }
    //endregion

}


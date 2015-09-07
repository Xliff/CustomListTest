package com.example.cliff.customlisttest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.cliff.customlisttest.data.DragData;

/**
 * Created by Cliff on 9/6/2015.
 */

// cw: I tried keeping this as an inner class to the Activity, but Android still complains.
//     Language constructs like that are useful and SHOULD be possible withoit playing with
//     scoping and static games. Now I need to keep action coordinates local to this class.
// -- 9/6/2015
public class DragRelativeLayout extends RelativeLayout {

    private float m_DownX = -1;
    private float m_DownY = -1;

    private Bitmap m_DragBitmap;
    private Boolean m_DroppedOnTarget;
    private View m_DragViewOrigin;
    private MainActivity m_A;

    public DragRelativeLayout(Context context) {
        super(context);
        init(context);
    }

    public DragRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DragRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        if (!this.isInEditMode()) {
            m_A = (MainActivity) context;
        }
        m_DroppedOnTarget = null;
        m_DragViewOrigin = null;
    }

    //region Getters and Setters
    public Bitmap getDragBitmap() {
        return m_DragBitmap;
    }

    public void setDragBitmap(Bitmap m_DragBitmap) {
        this.m_DragBitmap = m_DragBitmap;
    }

    public MainActivity getActivity() {
        return m_A;
    }

    public void setActivity(MainActivity m_A) {
        this.m_A = m_A;
    }

    public View getDragViewOrigin() {
        return m_DragViewOrigin;
    }

    public void setDragViewOrigin(View m_DragViewOrigin) {
        this.m_DragViewOrigin = m_DragViewOrigin;
        //  1 - Hide the view and store it for later
        //  2 - Get the converted bitmap from the View tag data
        //  3 - Handle the view disposition given the last position in the drag action.
        //      Note that this step is handled in the private ViewGroup sub-class.
        DragData dd = (DragData)m_DragViewOrigin.getTag();
        setDragBitmap(dd.b);
        m_DragViewOrigin.setVisibility(View.INVISIBLE);
    }
    //endregion

    @Override
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (m_DragBitmap != null) {
            // cw: Force drag image to be centered on touch.
            float centerX = m_DownX - m_DragBitmap.getWidth() / 2;
            float centerY = m_DownY - m_DragBitmap.getHeight() / 2;
            canvas.drawBitmap(m_DragBitmap, centerX, centerY, null);
        }
    }

    public void unsetDragViewOrigin() {
        if (m_DroppedOnTarget == null || m_DroppedOnTarget == false) {
            // cw: Handle animation before this method called.
            if (m_DragViewOrigin != null) {
                m_DragViewOrigin.setVisibility(View.VISIBLE);
            }
        } else {
            // cw: If dropped on a target, then notify source to perform its required
            // operations for this event.
        }
        m_DragViewOrigin = null;
        m_DroppedOnTarget = null;
        m_DragBitmap = null;
    }

    public View getChildUnderEvent(MotionEvent ev) {
        return getChildUnderEvent(this, ev);
    }

    public View getChildUnderEvent(ViewGroup v, MotionEvent ev) {
        int x = Math.round(ev.getX());
        int y = Math.round(ev.getY());
        for (int i = 0; i < v.getChildCount(); i++) {
            View child = v.getChildAt(i);

            if (
                x > child.getLeft()  &&
                x < child.getRight() &&
                y > child.getTop()   &&
                y < child.getBottom()
            ) {
                if (child instanceof ViewGroup) {
                    return getChildUnderEvent((ViewGroup)child, ev);
                } else {
                    return child;
                }
            }
        }

        return null;
    }

    @Override
    public boolean dispatchTouchEvent (MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                m_DownX = event.getX();
                m_DownY = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                m_DownX = event.getX();
                m_DownY = event.getY();
                invalidate();

                View v = getChildUnderEvent(event);
                if (v instanceof DragDropListView) {
                    v.onTouchEvent(event);
                }

                break;

            case MotionEvent.ACTION_UP:
                //touchEventsEnded();
                unsetDragViewOrigin();
                invalidate();
                break;

            case MotionEvent.ACTION_CANCEL:
                //touchEventsCancelled();
                unsetDragViewOrigin();
                invalidate();
                break;

            case MotionEvent.ACTION_POINTER_UP:
                // If a multitouch event took place and the original touch dictating
                // the movement of the hover cell has ended, then the dragging event
                // ends and the hover cell is animated to its corresponding position
                // in the listview.
//                    pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >>
//                            MotionEvent.ACTION_POINTER_INDEX_SHIFT;
//                    final int pointerId = event.getPointerId(pointerIndex);
//                    if (pointerId == m_ActivePointerId) {
//                        //touchEventsEnded();
//                        m_SelectionMobile = false;
//                        invalidate();
//                    }
                unsetDragViewOrigin();
                invalidate();
                break;

            default:
                break;
        }

        // Use a ValueAnimator with PropertyValuesHolder instances to animate point X,Y. Everytime the frame is
        // drawn, call invalidate().

        return super.dispatchTouchEvent(event);
    }

}

package com.example.cliff.customlisttest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
    private DragData m_DragData;
    private MainActivity m_A;
    private DragDropListView m_lastListView;

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
        m_DragData = null;
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

    public DragData getDragData() {
        return m_DragData;
    }

    public void setDragData(DragData m_DragData) {
        this.m_DragData = m_DragData;
        //  1 - Hide the view and store it for later
        //  2 - Get the converted bitmap from the View tag data
        //  3 - Handle the view disposition given the last position in the drag action.
        //      Note that this step is handled in the private ViewGroup sub-class.
        setDragBitmap(m_DragData.b);
        m_DragData.itemView.setVisibility(View.INVISIBLE);
    }
    //endregio

    public void unsetDragData() {
        if (m_DroppedOnTarget == null || m_DroppedOnTarget == false) {
            // cw: Handle animation before this method called.
            if (m_DragData != null) {
                m_DragData.itemView.setVisibility(View.VISIBLE);
            }
        } else {
            // cw: If dropped on a target, then notify source to perform its required
            // operations for this event.
        }
        m_DragData = null;
        m_DroppedOnTarget = null;
        m_DragBitmap = null;
    }

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

    public View getChildUnderEvent(MotionEvent ev) {
        return getChildUnderEvent(this, ev);
    }

    public View getChildUnderEvent(ViewGroup v, MotionEvent ev) {
        int x = Math.round(ev.getX());
        int y = Math.round(ev.getY());
        for (int i = 0; i < v.getChildCount(); i++) {
            View child = v.getChildAt(i);

            if (
                x >= (v.getLeft() + child.getLeft()) &&
                x <= (v.getLeft() + child.getRight()) &&
                y >= (v.getTop() + child.getTop()) &&
                y <= (v.getTop() + child.getBottom())
            ) {
                // cw: Descend ViewGroup objects unless it is a ListView
                if (child instanceof ViewGroup && !(child instanceof ListView)) {
                    return getChildUnderEvent((ViewGroup) child, ev);
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

                m_A.setPosText(
                    "Pos: " +
                        "X = " + String.format("%f.1", Float.valueOf(m_DownX)) + " " +
                        "Y = " + String.format("%f.1", Float.valueOf(m_DownY))
                );

                invalidate();

                if (m_A.isCurrentlyDragging()) {
                    View v = getChildUnderEvent(event);
                    // cw: Only fire event if we are NOT the DragDropListView where touch originated.
                    if (v == null) {
                        if (m_lastListView != null) {
                            m_lastListView.onDragBlur();
                            m_lastListView = null;
                        }
                        break;
                    }

                    if (v instanceof DragDropListView) {
                        if (!v.equals(m_DragData.originView)) {
                            if (((DragDropListView) v).getDragTarget()) {
                                m_lastListView = (DragDropListView)v;
                                m_lastListView.onDragHover();

                            }
                        }
                    } else {
                        if (m_lastListView != null) {
                            m_lastListView.onDragBlur();
                            m_lastListView = null;
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                //touchEventsEnded();
                unsetDragData();
                m_A.resetBackgrounds();
                m_A.setPosText("");
                invalidate();
                break;

            case MotionEvent.ACTION_CANCEL:
                //touchEventsCancelled();
                unsetDragData();
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
                unsetDragData();
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

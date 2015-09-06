package com.example.cliff.customlisttest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.cliff.customlisttest.data.DragData;
import com.example.cliff.customlisttest.data.PlayerData;

public class MainActivity extends AppCompatActivity {

    private float m_DownX = -1;
    private float m_DownY = -1;
    private Bitmap m_DragBitmap;

    //region Private ViewGroup subclass. -- Handles selected ListItem dragging.
    private class DragRelativeLayout extends RelativeLayout {

        public DragRelativeLayout(Context context) {
            super(context);
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (m_DragBitmap != null) {
                canvas.drawBitmap(m_DragBitmap, m_DownX, m_DownY, null);
            }
        }

        @Override
        public boolean onTouchEvent (MotionEvent event) {

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    m_DownX = event.getX();
                    m_DownY = event.getY();
                    break;

                case MotionEvent.ACTION_MOVE:
                    m_DownX = event.getX();
                    m_DownY= event.getY();
                    invalidate();

                    // TODO: Handle cell swapping, if a drag target.
                    // TODO: Handle scrolling.

                    return false;

                case MotionEvent.ACTION_UP:
                    //touchEventsEnded();
                    invalidate();
                    break;

                case MotionEvent.ACTION_CANCEL:
                    //touchEventsCancelled();
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
                    break;

                default:
                    break;
            }

            // Use a ValueAnimator with PropertyValuesHolder instances to animate point X,Y. Everytime the frame is
            // drawn, call invalidate().

            return super.onTouchEvent(event);
        }

    }
    //endregion -- Handles

    private Context context;

    private DragRelativeLayout m_MainLayout;
    private DragDropListView m_DragViewOrigin;
    private DragData m_DragData;
    private Boolean m_DroppedOnTarget;

    private int m_BorderSize = 0;
    private int m_BorderColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        m_MainLayout = (DragRelativeLayout) RelativeLayout.inflate(context, R.layout.activity_main, null);
        //m_DragImage = (ImageView)m_MainLayout.findViewById(R.id.dragImage);

        // Other field initializers.
        m_DroppedOnTarget = null;
        m_DragViewOrigin = null;
        TypedArray array = context.getTheme().obtainStyledAttributes(
                new int[]{ android.R.attr.colorBackground }
        );
        m_BorderColor = array.getColor(0, 0xFF00FF);
        //m_DragImage = null;
        m_DragBitmap = null;

        CustomAdapter pla = new CustomAdapter(this);
        CustomAdapter sla = new CustomAdapter(this);
        DragDropListView lvPlayers = (DragDropListView)findViewById(R.id.playerList);
        DragDropListView lvSelects  = (DragDropListView)findViewById(R.id.selectionList);
        lvPlayers.setAdapter(pla);
        lvSelects.setAdapter(sla);
        String teams[] = {"ATL", "GB", "DAL", "NO", "SF", "CHI", "HOU", "TEN"};
        for (String t: teams) {
            pla.addItem(
                    new PlayerData("QB", "Robert", "Griffin III", t, 5, 72)
            );
        }
        pla.notifyDataSetChanged();

        // TODO: Get selections from retrieved service data.
        lvSelects.setDragTarget(true);

        Toast.makeText(
            getApplicationContext(),
            "Total number of Items are: " + String.valueOf(lvPlayers.getAdapter().getCount()),
            Toast.LENGTH_LONG
        ).show();
    }


    // cw:  Once again, Android confounds my efforts to put this Bitmap issue to bed.
    //      dispatchDraw() and onDraw() are for VIEW-derived classes and not part of the
    //      activity. *sigh*
    //
    //      Now I need to subclass the main laout in R.layout.activity_main, and add in support
    //      code to handle the simple 4-liner code in the onDraw() method below.
    //
    //      I'm all for code customization, but there really should be an easier way to accomplish
    //      this.
    //      -- 8/30/2015
    /*
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (m_DragViewOrigin != null) {
            canvas.drawBitmap(m_DragBitmap, m_DownX, m_DownY, null);
        }
    }
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //region GETTERS AND SETTERS
    //
    public DragData getDragData() {
        return m_DragData;
    }

    public void setDragData(DragData m_DragData) {
        this.m_DragData = m_DragData;
    }

    public DragDropListView getDragViewOrigin() {
        return m_DragViewOrigin;
    }

    public void setDragViewOrigin(DragDropListView m_DragViewOrigin) {
        this.m_DragViewOrigin = m_DragViewOrigin;

        //  1 - Hide the view and store it for later
        //  2 - Get the converted bitmap from the View tag data
        //  3 - Handle the view disposition given the last position in the drag action.
        //      Note that this step is handled in the private ViewGroup sub-class.
        DragData dd = (DragData)m_DragViewOrigin.getTag();
        m_DragBitmap = dd.b;
        m_DragViewOrigin.setVisibility(View.INVISIBLE);
    }

    public void unsetDragViewOrigin() {
        if (!m_DroppedOnTarget) {
            // cw: Handle animation before this method called.
            m_DragViewOrigin.setVisibility(View.VISIBLE);
        } else {
            // cw: If dropped on a target, then notify source to perform its required
            // operations for this event.
        }
        m_DragViewOrigin = null;
        m_DroppedOnTarget = null;
        m_DragBitmap = null;
    }
    //endregion
}

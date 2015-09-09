package com.example.cliff.customlisttest;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cliff.customlisttest.data.DragData;
import com.example.cliff.customlisttest.data.PlayerData;

public class MainActivity extends AppCompatActivity {

    private Context context;

    private DragRelativeLayout m_MainLayout;
    private TextView m_PosText;
    private DragDropListView m_PlayerList, m_SelectionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        m_MainLayout = (DragRelativeLayout)findViewById(R.id.mainLayoutID);
        m_PosText = (TextView)findViewById(R.id.dragPositionText);
        setPosText("");

        CustomAdapter pla = new CustomAdapter(this);
        CustomAdapter sla = new CustomAdapter(this);
        m_PlayerList = (DragDropListView)findViewById(R.id.playerList);
        m_SelectionList  = (DragDropListView)findViewById(R.id.selectionList);

        m_PlayerList.setAdapter(pla);
        m_SelectionList.setAdapter(sla);
        String teams[] = {"ATL", "GB", "DAL", "NO", "SF", "CHI", "HOU", "TEN"};
        for (String t: teams) {
            pla.addItem(
                    new PlayerData("QB", "Robert", "Griffin III", t, 5, 72)
            );
        }
        pla.notifyDataSetChanged();

        // TODO: Get selections from retrieved service data.
        m_SelectionList.setDragTarget(true);
        m_SelectionList.setSortable(true);

        Toast.makeText(
            getApplicationContext(),
            "Total number of Items are: " + String.valueOf(m_SelectionList.getAdapter().getCount()),
            Toast.LENGTH_LONG
        ).show();
    }

    public void resetBackgrounds() {
        m_PlayerList.resetBackground();
        m_SelectionList.resetBackground();
    }

    public int getThemeBackgroundColor() {
        TypedArray array = context.getTheme().obtainStyledAttributes(
                new int[]{ android.R.attr.colorBackground }
        );

        return array.getColor(0, 0xFF00FF);
    }

    public void setPosText(String text) {
        m_PosText.setText(text);
    }

    public boolean isCurrentlyDragging() {
        return m_MainLayout.getDragBitmap() != null;
    }

    //region Auto-generated code
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
    //endregion

    //region GETTERS AND SETTERS
    //
    public void setDragData(DragData dd) {
        m_MainLayout.setDragData(dd);
    }

    public DragData getDragData () {
        return m_MainLayout.getDragData();
    }

    public void setDragViewOrigin(DragData dd) {
        m_MainLayout.setDragData(dd);
    }
   //endregion
}

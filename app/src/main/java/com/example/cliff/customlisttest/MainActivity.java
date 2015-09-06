package com.example.cliff.customlisttest;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.cliff.customlisttest.data.DragData;
import com.example.cliff.customlisttest.data.PlayerData;

public class MainActivity extends AppCompatActivity {

    private Context context;

    private DragRelativeLayout m_MainLayout;
    private DragData m_DragData;

    private int m_BorderSize = 0;
    private int m_BorderColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        m_MainLayout = (DragRelativeLayout)findViewById(R.id.mainLayoutID);

        // Other field initializers.
        TypedArray array = context.getTheme().obtainStyledAttributes(
            new int[]{ android.R.attr.colorBackground }
        );
        m_BorderColor = array.getColor(0, 0xFF00FF);
        //m_DragImage = null;

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

    public View getDragViewOrigin() {
        return m_MainLayout.getDragViewOrigin();
    }

    public void setDragViewOrigin(View m_DragViewOrigin) {
        m_MainLayout.setDragViewOrigin(m_DragViewOrigin);
    }
   //endregion
}

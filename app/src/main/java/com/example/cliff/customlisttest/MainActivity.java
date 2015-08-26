package com.example.cliff.customlisttest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.cliff.customlisttest.data.DragData;
import com.example.cliff.customlisttest.data.PlayerData;

public class MainActivity extends AppCompatActivity {

    private Context context;

    private ImageView m_ImageDrag;
    private View m_MainLayout;
    private DragData m_DragData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        m_MainLayout = RelativeLayout.inflate(context, R.layout.activity_main, null);
        m_ImageDrag = (ImageView)m_MainLayout.findViewById(R.id.dragImage);

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

        lvPlayers.setDragImage(m_ImageDrag);
        lvSelects.setDragImage(m_ImageDrag);
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
    //endregion
}

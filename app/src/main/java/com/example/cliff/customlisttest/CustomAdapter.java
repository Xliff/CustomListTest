package com.example.cliff.customlisttest;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cliff.customlisttest.data.DragData;
import com.example.cliff.customlisttest.data.PlayerData;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Cliff on 8/9/2015.
 */
public class CustomAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private String m_PackageName;
    private Resources m_res;
    //private Context m_Context;

    static private final int LINE_THICKNESS = 1;

    ArrayList<PlayerData> m_Items;

    public CustomAdapter(Context context) {
        super();
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        m_PackageName = context.getPackageName();
        m_res = context.getResources();
        m_Items = new ArrayList<>();
        //m_Context = context;
    }

    // region ADD methods.
    public void addItem(PlayerData pd, int pos) {
        if (pos < m_Items.size()) {
            m_Items.add(pos, pd);
        } else {
            m_Items.add(pd);
        }
        notifyDataSetChanged();
    }

    public void addItem(PlayerData pd) {
        m_Items.add(pd);
        notifyDataSetChanged();
    }
    //endregion

    // RETRIEVE method
    @Override
    public Object getItem(int position) {
        return m_Items.get(position);
    }

    // region REMOVE methods
    public void removeItem(int pos) {
        m_Items.remove(pos);
        notifyDataSetChanged();
    }

    public void clear(){
        m_Items.clear();
        notifyDataSetChanged();
    }
    //endregion

    @Override
    public int getCount() {
        // Return item count.
        return m_Items.size();
    }

    @Override
    public long getItemId(int position) {
        // cw: This is almost certainly wrong.
        return position;
    }

    // cw: We use a public facing version of getView() to insure we have a clean view rather
    // than trying to re-use the selected View from the control.
    public View getViewFromPos(ViewGroup parent, int pos) {
        PlayerData pd = m_Items.get(pos);

        View theView = mInflater.inflate(R.layout.custom_row, parent, false);
        DragData vh = new DragData();

        vh.tv_pos = (TextView) theView.findViewById(R.id.t_Pos);
        vh.tv_fn = (TextView) theView.findViewById(R.id.t_firstName);
        vh.tv_ln = (TextView) theView.findViewById(R.id.t_lastName);
        vh.tv_bye = (TextView) theView.findViewById(R.id.t_byeWeek);
        vh.tv_rank = (TextView) theView.findViewById(R.id.t_adpRank);
        vh.i_Team = (ImageView) theView.findViewById(R.id.i_Team);
        vh.pd = pd;
        theView.setTag(vh);

        return theView;
    }

    public void populateRowView(View theView) throws IOException {
        DragData vh = (DragData) theView.getTag();

        // Set background -- when we move this to an app, we will have to perform error checking.
        String bgColor = m_res.getString(
                m_res.getIdentifier("background_" + vh.pd.team, "string", m_PackageName)
        );
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.parseColor(bgColor), Color.parseColor("#111111")}
        );
        theView.setBackground(gd);

        // Set team icon.
        AssetManager am = m_res.getAssets();
        InputStream iData;
        iData = am.open("Helmet-" + vh.pd.team + ".png");
        vh.i_Team.setImageBitmap(BitmapFactory.decodeStream(iData));

        // Set text data.
        vh.tv_pos.setText(vh.pd.pos);
        vh.tv_fn.setText(vh.pd.firstname);
        vh.tv_ln.setText(vh.pd.lastname);
        vh.tv_bye.setText("Bye: " + vh.pd.bye);
        vh.tv_rank.setText("#" + vh.pd.rank);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // cw: This totally changes what I thought was going on here.
        // The current implementation should be correct, but the need for the DragData class is
        // now in question, should probably be moved back here.
        // -- 8/25/2015
        DragData vh;
        PlayerData pd = m_Items.get(position);
        int w, h;

        if (convertView == null) {
            convertView = getViewFromPos(parent, position);
        }

        vh = (DragData) convertView.getTag();
        vh.pd = pd;

        if (
            vh.width == 0 && vh.height == 0 &&
            (w = convertView.getWidth()) > 0 && (h = convertView.getHeight()) > 0
        ) {
            vh.width = w;
            vh.height = h;
        }
        convertView.setTag(vh);
        try {
            populateRowView(convertView);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return convertView;
    }

}


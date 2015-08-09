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

import com.example.cliff.customlisttest.data.PlayerData;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Cliff on 8/9/2015.
 */
public class CustomAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    String packageName;
    Resources res;

    ArrayList<PlayerData> items = new ArrayList<>();

    public CustomAdapter(Context context) {
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        packageName = context.getPackageName();
        res = context.getResources();
    }

    // Need an add method!
    public void addItem(PlayerData pd) {
        items.add(pd);
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub

        // Return item count.
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder vh;
        vh = new ViewHolder();

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_row, parent, false);

            vh.pd = items.get(position);

            // Set background -- when we move this to an app, we will have to perform error checking.
            String bgColor = res.getString(
                res.getIdentifier("background_" + vh.pd.team, "string", packageName)
            );
            GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] { Color.parseColor(bgColor), 0 }
            );
            convertView.setBackground(gd);

            // Set team icon.
            AssetManager am = res.getAssets();
            InputStream iData = null;
            vh.i_Team = (ImageView) convertView.findViewById(R.id.i_Team);
            try {
                iData = am.open("Helmet-" + vh.pd.team + ".png");
                vh.i_Team.setImageBitmap(BitmapFactory.decodeStream(iData));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            // Set text data.
            vh.tv_pos = (TextView)convertView.findViewById(R.id.t_Pos);
            vh.tv_pos.setText(vh.pd.pos);
            vh.tv_fn = (TextView)convertView.findViewById(R.id.t_firstName);
            vh.tv_fn.setText(vh.pd.firstname);
            vh.tv_ln = (TextView)convertView.findViewById(R.id.t_lastName);
            vh.tv_ln.setText(vh.pd.lastname);
            vh.tv_bye = (TextView)convertView.findViewById(R.id.t_byeWeek);
            vh.tv_bye.setText("Bye: " + vh.pd.bye);
            vh.tv_rank = (TextView)convertView.findViewById(R.id.t_adpRank);
            vh.tv_rank.setText("#" + vh.pd.rank);

            convertView.setTag(vh);
        }

        return convertView;
    }

    class ViewHolder {
        ImageView i_Team;
        TextView tv_pos, tv_fn, tv_ln, tv_bye, tv_rank;
        PlayerData pd;
    }

}


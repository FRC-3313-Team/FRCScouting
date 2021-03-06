package com.team3313.frcscouting.fragments;

/**
 * Created by oa10712 on 3/7/2018.
 */

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.team3313.frcscouting.DataStore;
import com.team3313.frcscouting.MainActivity;
import com.team3313.frcscouting.R;
import com.team3313.frcscouting.components.TextViewExtra;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Random;


public class TeamListFragment extends Fragment {
    private final static int COLUMNS = 6;
    LinearLayout topLayout;
    View.OnClickListener teamClick;

    public TeamListFragment() {
        teamClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = MainActivity.instance.getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, TeamDataFragment.newInstance("frc" + (String) ((TextView) v).getText())).commit();
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        topLayout = new LinearLayout(getContext());

        TableLayout tableLayout = new TableLayout(getContext());
        DataStore.teamData.length();
        int r = (int) Math.ceil(DataStore.teamData.length() / COLUMNS);
        JSONArray names = DataStore.teamData.names();
        for (int i = 0; i < COLUMNS; i++) {
            tableLayout.setColumnStretchable(i, true);
        }

        TableRow header = new TableRow(getContext());
        header.addView(new TextViewExtra(getContext(), "Red 1", null));
        header.addView(new TextViewExtra(getContext(), "Red 2", null));
        header.addView(new TextViewExtra(getContext(), "Red 3", null));
        header.addView(new TextViewExtra(getContext(), "Blue 1", null));
        header.addView(new TextViewExtra(getContext(), "Blue 2", null));
        header.addView(new TextViewExtra(getContext(), "Blue 3", null));
tableLayout.addView(header);
        for (int i = 0; i < r + 1; i++) {
            TableRow row = new TableRow(getContext());

            for (int c = 0; c < COLUMNS; c++) {
                try {
                    TextView view = new TextView(getContext());
                    view.setText(DataStore.teamData.getJSONObject(names.getString(i * COLUMNS + c)).getString("number").substring(3));
                    view.setMinHeight(50);
                    view.setGravity(Gravity.CENTER);
                    int color = Color.WHITE;
                    if(c % 2 == 0){
                        color = Color.LTGRAY;
                    }
                    if(i % 2 == 0){
                        if(color == Color.WHITE){
                            color = Color.LTGRAY;
                        }
                        else{
                            color = 0xffaaaaaa;
                        }
                    }
                    view.setBackgroundColor(color);
                    view.setOnClickListener(teamClick);
                    row.addView(view);
                } catch (Exception e) {
                }
            }
            tableLayout.addView(row);
        }
        topLayout.addView(tableLayout);
        return topLayout;
    }
}
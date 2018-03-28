package com.team3313.frcscouting.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.team3313.frcscouting.DataStore;
import com.team3313.frcscouting.MainActivity;
import com.team3313.frcscouting.components.TeamButtons;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by oa10712 on 3/12/2018.
 */

public class TeamPRFragment extends TeamFragment {
    private static final String ARG_TEAM_KEY = "frc3313";
    public CheckBox flowersBox;
    public CheckBox chairmansBox;
    public CheckBox safetyBox;
    public CheckBox entrepreneurshipBox;
    public CheckBox deansBox;
    public ArrayList<TableRow> socialRows = new ArrayList();
    LinearLayout linearLayout;
    LinearLayout prLayout;
    TableLayout awards;
    ScrollView scroller;

    public TeamPRFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment TeamPRFragment.
     */
    public static TeamPRFragment newInstance(String param1) {
        TeamPRFragment fragment = new TeamPRFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEAM_KEY, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            teamKey = getArguments().getString(ARG_TEAM_KEY);
        }
        MainActivity.instance.setTitle("Team " + teamKey + ", " + DataStore.getTeamField(teamKey, "name") + " - PR");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        JSONObject teamData = new JSONObject();
        scroller = new ScrollView(getContext());
        try {
            teamData = DataStore.teamData.getJSONObject(teamKey).getJSONObject("pit");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout buttonRow = new TeamButtons(getContext(), this);
        linearLayout.addView(buttonRow);

        prLayout = new LinearLayout(getContext());
        prLayout.setOrientation(LinearLayout.HORIZONTAL);

        awards = new TableLayout(getContext());

        TableRow chairmansRow = new TableRow(getContext());
        TextView chairmansText = new TextView(getContext());
        chairmansText.setText("Chairman's");
        chairmansRow.addView(chairmansText);
        chairmansBox = new CheckBox(getContext());
        try {
            chairmansBox.setChecked(teamData.getJSONObject("awards").getBoolean("chairmans"));
        } catch (Exception ex) {
        }
        chairmansRow.addView(chairmansBox);
        awards.addView(chairmansRow);


        TableRow flowersRow = new TableRow(getContext());
        TextView flowersText = new TextView(getContext());
        flowersText.setText("Woodie Flowers'");
        flowersRow.addView(flowersText);
        flowersBox = new CheckBox(getContext());
        try {
            flowersBox.setChecked(teamData.getJSONObject("awards").getBoolean("flowers"));
        } catch (Exception ex) {
        }
        flowersRow.addView(flowersBox);
        awards.addView(flowersRow);


        TableRow deansRow = new TableRow(getContext());
        TextView deansText = new TextView(getContext());
        deansText.setText("Deans List");
        deansRow.addView(deansText);
        deansBox = new CheckBox(getContext());
        try {
            deansBox.setChecked(teamData.getJSONObject("awards").getBoolean("deans"));
        } catch (Exception ex) {
        }
        deansRow.addView(deansBox);
        awards.addView(deansRow);


        TableRow entrepreneurshipRow = new TableRow(getContext());
        TextView entrepreneurshipText = new TextView(getContext());
        entrepreneurshipText.setText("Entrepreneurship");
        entrepreneurshipRow.addView(entrepreneurshipText);
        entrepreneurshipBox = new CheckBox(getContext());
        try {
            entrepreneurshipBox.setChecked(teamData.getJSONObject("awards").getBoolean("entrepreneurship"));
        } catch (Exception ex) {
        }
        entrepreneurshipRow.addView(entrepreneurshipBox);
        awards.addView(entrepreneurshipRow);


        TableRow safetyRow = new TableRow(getContext());
        TextView safetyText = new TextView(getContext());
        safetyText.setText("Safety");
        safetyRow.addView(safetyText);
        safetyBox = new CheckBox(getContext());
        try {
            safetyBox.setChecked(teamData.getJSONObject("awards").getBoolean("safety"));
        } catch (Exception ex) {
        }
        safetyRow.addView(safetyBox);
        awards.addView(safetyRow);


        prLayout.addView(awards);


        final TableLayout social = new TableLayout(getContext());
        social.setStretchAllColumns(true);

        Button addRow = new Button(getContext());
        addRow.setText("Add social row");
        addRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TableRow row = new TableRow(getContext());
                EditText site = new EditText(getContext());
                row.addView(site);
                EditText handle = new EditText(getContext());
                row.addView(handle);
                socialRows.add(row);
                social.addView(row);
            }
        });
        prLayout.addView(addRow);

        TableRow header = new TableRow(getContext());
        TextView siteText = new TextView(getContext());
        siteText.setText("Site");
        header.addView(siteText);
        TextView handleText = new TextView(getContext());
        handleText.setText("Handle");
        header.addView(handleText);
        social.addView(header);


        JSONArray socialData = new JSONArray();
        try {
            socialData = teamData.getJSONArray("social");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < socialData.length(); i++) {
            try {
                TableRow row = new TableRow(getContext());
                EditText site = new EditText(getContext());
                site.setText(socialData.getJSONObject(i).getString("site"));
                row.addView(site);
                EditText handle = new EditText(getContext());
                handle.setText(socialData.getJSONObject(i).getString("handle"));
                row.addView(handle);
                socialRows.add(row);
                social.addView(row);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        scroller.addView(social);
        prLayout.addView(scroller);


        linearLayout.addView(prLayout);
        return linearLayout;
    }

}

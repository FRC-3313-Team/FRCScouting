package com.team3313.frcscouting.components;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;

import com.team3313.frcscouting.DataStore;
import com.team3313.frcscouting.MainActivity;
import com.team3313.frcscouting.R;
import com.team3313.frcscouting.fragments.TeamDataFragment;
import com.team3313.frcscouting.fragments.TeamFragment;
import com.team3313.frcscouting.fragments.TeamMatchesFragment;
import com.team3313.frcscouting.fragments.TeamNotesFragment;
import com.team3313.frcscouting.fragments.TeamPRFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by oa10712 on 3/13/2018.
 */

public class TeamButtons extends LinearLayout {
    public TeamButtons(Context context, final TeamFragment fragment) {
        super(context);
        setOrientation(LinearLayout.HORIZONTAL);
        Button dataButton = new Button(getContext());
        dataButton.setText("Data");
        if (fragment instanceof TeamDataFragment) {
            dataButton.setEnabled(false);
        } else {
            dataButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = MainActivity.instance.getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, TeamDataFragment.newInstance(fragment.teamKey)).commit();
                }
            });
        }
        addView(dataButton);
        Button matchesButton = new Button(fragment.getActivity());
        matchesButton.setText("Matches");
        if (fragment instanceof TeamMatchesFragment) {
            matchesButton.setEnabled(false);
        } else {
            matchesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = MainActivity.instance.getSupportFragmentManager();
                    TeamMatchesFragment schedule = TeamMatchesFragment.newInstance(fragment.teamKey);
                    fragmentManager.beginTransaction().replace(R.id.content_frame, schedule).commit();
                }
            });
        }
        addView(matchesButton);
        Button notesButton = new Button(fragment.getActivity());
        notesButton.setText("Notes");
        if (fragment instanceof TeamNotesFragment) {
            notesButton.setEnabled(false);
        } else {
            notesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = MainActivity.instance.getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, TeamNotesFragment.newInstance(fragment.teamKey)).commit();
                }
            });
        }
        addView(notesButton);
        Button prButton = new Button(getContext());
        prButton.setText("PR");
        if (fragment instanceof TeamPRFragment) {
            prButton.setEnabled(false);
            Button saveButton = new Button(getContext());
            saveButton.setText("Save PR Data");
            saveButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    TeamPRFragment frag = (TeamPRFragment) fragment;
                    try {
                        JSONObject pit = new JSONObject();
                        JSONObject awards = new JSONObject();
                        awards.put("chairmans", frag.chairmansBox.isChecked());
                        awards.put("flowers", frag.flowersBox.isChecked());
                        awards.put("deans", frag.deansBox.isChecked());
                        awards.put("safety", frag.safetyBox.isChecked());
                        awards.put("entrepreneurship", frag.entrepreneurshipBox.isChecked());
                        pit.put("awards", awards);
                        JSONArray social = new JSONArray();
                        for (TableRow r : frag.socialRows) {
                            EditText site = (EditText) r.getChildAt(0);
                            EditText handle = (EditText) r.getChildAt(1);
                            JSONObject row = new JSONObject();
                            if (!site.getText().toString().equalsIgnoreCase("")) {
                                row.put("site", site.getText());
                                row.put("handle", handle.getText());
                                social.put(row);
                            }
                        }
                        pit.put("social", social);
                        pit.put("updated", true);
                        DataStore.teamData.getJSONObject(frag.teamKey).put("pit", pit);
                        System.out.println("Saving award data");
                        DataStore.uploadPitData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            addView(saveButton);
        } else {
            prButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = MainActivity.instance.getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, TeamPRFragment.newInstance(fragment.teamKey)).commit();
                }
            });
        }
        addView(prButton);
    }
}

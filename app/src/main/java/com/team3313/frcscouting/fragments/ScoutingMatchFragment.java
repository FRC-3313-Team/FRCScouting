package com.team3313.frcscouting.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.team3313.frcscouting.MainActivity;
import com.team3313.frcscouting.R;
import com.team3313.frcscouting.components.NumberPicker;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScoutingMatchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScoutingMatchFragment extends Fragment {

    LinearLayout linearLayout;
    LinearLayout matchSep;
    TableLayout autoLayout;
    TableLayout teleLayout;

    CheckBox autoSwitchBox;
    CheckBox autoScaleBox;
    CheckBox autoCrossBox;

    NumberPicker scalePicker;
    NumberPicker switchPicker;
    NumberPicker exchangePicker;
    CheckBox teleClimbBox;
    // TODO: Rename and change types of parameters
    private JSONObject data;


    public ScoutingMatchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param start Parameter 1.
     * @return A new instance of fragment ScoutingMatchFragment.
     */
    public static Fragment newInstance(JSONObject start) {
        ScoutingMatchFragment fragment = new ScoutingMatchFragment();
        fragment.data = start;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.instance.setTitle("Match " + getData("match_key", String.class) + " - Watching " + getData("team_key", String.class));
        linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);


        TableRow.LayoutParams pickParam = new TableRow.LayoutParams();
        pickParam.width = TableRow.LayoutParams.WRAP_CONTENT;
        pickParam.height = TableRow.LayoutParams.WRAP_CONTENT;

        TableRow.LayoutParams labelParam = new TableRow.LayoutParams();
        labelParam.gravity = Gravity.CENTER_VERTICAL;

        matchSep = new LinearLayout(getContext());
        matchSep.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout buttonRow = new LinearLayout(getActivity());
        buttonRow.setOrientation(LinearLayout.HORIZONTAL);
        Button matchButton = new Button(getActivity());
        matchButton.setText("Match");
        matchButton.setClickable(false);
        buttonRow.addView(matchButton);
        Button notesButton = new Button(getActivity());
        notesButton.setText("Notes");
        notesButton.setClickable(true);
        notesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    data.getJSONObject("auto").put("switch", autoSwitchBox.isChecked());
                    data.getJSONObject("auto").put("scale", autoScaleBox.isChecked());
                    data.getJSONObject("auto").put("passedLine", autoCrossBox.isChecked());

                    data.getJSONObject("tele").put("scale", scalePicker.getValue());
                    data.getJSONObject("tele").put("switch", switchPicker.getValue());
                    data.getJSONObject("tele").put("exchange", exchangePicker.getValue());
                    data.getJSONObject("tele").put("climb", teleClimbBox.isChecked());
                } catch (JSONException e) {
                }
                FragmentManager fragmentManager = MainActivity.instance.getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, ScoutingNotesFragment.newInstance(data)).commit();
            }
        });
        buttonRow.addView(notesButton);
        linearLayout.addView(buttonRow);

        autoLayout = new TableLayout(getContext());

        TableRow autoLabelRow = new TableRow(getContext());
        TextView autoLabel = new TextView(getContext());
        autoLabel.setText("Autonomous");
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.span = 2; //amount of columns you will span
        params.gravity = Gravity.CENTER_HORIZONTAL;
        autoLabel.setLayoutParams(params);
        autoLabelRow.addView(autoLabel);
        autoLayout.addView(autoLabelRow);

        TableRow autoSwitch = new TableRow(getContext());
        TextView autoCubeLabel = new TextView(getContext());
        autoCubeLabel.setText("Cube in Switch");
        autoSwitch.addView(autoCubeLabel);
        autoSwitchBox = new CheckBox(getContext());
        try {
            autoSwitchBox.setChecked(data.getJSONObject("auto").getBoolean("switch"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        autoSwitch.addView(autoSwitchBox);
        autoLayout.addView(autoSwitch);

        TableRow autoScale = new TableRow(getContext());
        TextView autoScaleLabel = new TextView(getContext());
        autoScaleLabel.setText("Cube in Scale");
        autoScale.addView(autoScaleLabel);
        autoScaleBox = new CheckBox(getContext());
        try {
            autoScaleBox.setChecked(data.getJSONObject("auto").getBoolean("scale"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        autoScale.addView(autoScaleBox);
        autoLayout.addView(autoScale);

        TableRow autoCross = new TableRow(getContext());
        TextView autoCrossLabel = new TextView(getContext());
        autoCrossLabel.setText("Crossed Auto Line");
        autoCross.addView(autoCrossLabel);
        autoCrossBox = new CheckBox(getContext());
        try {
            autoCrossBox.setChecked(data.getJSONObject("auto").getBoolean("passedLine"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        autoCross.addView(autoCrossBox);
        autoLayout.addView(autoCross);

        matchSep.addView(autoLayout);


        teleLayout = new TableLayout(getContext());

        TableRow teleRow = new TableRow(getContext());
        TextView teleLabel = new TextView(getContext());
        teleLabel.setText("Tele-Operated");
        params = new TableRow.LayoutParams();
        params.span = 4; //amount of columns you will span
        params.gravity = Gravity.CENTER_HORIZONTAL;
        teleLabel.setLayoutParams(params);
        teleRow.addView(teleLabel);
        teleLayout.addView(teleRow);

        TableRow scaleCubes = new TableRow(getContext());
        TextView scaleCubesLabel = new TextView(getContext());
        scaleCubesLabel.setText("Cubes on Scale");
        scaleCubesLabel.setLayoutParams(labelParam);
        scaleCubes.addView(scaleCubesLabel);
        scalePicker = new NumberPicker(getContext(), null);
        try {
            scalePicker.setValue(data.getJSONObject("tele").getInt("scale"));
        } catch (JSONException e) {
        }
        scalePicker.setLayoutParams(pickParam);
        scaleCubes.addView(scalePicker);
        teleLayout.addView(scaleCubes);


        TableRow switchCubes = new TableRow(getContext());
        TextView switchCubesLabel = new TextView(getContext());
        switchCubesLabel.setText("Cubes on Switch");
        switchCubesLabel.setLayoutParams(labelParam);
        switchCubes.addView(switchCubesLabel);
        switchPicker = new NumberPicker(getContext(), null);
        try {
            switchPicker.setValue(data.getJSONObject("tele").getInt("switch"));
        } catch (JSONException e) {
        }
        switchPicker.setLayoutParams(pickParam);
        switchCubes.addView(switchPicker);
        teleLayout.addView(switchCubes);


        TableRow exchangeCubes = new TableRow(getContext());
        TextView exchangeCubesLabel = new TextView(getContext());
        exchangeCubesLabel.setText("Cubes in Exchange");
        exchangeCubesLabel.setLayoutParams(labelParam);
        exchangeCubes.addView(exchangeCubesLabel);
        exchangePicker = new NumberPicker(getContext(), null);
        try {
            exchangePicker.setValue(data.getJSONObject("tele").getInt("exchange"));
        } catch (JSONException e) {
        }
        exchangePicker.setLayoutParams(pickParam);
        exchangeCubes.addView(exchangePicker);
        teleLayout.addView(exchangeCubes);

        TableRow teleClimb = new TableRow(getContext());
        TextView teleClimbLabel = new TextView(getContext());
        teleClimbLabel.setText("Succesful Climb");
        teleClimb.addView(teleClimbLabel);
        teleClimbBox = new CheckBox(getContext());
        try {
            teleClimbBox.setChecked(data.getJSONObject("tele").getBoolean("climb"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        teleClimb.addView(teleClimbBox);
        teleLayout.addView(teleClimb);


        matchSep.addView(teleLayout);


        linearLayout.addView(matchSep);
        return linearLayout;
    }

    private <T> T getData(String key, Class<T> clazz) {
        try {
            return (T) data.get(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

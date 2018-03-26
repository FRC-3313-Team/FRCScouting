package com.team3313.frcscouting.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.team3313.frcscouting.MainActivity;
import com.team3313.frcscouting.components.MatchButtons;
import com.team3313.frcscouting.components.NumberPicker;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScoutingMatchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScoutingMatchFragment extends ScoutingFragment {

    public CheckBox autoSwitchBox;
    public CheckBox autoScaleBox;
    public CheckBox autoCrossBox;
    public NumberPicker scalePicker;
    public NumberPicker switchPicker;
    public NumberPicker exchangePicker;
    public CheckBox teleClimbBox;
    LinearLayout linearLayout;
    LinearLayout buttonRow;
    LinearLayout matchSep;
    TableLayout tableHolder;
    // TODO: Rename and change types of parameters

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param start Parameter 1.
     * @return A new instance of fragment ScoutingMatchFragment.
     */
    public static ScoutingMatchFragment newInstance(JSONObject start) {
        ScoutingMatchFragment fragment = new ScoutingMatchFragment();
        fragment.data = start;
        try {
            fragment.data.put("updated", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.instance.setTitle(getData("match_key", String.class).split("_")[1].replace("qm", "Qualifier Match ") + " - Watching Team " + getData("team_key", String.class).substring(3));
        linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TableRow.LayoutParams pickParam = new TableRow.LayoutParams();
        pickParam.width = TableRow.LayoutParams.WRAP_CONTENT;
        pickParam.height = TableRow.LayoutParams.WRAP_CONTENT;

        TableRow.LayoutParams labelParam = new TableRow.LayoutParams();
        labelParam.gravity = Gravity.CENTER_VERTICAL;

        matchSep = new LinearLayout(getContext());
        matchSep.setOrientation(LinearLayout.HORIZONTAL);

        buttonRow = new MatchButtons(getContext(), this);
        linearLayout.addView(buttonRow);

        tableHolder = new TableLayout(getContext());
        tableHolder.setColumnStretchable(2, true);
        tableHolder.setColumnStretchable(5, true);


        TableRow labelRow = new TableRow(getContext());
        TextView autoLabel = new TextView(getContext());
        autoLabel.setText("Autonomous");
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.span = 2; //amount of columns you will span
        params.gravity = Gravity.CENTER_HORIZONTAL;
        autoLabel.setLayoutParams(params);
        labelRow.addView(autoLabel);

        labelRow.addView(new Space(getContext()));

        TextView teleLabel = new TextView(getContext());
        teleLabel.setText("Tele-Operated");
        params = new TableRow.LayoutParams();
        params.span = 2; //amount of columns you will span
        params.gravity = Gravity.CENTER_HORIZONTAL;
        teleLabel.setLayoutParams(params);
        labelRow.addView(teleLabel);

        labelRow.addView(new Space(getContext()));

        tableHolder.addView(labelRow);

        TableRow switchRow = new TableRow(getContext());
        TextView autoCubeLabel = new TextView(getContext());
        autoCubeLabel.setText("Cube in Switch");
        switchRow.addView(autoCubeLabel);

        autoSwitchBox = new CheckBox(getContext());
        try {
            autoSwitchBox.setChecked(data.getJSONObject("auto").getBoolean("switch"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        switchRow.addView(autoSwitchBox);

        switchRow.addView(new Space(getContext()));

        TextView switchCubesLabel = new TextView(getContext());
        switchCubesLabel.setText("Cubes on Switch");
        switchCubesLabel.setLayoutParams(labelParam);
        switchRow.addView(switchCubesLabel);
        switchPicker = new NumberPicker(getContext(), null);
        try {
            switchPicker.setValue(data.getJSONObject("tele").getInt("switch"));
        } catch (JSONException e) {
        }
        switchPicker.setLayoutParams(pickParam);
        switchRow.addView(switchPicker);

        tableHolder.addView(switchRow);

        TableRow scaleRow = new TableRow(getContext());
        TextView autoScaleLabel = new TextView(getContext());
        autoScaleLabel.setText("Cube in Scale");
        scaleRow.addView(autoScaleLabel);
        autoScaleBox = new CheckBox(getContext());
        try {
            autoScaleBox.setChecked(data.getJSONObject("auto").getBoolean("scale"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        scaleRow.addView(autoScaleBox);

        scaleRow.addView(new Space(getContext()));

        TextView scaleCubesLabel = new TextView(getContext());
        scaleCubesLabel.setText("Cubes on Scale");
        scaleCubesLabel.setLayoutParams(labelParam);
        scaleRow.addView(scaleCubesLabel);
        scalePicker = new NumberPicker(getContext(), null);
        try {
            scalePicker.setValue(data.getJSONObject("tele").getInt("scale"));
        } catch (JSONException e) {
        }
        scalePicker.setLayoutParams(pickParam);
        scaleRow.addView(scalePicker);

        tableHolder.addView(scaleRow);

        TableRow otherRow = new TableRow(getContext());
        TextView autoCrossLabel = new TextView(getContext());
        autoCrossLabel.setText("Crossed Auto Line");
        otherRow.addView(autoCrossLabel);
        autoCrossBox = new CheckBox(getContext());
        try {
            autoCrossBox.setChecked(data.getJSONObject("auto").getBoolean("passedLine"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        otherRow.addView(autoCrossBox);

        otherRow.addView(new Space(getContext()));


        TextView exchangeCubesLabel = new TextView(getContext());
        exchangeCubesLabel.setText("Cubes in Exchange");
        exchangeCubesLabel.setLayoutParams(labelParam);
        otherRow.addView(exchangeCubesLabel);
        exchangePicker = new NumberPicker(getContext(), null);
        try {
            exchangePicker.setValue(data.getJSONObject("tele").getInt("exchange"));
        } catch (JSONException e) {
        }
        exchangePicker.setLayoutParams(pickParam);
        otherRow.addView(exchangePicker);

        tableHolder.addView(otherRow);

        TableRow teleClimb = new TableRow(getContext());
        teleClimb.addView(new Space(getContext()));
        teleClimb.addView(new Space(getContext()));
        teleClimb.addView(new Space(getContext()));
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
        tableHolder.addView(teleClimb);

        matchSep.addView(tableHolder);

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

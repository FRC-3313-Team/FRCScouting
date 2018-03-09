package com.team3313.frcscouting.fragments;

/**
 * Created by oa10712 on 3/7/2018.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.team3313.frcscouting.MainActivity;
import com.team3313.frcscouting.R;

import org.json.JSONException;


public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    TableLayout tableLayout;
    ScrollView scrollView;
    Spinner spinner;
    EditText apiKey;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tableLayout = new TableLayout(getContext());
        scrollView = new ScrollView(getContext());

        TableRow stationRow = new TableRow(getContext());
        TextView spinnerLabel = new TextView(getContext());
        spinnerLabel.setText("Driver Station to Watch: ");
        stationRow.addView(spinnerLabel);
        spinner = new Spinner(getContext());
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.driver_station, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner.
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        try {
            spinner.setSelection(MainActivity.config.getInt("station"));
        } catch (JSONException e) {
        }
        stationRow.addView(spinner);


        TableRow apiRow = new TableRow(getActivity());
        TextView apiLabel = new TextView(getContext());
        apiLabel.setText("API Key");
        apiRow.addView(apiLabel);
        apiKey = new EditText(getContext());
        apiKey.setWidth(300);
        try {
            apiKey.setText(MainActivity.config.getString("apiKey"));
        } catch (JSONException e) {
            apiKey.setText("");
        }
        apiRow.addView(apiKey);

        TableRow saveRow = new TableRow(getContext());
        Button saveButton = new Button(getActivity());
        saveButton.setText("Save changes");
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MainActivity.config.put("apiKey", apiKey.getText().toString());
                    MainActivity.instance.saveConfig();
                    Toast.makeText(getContext(), "Config Saved", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                }
            }
        });
        saveRow.addView(saveButton);

        tableLayout.addView(stationRow);
        tableLayout.addView(apiRow);
        tableLayout.addView(saveRow);
        scrollView.addView(tableLayout);
        return scrollView;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        try {
            MainActivity.config.put("station", pos);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
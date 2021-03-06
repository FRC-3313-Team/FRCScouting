package com.team3313.frcscouting.fragments;

/**
 * Created by oa10712 on 3/7/2018.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.team3313.frcscouting.DataStore;
import com.team3313.frcscouting.MainActivity;
import com.team3313.frcscouting.components.MatchList;

public class ScheduleFragment extends Fragment {

    public ScheduleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Schedule for " + ((MainActivity) getActivity()).getActiveRegional(true));
        return new MatchList(getContext(), DataStore.scheduleData);
    }

    /*
     * Create textview
     */
    TextView textView() {
        TextView textView = new TextView(getActivity());
        textView.setText("Match data did not load");
        return textView;
    }

    /**
     * Create linear layout
     *
     * @return
     */
    LinearLayout linearLayout() {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        return linearLayout;
    }
}
package com.team3313.frcscouting;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Created by oa10712 on 3/9/2018.
 */

public class DataStore {
    /**
     * Match key, team key, match object
     */
    public static Table<String, String, JSONObject> matchData = HashBasedTable.create();
    public static JSONArray scheduleData;
    public static JSONObject config;
    public static JSONObject teamData = new JSONObject();

    private static RESTGetter.HttpsRequestTaskArray getTeamGetter() {
        return new RESTGetter.HttpsRequestTaskArray("https://www.team3313.com/regional/" + MainActivity.instance.getActiveRegional() + "/teams") {
            @Override
            protected void customEnd(JSONArray r) {
                try {
                    System.out.println("Teams.json was empty");
                    final List<String> sort = new ArrayList<>();
                    for (int i = 0; i < r.length(); i++) {
                        sort.add(r.getString(i));
                    }
                    Collections.sort(sort, new TeamNumberComparator());
                    for (final String s : sort) {
                        RESTGetter.HttpsRequestTask team = null;
                        team = new RESTGetter.HttpsRequestTask("https://team3313.com/teams/" + s) {
                            @Override
                            protected void customEnd(JSONObject ret) {
                                try {
                                    teamData.put(s, ret);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        team.execute("Authentication:" + config.getString("apiKey"));
                    }
                    writeToFile(teamData.toString(), "teams.json");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private static RESTGetter.HttpsRequestTaskArray getScheduleGetter() {
        return new RESTGetter.HttpsRequestTaskArray("https://www.team3313.com/regional/" + MainActivity.instance.getActiveRegional() + "/schedule") {
            @Override
            protected void customEnd(JSONArray r) {
                scheduleData = r;

                try {
                    scheduleData = sortJsonArray(scheduleData, "predicted_time");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                writeToFile(scheduleData.toString(), "regional-matches.json");
            }
        };
    }

    private static RESTGetter.HttpsRequestTaskArray getMatchDataGetter() {
        return new RESTGetter.HttpsRequestTaskArray("https://www.team3313.com/regional/" + MainActivity.instance.getActiveRegional() + "/match_data") {
            @Override
            protected void customEnd(JSONArray r) {
                System.out.println(r.toString());
                for (int i = 0; i < r.length(); i++) {
                    JSONObject item = null;
                    try {
                        item = r.getJSONObject(i);
                        item.remove("_id");
                        matchData.put(item.getString("match_key"), item.getString("team_key"), item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    static void initLoad() {
        String configString = readFromFile("config.json");
        if (!Objects.equals(configString, "")) {
            try {
                config = new JSONObject(configString);
            } catch (JSONException e) {
                config = new JSONObject();
                e.printStackTrace();
            }
        } else {
            config = new JSONObject();
        }
        String matches = readFromFile("regional-matches.json");
        if (!Objects.equals(matches, "")) {
            try {
                scheduleData = new JSONArray(matches);
            } catch (JSONException e) {
                e.printStackTrace();
                scheduleData = new JSONArray();
            }
        } else {
            getScheduleGetter().execute();
        }
        String teamString = readFromFile("teams.json");
        if (!Objects.equals(teamString, "")) {
            try {
                teamData = new JSONObject(teamString);
            } catch (JSONException e) {
                teamData = new JSONObject();
                e.printStackTrace();
            }
        } else {
            getTeamGetter().execute();
        }
        String matchString = readFromFile("match-data.json");
        if (!Objects.equals(matchString, "")) {
            try {
                JSONArray data = new JSONArray(matchString);
                for (int i = 0; i < data.length(); i++) {
                    JSONObject item = data.getJSONObject(i);
                    matchData.put(item.getString("match_key"), item.getString("team_key"), item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        getMatchDataGetter().execute();
    }

    public static void manualRefresh() {
        uploadMatchData();

        getScheduleGetter().execute();

        getTeamGetter().execute();

        getMatchDataGetter().execute();
    }

    private static void writeToFile(String data, String filename) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(MainActivity.instance.openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private static String readFromFile(String filename) {

        String ret = "";

        try {
            InputStream inputStream = MainActivity.instance.openFileInput(filename);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    public static void saveConfig() {
        writeToFile(config.toString(), "config.json");
    }

    public static String getTeamField(String teamKey, String key) {
        try {
            return teamData.getJSONObject(teamKey).getString(key);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "N/A";
    }

    @NonNull
    private static JSONArray sortJsonArray(JSONArray array, final String field) throws JSONException {
        List<JSONObject> jsons = new ArrayList<JSONObject>();
        for (int i = 0; i < array.length(); i++) {
            jsons.add(array.getJSONObject(i));
        }
        Collections.sort(jsons, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject lhs, JSONObject rhs) {
                String lid = null;
                try {
                    lid = lhs.getString(field);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String rid = null;
                try {
                    rid = rhs.getString(field);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Here you could parse string id to integer and then compare.
                return lid.compareTo(rid);
            }
        });
        return new JSONArray(jsons);
    }

    public static JSONArray searchTeamMatches(String team, String regional) {
        JSONArray results = new JSONArray();
        System.out.println("Searching " + regional + " for " + team);
        for (int i = 0; i < scheduleData.length(); i++) {
            try {
                JSONObject item = scheduleData.getJSONObject(i);
                if (item.getString("event_key").equalsIgnoreCase(regional)) {
                    boolean present = false;
                    JSONArray red = item.getJSONObject("alliances").getJSONObject("red").getJSONArray("team_keys");
                    for (int j = 0; j < 3; j++) {
                        if (red.getString(j).equalsIgnoreCase(team)) {
                            present = true;
                        }
                    }
                    JSONArray blue = item.getJSONObject("alliances").getJSONObject("blue").getJSONArray("team_keys");
                    for (int j = 0; j < 3; j++) {
                        if (blue.getString(j).equalsIgnoreCase(team)) {
                            present = true;
                        }
                    }
                    if (present) {
                        results.put(item);
                    }
                }
            } catch (JSONException e) {
            }
        }

        return results;
    }

    public static void uploadMatchData() {
        for (final JSONObject item : matchData.values()) {
            try {
                if (item.getBoolean("updated")) {
                    JSONObject toUpload = new JSONObject(item.toString());
                    toUpload.remove("updated");
                    System.out.println("Attempting upload:" + toUpload.getString("match_key") + "/" + toUpload.getString("team_key"));
                    final RESTGetter.HttpsSubmitTask t = new RESTGetter.HttpsSubmitTask("https://team3313.com/scouting/match/" + toUpload.getString("match_key") + "/" + toUpload.getString("team_key"), toUpload.toString()) {

                        @Override
                        protected void customEnd(String r) {
                            if (!r.startsWith("fail")) {
                                item.remove("updated");
                            }
                            System.out.println(r);
                        }
                    };
                    t.execute("Authentication:" + config.getString("apiKey"));
                }
            } catch (JSONException e) {
                //item is missing the 'updated' tag
            }
        }
    }

    public static void autoSave() {
        System.out.println("Autosaving");
        writeToFile(scheduleData.toString(), "regional-matches.json");
        saveConfig();
        writeToFile(teamData.toString(), "teams.json");
        JSONArray saveMatches = new JSONArray();
        for (JSONObject item : matchData.values()) {
            saveMatches.put(item);
        }
        writeToFile(saveMatches.toString(), "match-data.json");

    }

    public static void updateTeamStats(String teamKey) {
        System.out.println("Updating stats for " + teamKey);
        try {
            JSONObject teamObject = teamData.getJSONObject(teamKey);

            double scale = 0;
            double swth = 0;
            double exchange = 0;
            int climb = 0;
            int cross = 0;
            int autoScale = 0;
            int autoSwitch = 0;
            double played = 0;
            for (JSONObject match : matchData.values()) {
                if (match.getString("team_key").equalsIgnoreCase(teamKey)) {

                    System.out.println(match.toString());
                    played++;
                    JSONObject auto = match.getJSONObject("auto");
                    JSONObject tele = match.getJSONObject("tele");

                    if (auto.getBoolean("passedLine")) {
                        cross++;
                    }
                    if (auto.getBoolean("switch")) {
                        autoSwitch++;
                    }
                    if (auto.getBoolean("scale")) {
                        autoScale++;
                    }
                    if (tele.getBoolean("climb")) {
                        climb++;
                    }
                    scale += tele.getInt("scale");
                    swth += tele.getInt("switch");
                    exchange += tele.getInt("exchange");
                }
            }
            teamObject.put("played", played);
            if (played == 0) {
                played = 1;// this is for /0 errors
            }
            teamObject.put("scale", scale / played);
            teamObject.put("switch", swth / played);
            teamObject.put("exchange", exchange / played);


            teamObject.put("climb", climb / played);
            teamObject.put("cross", cross / played);
            teamObject.put("autoScale", autoScale / played);
            teamObject.put("autoSwitch", autoSwitch / played);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static class TeamNumberComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            int first = Integer.decode(o1.substring(3));
            int second = Integer.decode(o2.substring(3));
            return first - second;
        }
    }

}

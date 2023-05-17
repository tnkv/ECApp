package ru.tnkv.ecapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ru.tnkv.ecapp.utils.DateConverter;
import ru.tnkv.ecapp.utils.HttpRequests;
import ru.tnkv.ecapp.utils.ReportBlockAdapter;

public class MainActivity extends AppCompatActivity {
    RecyclerView reportList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // Создаю строку для запроса, беру значения из SharedPreferences если они там есть
        String reportListUrl = "" + prefs.getString("serverAddress", "https://api.ec.tnkv.ru/") + "getReports?token=" + prefs.getString("userToken", "");
        updateReportList(reportListUrl);

    }

    public void updateReportList(String url) {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    String answerFromServer = HttpRequests.getJson(url);
                    JSONArray reportListArray;
                    try {
                        reportListArray = new JSONObject(answerFromServer).getJSONArray("reports");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            ArrayList<Long> reportId = new ArrayList<>();
                            ArrayList<String> reportDate = new ArrayList<>();
                            ArrayList<String> reportAppname = new ArrayList<>();
                            ArrayList<String> reportLocation = new ArrayList<>();
                            ArrayList<String> reportException = new ArrayList<>();
                            setContentView(R.layout.activity_main);
                            for (int i = 0; i < reportListArray.length(); i++) {
                                try {

                                    JSONObject reportData = reportListArray.getJSONObject(i);
                                    reportId.add(reportData.getLong("id"));
                                    reportDate.add(DateConverter.getHumanDate(reportData.getLong("timestamp")));
                                    reportAppname.add(reportData.getString("application"));
                                    reportLocation.add(reportData.getString("location"));
                                    reportException.add(reportData.getString("exception"));
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            ReportBlockAdapter reportBlockAdapter = new ReportBlockAdapter(reportId,
                                    reportDate,
                                    reportAppname,
                                    reportLocation,
                                    reportException);
                            reportList = findViewById(R.id.recycler_view);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                            reportList.setLayoutManager(linearLayoutManager);
                            reportList.setAdapter(reportBlockAdapter);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.additional_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.SettingsMItem:
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
                break;
            case R.id.RefreshMItem:
                Toast.makeText(this, R.string.refreshing, Toast.LENGTH_SHORT).show();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                String reportListUrl = "" + prefs.getString("serverAddress", "https://api.ec.tnkv.ru/") + "getReports?token=" + prefs.getString("userToken", "");
                updateReportList(reportListUrl);
                break;
        }
        return true;
    }
}



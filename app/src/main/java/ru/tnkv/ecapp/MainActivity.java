package ru.tnkv.ecapp;

import static ru.tnkv.ecapp.utils.GetReportList.getActualReports;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.tnkv.ecapp.utils.GetReportList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String reportListUrl = "" + prefs.getString("serverAddress", "https://api.ec.tnkv.ru/") + "getReports?token=" +  prefs.getString("userToken", "");
        //updateReportList("https://api.ec.tnkv.ru/getReports?token=c8911f9d-4100-4e59-9766-8575979cecb0");
        updateReportList(reportListUrl);
    }

    public void updateReportList(String url) {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    String answerFromServer = GetReportList.getJson(url);
                    JSONArray arr;
                    try {
                        arr = new JSONObject(answerFromServer).getJSONArray("reports");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            setContentView(R.layout.activity_main);
                            ReportBlockAdapter adapter = null;
                            try {
                                adapter = new ReportBlockAdapter(MainActivity.this, getActualReports(arr));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                            ListView lv = findViewById(R.id.LV);
                            lv.setAdapter(adapter);
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
            case R.id.RefreshMItem:
                Toast.makeText(this, "refresh plug", Toast.LENGTH_SHORT).show();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                String reportListUrl = "" + prefs.getString("serverAddress", "https://api.ec.tnkv.ru/") + "getReports?token=" +  prefs.getString("userToken", "");
                updateReportList(reportListUrl);
                //updateReportList("https://api.ec.tnkv.ru/getReports?token=c8911f9d-4100-4e59-9766-8575979cecb0");
        }
        return true;
    }
}



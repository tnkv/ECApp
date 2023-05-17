package ru.tnkv.ecapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import ru.tnkv.ecapp.utils.HttpRequests;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        Button generateTokenButton = (Button) findViewById(R.id.generatetoken);
        generateTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
                String url = prefs.getString("serverAddress", "https://api.ec.tnkv.ru/") + "createUser";
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String answerFromServer = HttpRequests.getJson(url);
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    String token;
                                    try {
                                        token = new JSONObject(answerFromServer).getString("uid");
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData clip = ClipData.newPlainText("Token", token);
                                    clipboard.setPrimaryClip(clip);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                thread.start();
            }
        });

        Button sendTestReport = (Button) findViewById(R.id.sendtestreport);
        sendTestReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
                String url = prefs.getString("serverAddress", "https://api.ec.tnkv.ru/") + "sendReport?token="+prefs.getString("userToken", "")
                        + "&application=ECApp&location=SettingsActivity&exception=Test Request From ECApp";
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpRequests.getJson(url);
                        runOnUiThread(new Runnable() {
                              @Override
                              public void run() {
                                  Toast.makeText(SettingsActivity.this, R.string.sent, Toast.LENGTH_SHORT).show();
                              }
                          }
                        );
                    }
                });
                thread.start();
            }
        }
    );
}


public static class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }

}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return true;
    }

}
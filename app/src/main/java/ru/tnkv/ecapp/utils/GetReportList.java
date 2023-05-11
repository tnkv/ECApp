package ru.tnkv.ecapp.utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.tnkv.ecapp.ReportBlock;

public class GetReportList {
    public static String getJson(String url) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Запрос к серверу не был успешен: " +
                        response.code() + " " + response.message());
            }
            return (response.body().string());
        } catch (IOException e) {
            return e.toString();
        }

    }
    public static ReportBlock[] getActualReports(JSONArray array) throws JSONException {

        ReportBlock[] arr = new ReportBlock[array.length()];
        for(int i = 0; i < array.length(); i++) {
            System.out.println(array.getJSONObject(i).getInt("id"));
            System.out.println(array.getJSONObject(i).getString("token"));

            ReportBlock report = new ReportBlock();
            report.date = String.valueOf(array.getJSONObject(i).getInt("timestamp"));;
            report.appName = array.getJSONObject(i).getString("application");
            report.location = array.getJSONObject(i).getString("location");
            report.excType = array.getJSONObject(i).getString("exception");
            report.reportID = array.getJSONObject(i).getInt("id");
            arr[i] = report;
        }

        return arr;
    }

    public static class UpdateReports {
    }
}

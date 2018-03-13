package com.waracle.androidtest.data.loaders;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.waracle.androidtest.data.models.Cake;
import com.waracle.androidtest.utils.StreamUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CakeLoader extends AsyncTaskLoader<Result<List<Cake>>> {

    private static final String JSON_URL = "https://gist.githubusercontent.com/hart88/198f29ec5114a3ec3460/" +
            "raw/8dd19a88f9b8d24c23d9960f3300d0c917a4f07c/cake.json";

    private static final String TAG = CakeLoader.class.getSimpleName();

    public CakeLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Result<List<Cake>> loadInBackground() {

        JSONArray dataAsJson;

        try {
            URL url = new URL(JSON_URL);
            dataAsJson = loadData(url);
        } catch (IOException | JSONException e) {
            Log.e(TAG, "HTTP error");
            return null;
        }

        return new Result<>(parseJson(dataAsJson));
    }

    private JSONArray loadData(URL url) throws IOException, JSONException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            // Can you think of a way to improve the performance of loading data
            // using HTTP headers???

            // Also, Do you trust any utils thrown your way????

            byte[] bytes = StreamUtils.readUnknownFully(in);

            // Read in charset of HTTP content.
            String charset = parseCharset(urlConnection.getRequestProperty("Content-Type"));

            // Convert byte array to appropriate encoded string.
            String jsonText = new String(bytes, charset);

            // Read string as JSON.
            return new JSONArray(jsonText);
        } finally {
            urlConnection.disconnect();
        }
    }

    private String parseCharset(String contentType) {
        if (contentType != null) {
            String[] params = contentType.split(",");
            for (int i = 1; i < params.length; i++) {
                String[] pair = params[i].trim().split("=");
                if (pair.length == 2) {
                    if (pair[0].equals("charset")) {
                        return pair[1];
                    }
                }
            }
        }
        return "UTF-8";
    }

    private List<Cake> parseJson(JSONArray jsonArr) {

        List<Cake> outputList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject cakeAsJson = jsonArr.getJSONObject(i);
                Cake cake = new Cake();

                cake.setTitle(cakeAsJson.getString("title"));
                cake.setDescription(cakeAsJson.getString("desc"));
                cake.setImageUrl(cakeAsJson.getString("image"));
                outputList.add(cake);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON");
        }

        return outputList;
    }

}

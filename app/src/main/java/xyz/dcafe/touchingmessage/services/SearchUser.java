package xyz.dcafe.touchingmessage.services;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import xyz.dcafe.touchingmessage.MY;

/**
 * Created by Eugene J. Jeon on 2015-06-14.
 */
public class SearchUser extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>> {
    ArrayList<HashMap<String, String>> result = new ArrayList<>();

    @Override
    protected ArrayList<HashMap<String, String>> doInBackground(String... strings) {
        String value = "";

        try {
            HttpPost request = new HttpPost(Server.URL + "SEARCH_USER");
            request.setHeader("Content-Type", "application/json; charset=utf-8");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("MY_NUMBER", MY.Number);
            jsonObject.put("NUMBER", strings[0]);
            //TODO: 현재 한글이 전달 안됨! 수정 요망!

            StringEntity stringEntity = new StringEntity(jsonObject.toString());
            HttpEntity httpEntity = stringEntity;
            request.setEntity(httpEntity);

            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(request);

            HttpEntity httpEntityResponse = response.getEntity();
            InputStream inputStream = httpEntityResponse.getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, HTTP.UTF_8));

            String line = "";
            while ((line = bufferedReader.readLine()) != null) value += line;
            inputStream.close();
            Log.i("SendMessageToServer", "result is [" + value + "]");

            if (value.equals(Server.FAIL)) {
                return null;
            }

            JSONArray jsonArray = new JSONArray(value);
            for (int i = 0; i < jsonArray.length(); i++) {
                HashMap<String, String> map = new HashMap<>();

                try {
                    JSONObject object = jsonArray.getJSONObject(i);
                    Iterator<String> iterator = object.keys();
                    while(iterator.hasNext()) {
                        String key = iterator.next();
                        map.put(key, object.getString(key));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                result.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<HashMap<String, String>> list) {
        super.onPostExecute(list);
    }
}

package xyz.dcafe.touchingmessage.services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import xyz.dcafe.touchingmessage.MY;

/**
 * Created by Eugene J. Jeon on 2015-06-14.
 */
public class Sign extends AsyncTask<Void, Void, String> {
    private Context mContext;

    public Sign(Context context) {
        mContext = context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String value = "";

        try {
            HttpPost request = new HttpPost(Server.URL + "SIGN");
            request.setHeader("Content-Type", "application/json; charset=utf-8");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("NUMBER", MY.Number);
            jsonObject.put("NICKNAME", MY.NickName);
            jsonObject.put("GCMID", MY.GCMID);
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String string) {
        if (string.equals(Server.FAIL)) {
            Toast.makeText(mContext, "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
        } else if (string.equals(Server.ALREADY)) {
            Toast.makeText(mContext, "이미 등록된 아이디 입니다.", Toast.LENGTH_SHORT).show();
        } else if (string.equals(Server.SUCCESS)) {
            Toast.makeText(mContext, "등록 완료!", Toast.LENGTH_SHORT).show();
        }

        super.onPostExecute(string);
    }
}

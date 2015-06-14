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
public class SendVideo extends AsyncTask<String, Void, String> {
    private Context mContext;

    public SendVideo(Context context) {
        mContext = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        String value = "";

        try {
            HttpPost request = new HttpPost(Server.URL + "SEND_VIDEO");
            request.setHeader("Content-Type", "application/json; charset=utf-8");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("NICKNAME", MY.NickName);
            jsonObject.put("GCMID", strings[0]);
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
        if (string.equals(Server.SUCCESS)) {
            Toast.makeText(mContext, "등록 완료!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(mContext, "전송 실패!", Toast.LENGTH_SHORT).show();
        }

        super.onPostExecute(string);
    }
}

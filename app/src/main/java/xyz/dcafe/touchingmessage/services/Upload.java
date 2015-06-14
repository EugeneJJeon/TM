package xyz.dcafe.touchingmessage.services;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Eugene J. Jeon on 2015-06-14.
 */
public class Upload extends AsyncTask<String, Void, Void> {
    private final String mURL = Server.URL + "UPLOAD";

    private String mFileName;

    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";

    @Override
    protected Void doInBackground(String... strings) {
        mFileName = strings[1];

        try {
            FileInputStream fileInputStream = new FileInputStream(mFileName);
            URL connectURL = new URL(mURL);
            HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/from-data;boundary=" + boundary);

            DataOutputStream dataOutputStream = new DataOutputStream(conn.getOutputStream());
            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + mFileName +"\"" + lineEnd);
            dataOutputStream.writeBytes(lineEnd);

            int bytesAvailable = fileInputStream.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            byte[] buffer = new byte[bufferSize];
            int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dataOutputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            fileInputStream.close();
            dataOutputStream.flush();
            dataOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void param) {
        super.onPostExecute(param);
    }
}

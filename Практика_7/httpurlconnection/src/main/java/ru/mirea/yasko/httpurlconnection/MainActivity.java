package ru.mirea.yasko.httpurlconnection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView resultTextView;
    private String url = "https://api.ipify.org?format=json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTextView = findViewById(R.id.ipText);
    }

    public void OnClick(View view){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null){
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()){
            new DownloadPageTask().execute(url);
        } else {
            Toast.makeText(this, "Нет интернета", Toast.LENGTH_SHORT).show();
        }
    }

    private class DownloadPageTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            resultTextView.setText("Загружаем");
        }

        @Override
        protected String doInBackground(String... urls) {
            try{
                return downloadIpInfo(urls[0]);
            } catch (IOException e){
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result){
            try{
                JSONObject responseObject = new JSONObject(result);
                String ip = responseObject.getString("ip");
                resultTextView.setText(ip);
                Log.d(MainActivity.class.getSimpleName(), responseObject.toString());
                //Log.d(MainActivity.class.getSimpleName(), ip);
            } catch (JSONException e){
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }

        private String downloadIpInfo(String address) throws IOException{
            InputStream inputStream = null;
            String data = "";
            try{
                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(100000);
                connection.setConnectTimeout(100000);
                connection.setRequestMethod("GET");
                connection.setInstanceFollowRedirects(true);
                connection.setUseCaches(false);
                connection.setDoInput(true);
                int responseCode = connection.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK){
                    inputStream = connection.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    int read = 0;
                    while((read = inputStream.read()) != -1){
                        bos.write(read);
                    }
                    bos.close();
                    data = bos.toString();
                } else{
                    data = connection.getResponseMessage() + " . Error Code : " + responseCode;
                }
                connection.disconnect();
            } catch (IOException e){
                e.printStackTrace();
            } finally {
                if (inputStream != null){
                    inputStream.close();
                }
            }
            return data;
        }
    }
}
package com.adliano.restfullcarsmyservergolang;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity
{
    TextView tvShowCars;
    String myWebServerURL = "http://10.0.0.28:8080/car";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvShowCars = findViewById(R.id.tvShowCars);

        new AsyncTaskGetCras().execute(myWebServerURL);
    }

    class AsyncTaskGetCras extends AsyncTask<String,String,String>
    {
        String strData = "";

        @Override
        protected String doInBackground(String... strings)
        {
            // get the URL
            try
            {
                URL url = new URL(strings[0]); //throws MalformedURLException
                // connect and send the request
                HttpURLConnection connection = (HttpURLConnection)url.openConnection(); // throws IOException
                // ste the timeout
                connection.setConnectTimeout(5000);

                // get the data
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                // Use IOUtils (org-apache-commons-io) to convert the Stream to String
                strData = IOUtils.toString(inputStream);

                publishProgress(strData);

                inputStream.close();
                connection.disconnect();
            }
            catch(MalformedURLException e)
            {
                Log.d("**** URLException **** ",e.toString());
                e.printStackTrace();
            }
            catch(IOException e)
            {
                Log.d("**** IOEXC ****",e.toString());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values)
        {
            String model,make;
            int year,doors;
            boolean sold;

            String output = "";

            try
            {
                JSONArray array = new JSONArray(values[0]);

                for(int i = 0; i < array.length(); i++)
                {
                    JSONObject jsonObject = array.getJSONObject(i);
                    model = jsonObject.getString("model");
                    year = jsonObject.getInt("year");
                    make = jsonObject.getString("make");
                    doors = jsonObject.getInt("doors");
                    sold = jsonObject.getBoolean("sold");

                    output += model+" "+year+" "+make+" "+doors+" "+sold+"\n";
                }

                tvShowCars.setText(output);
            }
            catch(JSONException e)
            {
                mktoast("JSONException");
                e.printStackTrace();
            }
        }
    }


    public void mktoast(String msg)
    {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

}

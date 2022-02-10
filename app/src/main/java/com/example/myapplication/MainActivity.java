package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: stating with AsyncTask");
        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");
        Log.d(TAG, "onCreate: done");

    }



    private class DownloadData extends AsyncTask<String, Void, String> {
        private static final String TAG = "DownloadData";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: parameter is: " + s);
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: starts with " + strings[0]);
            String rssFeed = downloadXML(strings[0]);

            if (rssFeed == null) {
                Log.e(TAG, "doInBackground: error Downloading");
            }
            return rssFeed;
        }

        private String downloadXML(String urlPath) {
            StringBuffer xmlResult = new StringBuffer();

            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG, "downloadXML: The response code was " + response);
//                InputStream inputStream = connection.getInputStream();
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                int charsRead;
                char[] inputBuffer = new char[500];

                while (true) {
                    charsRead = bufferedReader.read(inputBuffer);

                    if (charsRead < 0) {
                        break;
                    }

                    if (charsRead > 0) {
                        xmlResult.append(String.copyValueOf(inputBuffer, 0, charsRead));
                    }
                }

                bufferedReader.close();

                return xmlResult.toString();

            }catch (MalformedURLException e) {
                Log.e(TAG, "downloadXML: Invalid URL " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "downloadXML: IO Exception reading data: " + e.getMessage() );
            } catch (SecurityException e) {
                Log.e(TAG, "downloadXML: security exception. need permission " + e.getMessage());
                e.printStackTrace();
            }

            return null;
        }
    }




}
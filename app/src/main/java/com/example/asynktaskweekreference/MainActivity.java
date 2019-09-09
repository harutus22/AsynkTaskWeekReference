package com.example.asynktaskweekreference;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
    }

    public void startAsyncTask(View view){
        MyAsyncTask myAsyncTask = new MyAsyncTask(this);
        myAsyncTask.execute(10);
    }

    static class MyAsyncTask extends AsyncTask<Integer, Integer, String>{

        private WeakReference<MainActivity> weakReference;

        MyAsyncTask(MainActivity mainActivity){
            this.weakReference = new WeakReference<>(mainActivity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity mainActivity = weakReference.get();
            if(mainActivity == null || mainActivity.isFinishing()){
                return;
            }
            mainActivity.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Integer... integers) {
            for (int i = 0; i < integers[0]; i++) {
                publishProgress((i * 100) / integers[0]);
                SystemClock.sleep(1000);
            }
            return "Finished";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            MainActivity mainActivity = weakReference.get();
            if(mainActivity == null || mainActivity.isFinishing()){
                return;
            }
            mainActivity.progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            MainActivity mainActivity = weakReference.get();
            if(mainActivity == null || mainActivity.isFinishing()){
                return;
            }
            Toast.makeText(mainActivity, s, Toast.LENGTH_LONG).show();
            mainActivity.progressBar.setProgress(0);
            mainActivity.progressBar.setVisibility(View.INVISIBLE);
        }
    }
}

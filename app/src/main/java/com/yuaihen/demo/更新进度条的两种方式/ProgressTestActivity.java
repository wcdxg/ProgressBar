package com.yuaihen.demo.更新进度条的两种方式;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.yuaihen.demo.R;

/**
 * Created by Yuaihen.
 * on 2018/11/13
 * 更新进度条的两种方式
 */
public class ProgressTestActivity extends Activity implements View.OnClickListener {

    private Button mBtn;
    private ProgressBar mProgressBar;
    public static final int MSG_UPDATE = 0X0001;
    private int currentProgressBar;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int progress = mProgressBar.getProgress();
            mProgressBar.setProgress(++progress);
            if (progress >= 100) {
                mHandler.removeMessages(MSG_UPDATE);
            }

            mHandler.sendEmptyMessageDelayed(MSG_UPDATE, 100);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        mProgressBar = findViewById(R.id.progressBar);
        mBtn = findViewById(R.id.btn_update);
        mBtn.setOnClickListener(this);

        mProgressBar.setMax(100);
        mProgressBar.setProgress(0);
    }

    /**
     * 更新进度条方式一
     */
    public void updateProgress(View view) {
        //从0更新到100
        if (mProgressBar.getProgress() == 100) {
            mProgressBar.setProgress(0);
            currentProgressBar = 0;
        }
        mHandler.sendEmptyMessage(MSG_UPDATE);
    }

    /**
     * 更新进度条方式二
     */
    private void updateProgress2() {
        new MyAyscTask().execute(0);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_update) {
            updateProgress2();
        }
    }


    private class MyAyscTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setProgress(0);
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            while (integers[0] <= 100) {
                integers[0] += 1;
                publishProgress(integers[0]);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            //            mProgressBar.setProgress(0);
        }
    }
}

package com.yuaihen.demo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.yuaihen.demo.自定义进度条.HorizontalProgressbarWithProgress;
import com.yuaihen.demo.自定义进度条.RoundProgressBarWithProgress;

public class MainActivity extends AppCompatActivity {

    private HorizontalProgressbarWithProgress mHProgress;
    private RoundProgressBarWithProgress mRoundProgress2, mRoundProgress3;
    private static final int MSG_UPDATE = 0X110;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int progress = mHProgress.getProgress();
            int roundProgress2 = mRoundProgress2.getProgress();
            int roundProgress3 = mRoundProgress3.getProgress();
            mHProgress.setProgress(++progress);
            mRoundProgress2.setProgress(++roundProgress2);
            mRoundProgress3.setProgress(++roundProgress3);
            if (progress >= 100) {
                mHandler.removeMessages(MSG_UPDATE);
            }

            mHandler.sendEmptyMessageDelayed(MSG_UPDATE, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHProgress = findViewById(R.id.progress1);
        mRoundProgress2 = findViewById(R.id.progress2);
        mRoundProgress3 = findViewById(R.id.progress3);

        mHandler.sendEmptyMessage(MSG_UPDATE);
    }
}

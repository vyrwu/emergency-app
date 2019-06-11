package com.example.p3_emergencyapp;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by mac on 20/11/2017.
 */

public class ServiceBroadcastingJob extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                WiFiP2pServiceManager.getInstance().discoverPeers();
                Log.w(WiFiP2pServiceManager.TAG, "Discovering Peers...");
            }
        });
        jobFinished(params, false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

}

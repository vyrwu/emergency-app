package com.example.p3_emergencyapp;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

/**
 * Created by mac on 30/11/2017.
 */

public class JobManager {
    private static JobManager instance;

    private int SERVICE_BROADCASTING_JOB_ID = 0;
    private int SERVICE_DISCOVERING_JOB_ID = 1;
    private JobInfo serviceBroadcastingJobInfo;
    private JobInfo serviceDiscoveringJobInfo;
    private JobScheduler jobScheduler;
    private Context thisContext;

    private int SERVICE_BROADCASTING_INTERVAL = 10000;
    private int SERVICE_DISCOVERING_INTERVAL = 10000;

    private JobManager() {
    }

    public static JobManager getInstance() {
        if (instance == null) {
            instance = new JobManager();
        }
        return instance;
    }

    public void initialize(final Context context) {
        jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        thisContext = context;
    }

    public void startBroadcastingService() {
        jobScheduler.cancel(0);
        serviceBroadcastingJobInfo = new JobInfo.Builder(SERVICE_BROADCASTING_JOB_ID,
                new ComponentName(thisContext, ServiceBroadcastingJob.class)).
                setPeriodic(SERVICE_BROADCASTING_INTERVAL).build();
        jobScheduler.schedule(serviceBroadcastingJobInfo);
    }

    public void startServiceDiscovery() {
        jobScheduler.cancel(1);
        serviceDiscoveringJobInfo = new JobInfo.Builder(SERVICE_DISCOVERING_JOB_ID,
                new ComponentName(thisContext, ServiceDiscoveringJob.class)).
                setPeriodic(SERVICE_DISCOVERING_INTERVAL).build();
        jobScheduler.schedule(serviceDiscoveringJobInfo);
    }

    public void cleanup() {
        jobScheduler.cancelAll();
        jobScheduler.getAllPendingJobs().clear();
    }
}

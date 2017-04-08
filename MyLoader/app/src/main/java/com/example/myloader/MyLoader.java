package com.example.myloader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by Vilagra on 08.02.2017.
 */

public class MyLoader extends AsyncTaskLoader<Integer> {
    final static String LOG_TAG = "myLogs";
    Handler h;
    boolean stop = false;

    public void setH(Handler h) {
        this.h = h;
    }

    public MyLoader(Context context) {
        super(context);
    }

    @Override
    public Integer loadInBackground() {
        for (int i = 0; i < 10;) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //deliverResult(i);
            Message msg = h.obtainMessage(0,i,0);
            h.sendMessage(msg);
            if(!stop){
                i++;
            }

        }
        return new Random(System.currentTimeMillis()).nextInt();
    }

/*    @Override
    public void deliverResult(Object data) {
        super.deliverResult(data);
    }*/

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        Log.d(LOG_TAG, hashCode() + " onForceLoad");
    }

    @Override
    protected void onAbandon() {
        super.onAbandon();
        Log.d(LOG_TAG, hashCode() + " onAbandon");
        this.onReset();
    }

    @Override
    protected void onReset() {
        super.onReset();
        Log.d(LOG_TAG, hashCode() + " onReset");
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.d(LOG_TAG, hashCode() + " onStartLoading");
        stop=false;

    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        Log.d(LOG_TAG, hashCode() + " onStopLoading");
        stop=true;
    }
}

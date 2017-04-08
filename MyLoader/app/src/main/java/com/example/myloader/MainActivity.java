package com.example.myloader;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<Integer>{

    Handler h;
    ProgressBar progressBar;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(MyLoader.LOG_TAG,"createA");
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        textView = (TextView) findViewById(R.id.indicator);
        progressBar.setMax(100);
        h=new Handler(){
            @Override
            public void handleMessage(Message msg){
                progressBar.setProgress(msg.arg1);
                textView.setText(String.valueOf(msg.arg1));
            }
        };
        Loader<Integer> loader=getLoaderManager().initLoader(0,new Bundle(),  this);
        ((MyLoader)loader).setH(h);

    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        MyLoader loader=new MyLoader(this);
        loader.setH(h);
        Log.d(MyLoader.LOG_TAG, "onCreateLoader"+loader.hashCode());
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Integer> loader, Integer o) {
        //((MyLoader)loader).setH(null);
        Log.d(MyLoader.LOG_TAG, "onLoadFinished for loader " + loader.hashCode()
                + ", result = "+o);
        //progressBar.setProgress(o);
        //textView.setText(String.valueOf(o));
    }

    @Override
    public void onLoaderReset(Loader<Integer> loader) {
        Log.d(MyLoader.LOG_TAG, "onLoaderReset for loader " + loader.hashCode());

    }

    public void onClick(View view) {
        Loader<Object> loader=getLoaderManager().getLoader(0);
        loader.forceLoad();
        //Log.d(MyLoader.LOG_TAG,loader)
    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.d(MyLoader.LOG_TAG,"startA");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(MyLoader.LOG_TAG,"resumeA");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(MyLoader.LOG_TAG,"pauseA");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(MyLoader.LOG_TAG,"stopA");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(MyLoader.LOG_TAG,"destrA");
    }
}

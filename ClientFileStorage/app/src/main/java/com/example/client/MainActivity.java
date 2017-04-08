package com.example.client;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Intent mRequestFileIntent;
    private ParcelFileDescriptor mInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRequestFileIntent = new Intent(Intent.ACTION_PICK);
        mRequestFileIntent.setType("image/jpg");
        ((Button)findViewById(R.id.button)).setOnClickListener(this);

    }

    protected void requestedFile(){
        startActivityForResult(mRequestFileIntent,0);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent returnIntent) {
        Log.d("coze", String.valueOf(resultCode));
        if(resultCode!=RESULT_OK){
            return;
        }else{
            Uri returnUri = returnIntent.getData();
            Log.d("uri-uri",returnUri.toString());
            try{
                mInput = getContentResolver().openFileDescriptor(returnUri,"r");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }
            FileDescriptor fd= mInput.getFileDescriptor();
            FileInputStream fileInputStream;
            FileOutputStream fileOutputStream;
            File rootExternal= Environment.getExternalStorageDirectory();
            File writeFile= new File(rootExternal,"noviy.jpg");
            try{
                fileInputStream = new FileInputStream(fd);
                fileOutputStream =new FileOutputStream(writeFile);
                byte[] buff = new byte[1000];
                int len;
                while((len=fileInputStream.read(buff))!=-1) {
                    fileOutputStream.write(buff,0,len);
                }
                fileInputStream.close();
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onClick(View v) {
        requestedFile();
    }
}

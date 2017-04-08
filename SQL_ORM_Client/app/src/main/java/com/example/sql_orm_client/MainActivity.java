package com.example.sql_orm_client;


import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>{

    final String LOG_TAG = "myLogs";

    final Uri CONTACT_URI = Uri.parse("content://ru.startandroid.providers.AdressBook/Persons");
    final Uri POSITION_URI = Uri.parse("content://ru.startandroid.providers.AdressBook/Positions");

    final String PERSON_NAME = "name";
    final String PERSON_AGE = "age";
    final String PERSON_POSITION = "position";
    SimpleCursorAdapter adapter;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Cursor cursor = getContentResolver().query(CONTACT_URI, null, null,
                //null, null);
        //startManagingCursor(cursor);

        String from[] = {"_id", "name","age","position","salary"};
        int to[] = {R.id.idPerson,R.id.nameT,R.id.age,R.id.position,R.id.salary};
        adapter = new SimpleCursorAdapter(this,
                R.layout.item, null, from, to);

        ListView lvContact = (ListView) findViewById(R.id.lvContact);
        lvContact.setAdapter(adapter);
        getLoaderManager().initLoader(0,null,this);
    }

    public void onClickInsert(View v) {
        ContentValues cv = new ContentValues();
        Uri newUri;
        switch (v.getId()){
            case R.id.btnInsert:
                cv.put(PERSON_NAME, "vaska");
                cv.put(PERSON_AGE, 12);
                cv.put(PERSON_POSITION,"админ");
                newUri = getContentResolver().insert(CONTACT_URI, cv);
                Log.d(LOG_TAG, "insert, result Uri : " + newUri.toString());
                break;
            case R.id.btnInsert2:
                cv.put("name","админ");
                cv.put("salary",30000000);
                newUri = getContentResolver().insert(POSITION_URI, cv);
                Log.d(LOG_TAG, "insert, result Uri : " + newUri.toString());
                break;
        }


    }

    public void onClickUpdate(View v) {
        ContentValues cv = new ContentValues();
        //cv.put(CONTACT_NAME, "name 5");
        //cv.put(CONTACT_EMAIL, "email 5");
        Uri uri = ContentUris.withAppendedId(CONTACT_URI, 2);
        int cnt = getContentResolver().update(uri, cv, null, null);
        Log.d(LOG_TAG, "update, count = " + cnt);
    }

    public void onClickDelete(View v) {
        Uri uri = ContentUris.withAppendedId(CONTACT_URI, 3);
        int cnt = getContentResolver().delete(uri, null, null);
        Log.d(LOG_TAG, "delete, count = " + cnt);
    }

    public void onClickError(View v) {
        Uri uri = Uri.parse("content://ru.startandroid.providers.AdressBook/phones");
        try {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        } catch (Exception ex) {
            Log.d(LOG_TAG, "Error: " + ex.getClass() + ", " + ex.getMessage());
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(this,CONTACT_URI,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
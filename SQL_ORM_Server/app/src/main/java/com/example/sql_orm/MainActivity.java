package com.example.sql_orm;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.example.sql_orm.R;
import com.example.sql_orm.entity.Person;
import com.example.sql_orm.entity.Position;

import java.util.Arrays;

public class MainActivity extends Activity implements OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    final static String LOG_TAG = "myLogs";

    Button btnAdd, btnRead, btnClear;
    EditText etName, etAge, etPos;

    ListView listData;
    SimpleCursorAdapter simpleCursorAdapter;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnRead = (Button) findViewById(R.id.btnRead);
        btnRead.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        etName = (EditText) findViewById(R.id.etName);
        etAge = (EditText) findViewById(R.id.age);
        etPos = (EditText) findViewById(R.id.posid);

        if (Position.load(Position.class, 1) == null) {
            new Positions().fill();
        }

        String[] from = new String[]{"_id", PersonProvider.PERSON_NAME, PersonProvider.PERSON_AGE, PersonProvider.PERSON_POSITION, PersonProvider.POSITION_SALARY};
        int[] to = new int[]{R.id.idPerson, R.id.nameT, R.id.age, R.id.position, R.id.salary};

        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.item, null, from, to, 0);
        listData = (ListView) findViewById(R.id.lvData);
        listData.setAdapter(simpleCursorAdapter);

        registerForContextMenu(listData);

        getLoaderManager().initLoader(0, null, this);

    }

    @Override
    public void onClick(View v) {
        // получаем данные из полей ввода
        String name = etName.getText().toString();
        int age = Integer.valueOf(etAge.getText().toString());
        String s = etPos.getText().toString();

        switch (v.getId()) {
            case R.id.btnAdd:
                ContentValues cv = new ContentValues();
                cv.put(PersonProvider.PERSON_NAME, name);
                cv.put(PersonProvider.PERSON_AGE, age);
                cv.put(PersonProvider.PERSON_POSITION, s);
                getContentResolver().insert(PersonProvider.PERSON_CONTENT_URI, cv);
                break;
            case R.id.btnRead:
                break;
            case R.id.btnClear:
                Log.d(LOG_TAG, "--- Clear mytable: ---");
                // удаляем все записи
                getContentResolver().delete(PersonProvider.PERSON_CONTENT_URI,null,null);
                break;
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 0, R.string.delete_record);
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            // получаем из пункта контекстного меню данные по пункту списка
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item
                    .getMenuInfo();
            Uri uri = ContentUris.withAppendedId(PersonProvider.PERSON_CONTENT_URI,acmi.id);
            getContentResolver().delete(uri,null,null);
            return true;
        }
        return super.onContextItemSelected(item);
    }


    protected void onDestroy() {
        super.onDestroy();
        // закрываем подключение при выходе
        //db.close();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.d("create loader", "yyy");
        return new android.content.CursorLoader(this, PersonProvider.PERSON_CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(LOG_TAG, "finished" + loader.hashCode() + Arrays.toString(cursor.getColumnNames()));
        simpleCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}

package com.example.sql_orm;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import com.activeandroid.Cache;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.example.sql_orm.entity.Person;
import com.example.sql_orm.entity.Position;

import java.util.List;

public class PersonProvider extends ContentProvider {
    final String LOG_TAG = "myLogs";

    // // Константы для БД
    // БД
    static final String DB_NAME = "AA_MODELS";


    // Поля
    static final String PERSON_ID = "id";
    static final String PERSON_NAME = "name";
    static final String PERSON_AGE = "age";
    static final String PERSON_POSITION = "position";

    static final String POSITION_ID = "id";
    static final String POSITION_NAME = "position";
    static final String POSITION_SALARY = "salary";

    // // Uri
    // authority
    static final String AUTHORITY = "ru.startandroid.providers.AdressBook";

    // path
    static final String PERSON_PATH = "Persons";
    static final String POSITION_PATH = "Positions";

    // Общий Uri
    public static final Uri PERSON_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PERSON_PATH);
    public static final Uri POSITION_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + POSITION_PATH);

    // Типы данных
    // набор строк
    static final String PERSON_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + PERSON_PATH;

    // одна строка
    static final String PERSON_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + PERSON_PATH;

    //// UriMatcher
    // общий Uri
    static final int URI_CONTACTS = 1;

    // Uri с указанным ID
    static final int URI_CONTACTS_ID = 2;

    static final int URI_POSITIONS = 3;

    // описание и создание UriMatcher
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PERSON_PATH, URI_CONTACTS);
        uriMatcher.addURI(AUTHORITY, PERSON_PATH + "/#", URI_CONTACTS_ID);
        uriMatcher.addURI(AUTHORITY, POSITION_PATH, URI_POSITIONS);
    }

    //DBHelper dbHelper;
    //SQLiteDatabase db;

    public boolean onCreate() {
        Log.d(LOG_TAG, "onCreate");
        //dbHelper = new DBHelper(getContext());
        return true;
    }

    // чтение
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(LOG_TAG, "query, " + uri.toString());
        Cursor cursor = getAllData();
        cursor.setNotificationUri(getContext().getContentResolver(),
                PERSON_CONTENT_URI);
        return cursor;
    }

    public Cursor getAllData() {
        String pos = Cache.getTableInfo(Position.class).getTableName();
        String per = Cache.getTableInfo(Person.class).getTableName();
        String sqlQuery = "select PL.id as _id,PL.name as " + PERSON_NAME + ",PL.age as " + PERSON_AGE + ",PS.name as " + PERSON_POSITION + ",Sallary"
                + " from " + per + " as PL "
                + "inner join " + pos + " as PS "
                + "on position = PS.id ";
        sqlQuery = new Select("PL.id as _id,PL.name as " + PERSON_NAME + ",PL.age as " + PERSON_AGE + ",PS.name as " + PERSON_POSITION + ",PS.salary as salary")
                .from(Person.class).as("PL").innerJoin(Position.class).as("PS").on("position = PS.id ").toSql();
        return Cache.openDatabase().rawQuery(sqlQuery, null);
    }

    public Uri insert(Uri uri, ContentValues values) {
        Log.d(LOG_TAG, "insert, " + uri.toString());
        Uri resultUri = null;
        long rowID;
        switch (uriMatcher.match(uri)) {
            case URI_CONTACTS:
                String name = (String) values.get(PERSON_NAME);
                int age = values.getAsInteger(PERSON_AGE);
                List<Position> listPosition = new Select().from(Position.class).where("Name = ?",values.getAsString(PERSON_POSITION)).execute();
                Log.d(MainActivity.LOG_TAG, String.valueOf(listPosition.size()));
                if(listPosition.size()!=0) {
                    Position position = listPosition.get(0);
                    rowID = new Person(age, name, position).save();
                    resultUri = ContentUris.withAppendedId(PERSON_CONTENT_URI, rowID);
                    Log.d(LOG_TAG, "insert, " + resultUri.toString());
                    //уведомляем ContentResolver, что данные по адресу resultUri изменились
                    getContext().getContentResolver().notifyChange(resultUri, null);
                }else {
                    Toast.makeText(this.getContext(),"Не коректное название професии",Toast.LENGTH_SHORT).show();
                }
                break;
            case URI_POSITIONS:
                String namePos=values.getAsString("name");
                Integer salary=values.getAsInteger("salary");
                rowID = new Position(namePos,salary).save();
                resultUri = ContentUris.withAppendedId(POSITION_CONTENT_URI, rowID);
                Log.d(LOG_TAG, "insert, " + resultUri.toString());
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        return resultUri;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "delete, " + uri.toString());
        List<Person> list;
        switch (uriMatcher.match(uri)) {
            case URI_CONTACTS:
                Log.d(LOG_TAG, "URI_CONTACTS");
                list=new Delete().from(Person.class).execute();
                break;
            case URI_CONTACTS_ID:
                String id = uri.getLastPathSegment();
                list=new Delete().from(Person.class).where(PERSON_ID+ " = ?", id).execute();
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        //db = dbHelper.getWritableDatabase();
        //int cnt = db.delete(PERSONS_TABLE, selection, selectionArgs);
        //Log.d(MainActivity.LOG_TAG,list.toString());
        getContext().getContentResolver().notifyChange(uri, null);
        return 0;
    }

    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.d(LOG_TAG, "update, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_CONTACTS:
                Log.d(LOG_TAG, "URI_CONTACTS");

                break;
            case URI_CONTACTS_ID:
/*                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = CONTACT_ID + " = " + id;
                } else {
                    selection = selection + " AND " + CONTACT_ID + " = " + id;
                }*/
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        //db = dbHelper.getWritableDatabase();
        //int cnt = db.update(PERSONS_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return -1;
    }

    public String getType(Uri uri) {
        Log.d(LOG_TAG, "getType, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_CONTACTS:
                return PERSON_CONTENT_TYPE;
            case URI_CONTACTS_ID:
                return PERSON_CONTENT_ITEM_TYPE;
        }
        return null;
    }


}

package com.swyftlabs.swyftbooks1.ContentProviders;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class BookInformationContentProvider extends ContentProvider {

    public static final String AUTHORITY = "item.swyftbooks.com";
    public static final String BASE = "item";
    public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+'/'+BASE);


    private static SQLiteDatabase db;

    public static final String TABLE = "SEARCHES";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "TITLE";
    public static final String COLUMN_AUTHOR = "AUTHOR";
    public static final String COLUMN_ISBN = "ISBN";
    public static final String COLUMN_EAN = "EAN";
    public static final String COLUMN_EDITION = "EDITION";
    public static final String COLUMN_BINDING = "BINDING";
    public static final String COLUMN_PUBLISHER = "PUBLISHER";
    public static final String COLUMN_LISTPRICE = "LISTPRICE";
    public static final String COLUMN_BOOKIMAGE = "BOOKIMAGE";

    public static final int ALL_BOOKS = 1; // get all books

    public static final String MIME_ALL_BOOKS = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.item.swyftbooks.com";

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, BASE, ALL_BOOKS);
    }

    public static class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context) {
            super(context, "SEARCHES", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try{
                db.beginTransaction();
                String sql = String.format(
                        "CREATE TABLE %s " +
                                "(%s integer primary key autoincrement, " +
                                "%s text, %s text, %s text, %s text, %s text, %s text, %s text, %s text, %s text)",
                        TABLE, COLUMN_ID, COLUMN_TITLE, COLUMN_AUTHOR,
                        COLUMN_ISBN,COLUMN_EAN, COLUMN_EDITION, COLUMN_PUBLISHER,COLUMN_BINDING,
                        COLUMN_LISTPRICE, COLUMN_BOOKIMAGE);

                db.execSQL(sql);
                db.setTransactionSuccessful();
            }finally {
                db.endTransaction();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public BookInformationContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int numDeleted = 0;
        switch (URI_MATCHER.match(uri)){
            case ALL_BOOKS: {
                numDeleted = db.delete(TABLE, selection, selectionArgs);
                break;
            }
        }
        if(numDeleted != 0){
            if(getContext() != null){
                getContext().getContentResolver().notifyChange(uri, null);
            }
        }
        return numDeleted;
    }

    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)){
            case ALL_BOOKS:
                return MIME_ALL_BOOKS;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = db.insert(TABLE, "HOME_PHONE", values);
        if(getContext() == null){
            throw new RuntimeException("No content available");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    @Override
    public boolean onCreate() {
        db = new OpenHelper(getContext()).getWritableDatabase();
        return true;
    }

    public static SQLiteDatabase getDb() {
        return db;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch (URI_MATCHER.match(uri)){
            case ALL_BOOKS:
                Cursor cursor = db.query(TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                if(getContext() != null){
                    cursor.setNotificationUri(getContext().getContentResolver(), uri);
                }
                return cursor;
            default:
                return null;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int numChanges = 0;
        switch (URI_MATCHER.match(uri)) {
            case ALL_BOOKS: {
                numChanges = db.update(TABLE, values, selection, selectionArgs);
                break;
            }
        }
        if (numChanges != 0) {
            if (getContext() == null) {
                throw new RuntimeException("No content available");
            }
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numChanges;
    }
}

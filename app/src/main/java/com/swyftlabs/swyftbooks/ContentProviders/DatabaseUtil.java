package com.swyftlabs.swyftbooks.ContentProviders;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.swyftlabs.swyftbooks.Classes.ResultItem;

import java.util.ArrayList;

/**
 * Created by Gerard on 2/27/2017.
 */

public class DatabaseUtil {

    public static ResultItem findContact(Context context, long id){
        Uri uri = ContentUris.withAppendedId(BookInformationContactProvider.CONTENT_URI, id);
        String[] projection = {BookInformationContactProvider.COLUMN_ID,BookInformationContactProvider.COLUMN_TITLE
                ,BookInformationContactProvider.COLUMN_AUTHOR, BookInformationContactProvider.COLUMN_ISBN,
                BookInformationContactProvider.COLUMN_EDITION,BookInformationContactProvider.COLUMN_BINDING,BookInformationContactProvider.COLUMN_PUBLISHER,
                BookInformationContactProvider.COLUMN_LISTPRICE, BookInformationContactProvider.COLUMN_BOOKIMAGE};
        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if(cursor == null || !cursor.moveToFirst()){
                return null;
            }
            return new ResultItem(cursor.getLong(0),cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9));
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
    }

    public static ArrayList<ResultItem> getContacts(Context context){
        ArrayList<ResultItem> items = new ArrayList<>();
        Uri uri = BookInformationContactProvider.CONTENT_URI;
        String[] projection = {BookInformationContactProvider.COLUMN_ID,BookInformationContactProvider.COLUMN_TITLE
                ,BookInformationContactProvider.COLUMN_AUTHOR, BookInformationContactProvider.COLUMN_ISBN, BookInformationContactProvider.COLUMN_EAN,
                BookInformationContactProvider.COLUMN_EDITION,BookInformationContactProvider.COLUMN_BINDING,BookInformationContactProvider.COLUMN_PUBLISHER,
                BookInformationContactProvider.COLUMN_LISTPRICE, BookInformationContactProvider.COLUMN_BOOKIMAGE};
        Cursor cursor = null;

        try {
            cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if(cursor == null || !cursor.moveToFirst()){
                return null;
            }else {
                do {
                    items.add(new ResultItem(cursor.getLong(0), cursor.getString(1), cursor.getString(2),
                            cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9)));
                }while(cursor.moveToNext());
            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return items;
    }

    public static void updateContact(Context context, ResultItem resultItem){
        ContentValues contentValues = new ContentValues();

        contentValues.put(BookInformationContactProvider.COLUMN_TITLE, resultItem.getBookTitle());
        contentValues.put(BookInformationContactProvider.COLUMN_AUTHOR, resultItem.getBookAuthor());
        contentValues.put(BookInformationContactProvider.COLUMN_ISBN, resultItem.getBookISBN());
        contentValues.put(BookInformationContactProvider.COLUMN_EAN, resultItem.getBookEAN());
        contentValues.put(BookInformationContactProvider.COLUMN_EDITION, resultItem.getBookEdition());
        contentValues.put(BookInformationContactProvider.COLUMN_BINDING, resultItem.getBookBinding());
        contentValues.put(BookInformationContactProvider.COLUMN_PUBLISHER, resultItem.getBookPublisher());
        contentValues.put(BookInformationContactProvider.COLUMN_LISTPRICE, resultItem.getBookListPrice());
        contentValues.put(BookInformationContactProvider.COLUMN_BOOKIMAGE, resultItem.getBookImageLink());

        if(resultItem.getId() != -1) {
            Uri uri = ContentUris.withAppendedId(BookInformationContactProvider.CONTENT_URI, resultItem.getId());
            context.getContentResolver().update(uri, contentValues, null, null);
        } else{
            Uri uri = context.getContentResolver().insert(BookInformationContactProvider.CONTENT_URI, contentValues);
            String id = uri.getLastPathSegment();
            long idLong = Long.parseLong(id);
            resultItem.setId(idLong);
        }
    }

    public static void delete(Context context, long itemId) {
        try {
            Uri uri = BookInformationContactProvider.CONTENT_URI;
            String selection = BookInformationContactProvider.COLUMN_ID + "=?";
            String selectionargs[] = { String.valueOf(itemId) };
            context.getContentResolver().delete(uri, selection, selectionargs);
        } catch (Exception e) {
        }
    }

}

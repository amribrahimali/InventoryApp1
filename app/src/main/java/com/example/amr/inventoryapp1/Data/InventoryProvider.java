package com.example.amr.inventoryapp1.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.amr.inventoryapp1.Data.InventoryContract.InventoryEntry;


/**
 * Created by Amr on 05/04/2018.
 */

public class InventoryProvider extends ContentProvider {

    public static final String LOG_TAG = InventoryProvider.class.getSimpleName();
    public static final int PRODUCT = 100;
    public static final int PRODUCT_ID = 101;
    private InventoryDbHelper mDpHelper;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY,
                InventoryContract.PATH_INVENTORY, PRODUCT);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY,
                InventoryContract.PATH_INVENTORY + "/#", PRODUCT_ID);
    }


    @Override
    public boolean onCreate() {
        mDpHelper = new InventoryDbHelper(getContext());
        return true;
    }


    @Override
    public Cursor query( Uri uri,  String[] projection,  String selection
            , String[] selectionArgs,  String sortOrder) {
        SQLiteDatabase dp = mDpHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCT:
                cursor = dp.query(InventoryEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case PRODUCT_ID:
                selection = InventoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = dp.query(InventoryEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Can't query Unknwon Uri " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCT:
                return InventoryEntry.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return InventoryEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public Uri insert( Uri uri, ContentValues contentValues) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCT:
                return insetProduct(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    public Uri insetProduct(Uri uri, ContentValues contentValues) {

        String name = contentValues.getAsString(InventoryEntry.COLUMN_PRODUCT_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Product name required..!");
        }
        int price = contentValues.getAsInteger(InventoryEntry.COLUMN_PRODUCT_PRICE);
        if (price == 0) {
            throw new IllegalArgumentException("Price can't be 0");
        }
        String supName = contentValues.getAsString(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME);
        if (supName == null) {
            throw new IllegalArgumentException("Supplier name required..!");
        }
        String supNum = contentValues.getAsString(InventoryEntry.COLUMN_SUPPLIER_NUMBER);
        if (supNum == null) {
            throw new IllegalArgumentException("Supplier number required..!");
        }
        SQLiteDatabase db = mDpHelper.getWritableDatabase();
        long id = db.insert(InventoryEntry.TABLE_NAME, null, contentValues);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }


    @Override
    public int delete( Uri uri,  String selection,  String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        int rowsDeleted;
        SQLiteDatabase dp = mDpHelper.getWritableDatabase();
        switch (match) {
            case PRODUCT:
                rowsDeleted = dp.delete(InventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PRODUCT_ID:
                selection = InventoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = dp.delete(InventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update( Uri uri,  ContentValues contentValues, String selection,  String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        SQLiteDatabase dp = mDpHelper.getWritableDatabase();
        int rows = 0;
        switch (match) {
            case PRODUCT:
                rows = dp.update(InventoryEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case PRODUCT_ID:
                selection = InventoryEntry._ID + " =?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rows = dp.update(InventoryEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rows;
    }
}


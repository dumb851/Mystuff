package com.zubrid.mystuff.lab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zubrid.mystuff.database.BaseHelper;
import com.zubrid.mystuff.database.DbSchemas;
import com.zubrid.mystuff.database.MyCursorWrapper;
import com.zubrid.mystuff.model.ItemStuff;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class ItemStuffLab {

    public static final String TAG = "ItemStuffLab";
    private static ItemStuffLab sItemStuffLab;

    private SQLiteDatabase mDatabase;

    private ItemStuffLab(Context context) {

        mDatabase = new BaseHelper(context).getWritableDatabase();

    }

    public static ItemStuffLab get(Context context) {

        if (sItemStuffLab == null) {
            sItemStuffLab = new ItemStuffLab(context);
        }
        return sItemStuffLab;
    }

    public ArrayList<ItemStuff> getItems() {

        //! TODO hide deletionMarked

        int orderNumber = 0;

        ArrayList<ItemStuff> itemStuffs = new ArrayList<>();
        MyCursorWrapper cursor = queryItems(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                ItemStuff itemStuff = cursor.getItem();
                itemStuff.setOrderNumber(orderNumber++);
                itemStuffs.add(itemStuff);

                cursor.moveToNext();

            }

        } finally {
            cursor.close();
        }

        return itemStuffs;

    }


    private MyCursorWrapper queryItems(String whereClause, String[] whereArgs) {

        String orderBy = DbSchemas.ItemsTable.Cols.TITLE;

        Cursor cursor = mDatabase.query(
                DbSchemas.ItemsTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                orderBy
        );

        return new MyCursorWrapper(cursor);
    }

    public void addItem(ItemStuff itemStuff) {

        ContentValues values = getContentValues(itemStuff);
        mDatabase.insert(DbSchemas.ItemsTable.NAME, null, values);
    }

    public void deleteItem(ItemStuff itemStuff) {

        deleteItemByUUID(itemStuff.getId());

    }

    public void deleteItemByUUID(UUID uuid) {

        String uuidString = uuid.toString();

        mDatabase.delete(DbSchemas.ItemsTable.NAME, DbSchemas.ItemsTable.Cols.UUID + " = ?",
                new String[]{uuidString});

    }

    private static ContentValues getContentValues(ItemStuff itemStuff) {

        ContentValues values = new ContentValues();
        values.put(DbSchemas.ItemsTable.Cols.UUID, itemStuff.getId().toString());
        values.put(DbSchemas.ItemsTable.Cols.TITLE, itemStuff.getTitle());
        values.put(DbSchemas.ItemsTable.Cols.DATE, new Date().getTime());
        values.put(DbSchemas.ItemsTable.Cols.DELETION_MARK, itemStuff.getDeletionMark());

        return values;
    }

    public ItemStuff getItem(UUID id) {

        MyCursorWrapper cursor = queryItems(
                DbSchemas.ItemsTable.Cols.UUID + " =?",
                new String[]{id.toString()}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getItem();

        } finally {
            cursor.close();
        }

    }

    public void updateItem(ItemStuff itemStuff) {

        String uuidString = itemStuff.getId().toString();
        ContentValues values = getContentValues(itemStuff);

        int result;

        result = mDatabase.update(DbSchemas.ItemsTable.NAME, values,
                DbSchemas.ItemsTable.Cols.UUID + " = ?",
                new String[]{uuidString}
        );

        Log.i(TAG, "update result= " + result);
    }

    public void moveItemToTrash(ItemStuff itemStuff) {

        itemStuff.setDeletionMark(true);
        updateItem(itemStuff);

    }

}

package com.zubrid.mystuff.database;

import android.database.Cursor;

import com.zubrid.mystuff.model.Item;

import java.util.Date;
import java.util.UUID;

public class ItemsCursorWrapper extends android.database.CursorWrapper {

    public ItemsCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Item getItem() {

        String uuidString = getString(getColumnIndex(DbSchemas.ItemsTable.Cols.UUID));
        String title = getString(getColumnIndex(DbSchemas.ItemsTable.Cols.TITLE));
        long lastSavedDate = getLong(getColumnIndex(DbSchemas.ItemsTable.Cols.DATE));
        int deletionMark = getInt(getColumnIndex(DbSchemas.ItemsTable.Cols.DELETION_MARK));

        Item item = new Item(UUID.fromString(uuidString));
        item.setTitle(title);
        item.setLastSavedDate(new Date(lastSavedDate));
        item.setDeletionMark(deletionMark);

        return item;
    }
}

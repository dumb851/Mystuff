package com.zubrid.mystuff.database;

import android.database.Cursor;

import com.zubrid.mystuff.model.ItemStuff;
import com.zubrid.mystuff.model.ItemLabel;
import com.zubrid.mystuff.model.Label;

import java.util.Date;
import java.util.UUID;

public class MyCursorWrapper extends android.database.CursorWrapper {

    public MyCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public ItemStuff getItem() {

        String uuidString = getString(getColumnIndex(DbSchemas.ItemsTable.Cols.UUID));
        String title = getString(getColumnIndex(DbSchemas.ItemsTable.Cols.TITLE));
        long lastSavedDate = getLong(getColumnIndex(DbSchemas.ItemsTable.Cols.DATE));
        int deletionMark = getInt(getColumnIndex(DbSchemas.ItemsTable.Cols.DELETION_MARK));

        ItemStuff itemStuff = new ItemStuff(UUID.fromString(uuidString));
        itemStuff.setTitle(title);
        itemStuff.setLastSavedDate(new Date(lastSavedDate));
        itemStuff.setDeletionMark(deletionMark);

        return itemStuff;
    }

    public ItemLabel getItemWithLabel() {

        String uuidItem = getString(getColumnIndex(DbSchemas.ItemLabelsTable.Cols.UUID_ITEM));
        String uuidLabel = getString(getColumnIndex(DbSchemas.ItemLabelsTable.Cols.UUID_LABEL));

        return new ItemLabel(UUID.fromString(uuidItem), UUID.fromString(uuidLabel));
    }

    public Label getLabel() {

        String uuidString = getString(getColumnIndex(DbSchemas.LabelsTable.Cols.UUID));
        String title = getString(getColumnIndex(DbSchemas.LabelsTable.Cols.TITLE));
        long lastSavedDate = getLong(getColumnIndex(DbSchemas.LabelsTable.Cols.DATE));

        Label label = new Label(UUID.fromString(uuidString));
        label.setTitle(title);
        label.setLastSavedDate(new Date(lastSavedDate));

        return label;
    }
}

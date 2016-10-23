package com.zubrid.mystuff.database;

import android.database.Cursor;

import com.zubrid.mystuff.model.ItemLabel;

import java.util.UUID;

public class ItemLabelsCursorWrapper extends android.database.CursorWrapper {

    public ItemLabelsCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public ItemLabel getItemWithLabel() {

        String uuidItem = getString(getColumnIndex(DbSchemas.ItemLabelsTable.Cols.UUID_ITEM));
        String uuidLabel = getString(getColumnIndex(DbSchemas.ItemLabelsTable.Cols.UUID_LABEL));

        return new ItemLabel(UUID.fromString(uuidItem), UUID.fromString(uuidLabel));
    }
}

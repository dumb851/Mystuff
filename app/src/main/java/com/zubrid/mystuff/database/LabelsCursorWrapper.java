package com.zubrid.mystuff.database;

import android.database.Cursor;
import com.zubrid.mystuff.model.Label;

import java.util.Date;
import java.util.UUID;

public class LabelsCursorWrapper extends android.database.CursorWrapper {

    public LabelsCursorWrapper(Cursor cursor) {
        super(cursor);
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

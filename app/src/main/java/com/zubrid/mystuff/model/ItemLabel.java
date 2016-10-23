package com.zubrid.mystuff.model;

import java.util.UUID;

public class ItemLabel {

    private UUID mItemUUID;
    private UUID mLabelUUID;

    public ItemLabel(UUID itemUUID, UUID labelUUID) {
        mItemUUID = itemUUID;
        mLabelUUID = labelUUID;
    }

    public UUID getItemUUID() {
        return mItemUUID;
    }

    public UUID getLabelUUID() {
        return mLabelUUID;
    }
}

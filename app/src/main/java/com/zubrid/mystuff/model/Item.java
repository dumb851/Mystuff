package com.zubrid.mystuff.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Item implements iItemList{

    private UUID mId;
    private String mTitle;
    private String mDescription;
    private Date mLastSavedDate;
    private List<Label> mLabels;
    private int orderNumber;
    private boolean mIsSeparator;

    public boolean isSeparator() {
        return mIsSeparator;
    }

    public Item() {
        this(UUID.randomUUID());
    }

    public Item(UUID id) {
        mId = id;
        mLastSavedDate = new Date();
        mIsSeparator = false;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setLastSavedDate(Date changeDate) {
        mLastSavedDate = changeDate;
    }

    public Date getLastSavedDate() {
        return mLastSavedDate;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public List<Label> getLabels() {
        return mLabels;
    }

    public void setLabels(List<Label> labels) {
        mLabels = labels;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setIsSeparator(boolean isSeparator) {
        mIsSeparator = isSeparator;
    }

    public int getOrderNumber() {
        return orderNumber;
    }
}

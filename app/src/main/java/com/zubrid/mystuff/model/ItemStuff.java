package com.zubrid.mystuff.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ItemStuff implements iItemList{

    private UUID mId;
    private String mTitle;
    private String mDescription;
    private Date mLastSavedDate = null;
    private List<Label> mLabels;
    private int orderNumber;
    private boolean mIsSeparator;
    private boolean mDeletionMark;

    public int getDeletionMark() {

        if (mDeletionMark) {
            return 1;
        }

        return 0;
    }

    public boolean isDeletionMark() {
        return mDeletionMark;
    }

    public void setDeletionMark(boolean deletionMark) {

        mDeletionMark = deletionMark;
    }

    public void setDeletionMark(int i) {

        if (i != 0) {
            mDeletionMark = true;
        } else {
            mDeletionMark = false;
        }

    }

    public boolean isSeparator() {
        return mIsSeparator;
    }

    public ItemStuff() {
        this(UUID.randomUUID());
    }

    public ItemStuff(UUID id) {
        mId = id;
        //!mLastSavedDate = new Date();
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

    public String getTitleFirstLetter() {

        String firstLetter;
        if (mTitle == null || mTitle.length() == 0) {
            firstLetter = "";
        } else {
            firstLetter = String.valueOf(mTitle.charAt(0));
        }

        return firstLetter;
    }
}

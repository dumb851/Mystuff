package com.zubrid.mystuff.model;

import java.io.Serializable;

public class ChoiceItem<T> implements Serializable{

    private T mItem;
    private boolean mChecked;
    private String mTitle;

    public ChoiceItem(T item, String title) {
        mItem = item;
        mChecked = false;
        mTitle = title;
    }

    public T getItem() {
        return mItem;
    }

    public void setItem(T item) {
        mItem = item;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
}

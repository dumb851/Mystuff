package com.zubrid.mystuff.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zubrid.mystuff.lab.ItemStuffLab;
import com.zubrid.mystuff.R;
import com.zubrid.mystuff.model.ItemStuff;
import com.zubrid.mystuff.model.iItemList;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //!List<iItemList> mItemStuffs = new ArrayList<>();
    List<ItemStuff> mItemStuffs;

    public static final int TYPE_ITEM = 0;
    public static final int TYPE_SEPARATOR = 1;

    public ItemsAdapter(Context context) {
        mItemStuffs = ItemStuffLab.get(context).getItems();
    }

    public iItemList getItem(int position) {
        return mItemStuffs.get(position);
    }

    public void addItem(ItemStuff itemStuff) {
        mItemStuffs.add(itemStuff);
        notifyItemInserted(getItemCount() - 1);
    }

    public void addItem(int position, ItemStuff itemStuff) {
        mItemStuffs.add(itemStuff);
        notifyItemInserted(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case TYPE_ITEM:
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_list, parent, false);

                TextView title = (TextView) v.findViewById(R.id.item_list_title_text_view);
                TextView date = (TextView) v.findViewById(R.id.item_list_id_text_view);

                return new TaskViewHolder(v, title, date);
            case TYPE_SEPARATOR:
                return null; //!

            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        iItemList item = mItemStuffs.get(position);

        if (!item.isSeparator()) {
            holder.itemView.setEnabled(true);

            ItemStuff task = (ItemStuff) item;

            TaskViewHolder taskViewHolder = (TaskViewHolder) holder;

            taskViewHolder.title.setText(task.getTitle());

        }
    }

    @Override
    public int getItemCount() {
        return mItemStuffs.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).isSeparator()) {
            return TYPE_SEPARATOR;
        } else {
            return TYPE_ITEM;
        }
    }

    private class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView date;

        public TaskViewHolder(View itemView, TextView title, TextView date) {
            super(itemView);

            this.title = title;
            this.date = date;
        }
    }
}

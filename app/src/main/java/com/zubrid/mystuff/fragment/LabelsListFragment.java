package com.zubrid.mystuff.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zubrid.mystuff.lab.LabelLab;
import com.zubrid.mystuff.R;
import com.zubrid.mystuff.activity.LabelPagerActivity;
import com.zubrid.mystuff.model.Label;

import java.util.ArrayList;
import java.util.UUID;

public class LabelsListFragment extends Fragment{


    static final int INTENT_NEW_LABEL = 2;

    private LabelAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    private ArrayList<Label> mLabels;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.list_labels, container, false);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab_labels_list);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new LabelPagerActivity().newIntent(getContext(), null);
                startActivityForResult(intent, INTENT_NEW_LABEL);
            }
        });

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView = (RecyclerView) v.findViewById(R.id.label_recycler_view);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mLabels = LabelLab.get(getActivity()).getLabels();
        mAdapter = new LabelAdapter();
        mRecyclerView.setAdapter(mAdapter);
        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

         if (resultCode == Activity.RESULT_OK) {
            mLabels = LabelLab.get(getActivity()).getLabels();

            ArrayList<UUID> changedItems = (ArrayList<UUID>) data.getSerializableExtra("changedLabels");

            for (UUID itemId : changedItems) {

                Label newLabel = null;
                Label newSeparator = null;

                for (Label item : mLabels) {
                    if (item.getId().equals(itemId)) {
                        newLabel = item;

                        int index = mLabels.indexOf(item);
                        if (index != 0) {
                            Label itemBefore = mLabels.get(index - 1);
                            if (itemBefore.isSeparator()) {
                                newSeparator = itemBefore;
                            }
                        }

                        break;
                    }
                }

                if (newLabel != null) {
                    if (newSeparator != null) {
                        mAdapter.notifyLabelInserted(newSeparator);
                    }

                    mAdapter.notifyLabelInserted(newLabel);
                    mLinearLayoutManager.scrollToPosition(newLabel.getOrderNumber());
                }
            }
        }

    }

    public static LabelsListFragment newInstance() {

        return new LabelsListFragment();
    }


    // RecyclerView

    private class LabelAdapter extends RecyclerView.Adapter<LabelHolder> {

        public static final int VIEW_TYPE_Label = 0;
        public static final int VIEW_TYPE_SEPARATOR = 1;


        @Override
        public LabelHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if (viewType == VIEW_TYPE_SEPARATOR) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.separator, parent, false);
                return new LabelHolder(view, viewType);
            }

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.label_list, parent, false);
            return new LabelHolder(view, viewType);

        }

        @Override
        public void onBindViewHolder(LabelHolder holder, int position) {

            holder.itemView.setEnabled(true);

            Label label = mLabels.get(position);
            holder.bindLabel(label);

        }

        @Override
        public int getItemViewType(int position) {

            Label label = mLabels.get(position);
            if (label.isSeparator()) {
                return VIEW_TYPE_SEPARATOR;
            }

            return VIEW_TYPE_Label;

        }

        //        @Override
//        public void onBindViewHolder(ItemHolder holder, int pos) {
//            Item item = mItems.get(pos);
//            holder.bindItem(item);
//            Log.d(TAG, "binding crime" + item + "at position" + pos);
//        }

        @Override
        public int getItemCount() {
            return mLabels.size();
        }

        public void notifyLabelInserted(Label label) {

            label.getOrderNumber();

            notifyItemInserted(label.getOrderNumber());
        }
    }

    private class LabelHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {

        private final TextView mTitleTextView;
        private final TextView mIdTextView;
        private Label mLabel;

        public LabelHolder(View labelView, int viewType) {
            //!super(itemView, mMultiSelector);

            super(labelView);

            if (viewType == LabelAdapter.VIEW_TYPE_SEPARATOR) {

                mTitleTextView = (TextView) labelView.findViewById(R.id.separator_title);
                mIdTextView = null;

            } else {

                mTitleTextView = (TextView) labelView.findViewById(R.id.label_list_title_text_view);
                mIdTextView = (TextView) labelView.findViewById(R.id.label_list_id_text_view);

                labelView.setOnClickListener(this);
                //itemView.getId();
                labelView.setLongClickable(true);
                labelView.setOnLongClickListener(this);
            }


            //!mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.crime_list_item_solvedCheckBox);

//            CardView cardView = (CardView) itemView.findViewById(R.id.card_view);
//
//            cardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Snackbar.make(view, "onClick", Snackbar.LENGTH_SHORT).show();
//                }
//            });

        }

        public void bindLabel(Label label) {
            mLabel = label;
            mTitleTextView.setText(label.getTitle());
            if (!label.isSeparator()) {
                mIdTextView.setText(label.getId().toString());
            }

        }

        //
        @Override
        public void onClick(View v) {

            //TODO
            Snackbar.make(v, "onClick", Snackbar.LENGTH_SHORT).show();

            if (mLabel == null) {
                return;
            }

            selectLabel(mLabel);

        }

        //
        @Override
        public boolean onLongClick(View v) {

            //TODO
            Snackbar.make(v, "onLongClick", Snackbar.LENGTH_SHORT).show();

//!                ((AppCompatActivity) getActivity()).startSupportActionMode(mDeleteMode);
//                mMultiSelector.setSelected(this, true);
            return true;
        }
    }

    private void selectLabel(Label label) {

        Intent intent = new LabelPagerActivity().newIntent(getContext(), label.getId());
        startActivityForResult(intent, 0);

//!        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            // NOTE: shared element transition here.
//            // Support library fragments do not support the three parameter
//            // startActivityForResult call. So to get this to work, the entire
//            // project had to be shifted over to use stdlib fragments,
//            // and the v13 ViewPager.
//            int index = mItems.indexOf(item);
//            ItemHolder holder = (ItemHolder) mRecyclerView
//                    .findViewHolderForPosition(index);
//
//            ActivityOptions options = ItemActivity.getTransition(
//                    getActivity(), holder.itemView);
//
//            startActivityForResult(intent, 0, options.toBundle());
//        } else {
//            startActivityForResult(intent, 0);
//        }
    }
}


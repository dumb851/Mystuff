package com.zubrid.mystuff.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;

import com.zubrid.mystuff.R;
import com.zubrid.mystuff.lab.ItemLab;
import com.zubrid.mystuff.lab.ItemLabelsLab;
import com.zubrid.mystuff.lab.LabelLab;
import com.zubrid.mystuff.model.ChoiceItem;
import com.zubrid.mystuff.model.Item;
import com.zubrid.mystuff.model.Label;

import java.util.ArrayList;
import java.util.UUID;

public class ChoiceLabelFragment extends Fragment{

    public static final String EXTRA_ITEM_ID = "mystuff.ITEM_ID";
    private Adapter mAdapter;
    private RecyclerView mRecyclerView;
    private ArrayList<ChoiceItem> mAllLabels = new ArrayList<>();
    private ArrayList<ChoiceItem> mChangedLabels = new ArrayList<>();
    private Item mItem;

    public static ChoiceLabelFragment newInstance() {
//        Bundle args = new Bundle();
//        args.putSerializable(EXTRA_ITEM_ID, itemId);

        ChoiceLabelFragment fragment = new ChoiceLabelFragment();
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getActivity().getIntent();
        UUID itemId = (UUID) intent.getSerializableExtra(ChoiceLabelFragment.EXTRA_ITEM_ID);

        mItem = ItemLab.get(getActivity()).getItem(itemId);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,
                              Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.choice_label_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.choice_label_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new Adapter(getContext());


        mRecyclerView.setAdapter(mAdapter);
        ArrayList<Label> allLabels = LabelLab.get(getActivity()).getLabels(false);
        ArrayList<Label> labelsByItem = ItemLabelsLab.get(getActivity()).getLabelsByItem(mItem);

        for (Label label : allLabels) {

            if (labelsByItem.contains(label)) {
                Log.d("ChoiceLabelFragment", "contains: " + label.getTitle());
            }

            ChoiceItem<Label> choiceItem = new ChoiceItem<>(label, label.getTitle());
            mAllLabels.add(choiceItem);
        }

        mAdapter.getFilter().filter("");

        EditText searchText = (EditText) view.findViewById(R.id.choice_label_list_search_text_edit);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        return view;
    }

    public ArrayList<ChoiceItem> getChangedLabels() {

        return mChangedLabels;
    }

    private class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>
            implements Filterable {

        private LabelFilter mLabelFilter;
        private final ArrayList<ChoiceItem> mFilteredLabel;

        public Adapter(Context context) {
            mFilteredLabel = new ArrayList<>();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.choice_label_label, parent, false);

            return new ViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ChoiceItem choiceItem = mFilteredLabel.get(position);

            holder.bindItem(choiceItem);

        }

        @Override
        public int getItemCount() {
            return mFilteredLabel.size();
        }

        @Override
        public Filter getFilter() {
            if(mLabelFilter == null)
                mLabelFilter = new LabelFilter(this, mAllLabels);
            return mLabelFilter;
        }

        private class LabelFilter extends Filter {

            private final Adapter mAdapter;
            private final ArrayList<ChoiceItem> mOriginalList;
            private final ArrayList<ChoiceItem> mFilteredList;

            public LabelFilter(Adapter adapter, ArrayList<ChoiceItem> originalList) {
                mAdapter = adapter;
                mOriginalList = originalList;
                mFilteredList = new ArrayList<>();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                mFilteredList.clear();
                final FilterResults results = new FilterResults();

                if (constraint.length() == 0) {
                    mFilteredList.addAll(mOriginalList);
                } else {
                    final String filterPattern = constraint.toString().toLowerCase().trim();

                    for (final ChoiceItem choiceItem : mOriginalList) {
                        if (choiceItem.getTitle().contains(filterPattern)) {
                            mFilteredList.add(choiceItem);
                        }
                    }
                }
                results.values = mFilteredList;
                results.count = mFilteredList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mAdapter.mFilteredLabel.clear();
                mAdapter.mFilteredLabel.addAll((ArrayList<ChoiceItem>) filterResults.values);
                mAdapter.notifyDataSetChanged();
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private ChoiceItem mChoiceItem;
            private final CheckBox mCheckBox;

            public ViewHolder(View itemView, int viewType) {
                super(itemView);
                mCheckBox = (CheckBox) itemView.findViewById(R.id.choice_label_label_check_box);

                mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        mChoiceItem.setChecked(b);

                        int i = mChangedLabels.indexOf(mChoiceItem);
                        if (i != -1) {
                            mChangedLabels.remove(i);
                        }

                        mChangedLabels.add(mChoiceItem);
                    }
                });
            }

            public void bindItem(ChoiceItem choiceItem) {
                mChoiceItem = choiceItem;
                mCheckBox.setText(choiceItem.getTitle());
                mCheckBox.setChecked(choiceItem.isChecked());
            }

        }
    }
}




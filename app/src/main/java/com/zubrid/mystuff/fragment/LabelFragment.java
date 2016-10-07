package com.zubrid.mystuff.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.zubrid.mystuff.lab.LabelLab;
import com.zubrid.mystuff.R;
import com.zubrid.mystuff.model.Label;

import java.util.ArrayList;
import java.util.UUID;

public class LabelFragment extends Fragment{

    public static final String EXTRA_LABEL_ID = "mystuff.Label_ID";

    private EditText mTitleField;
    private TextView mIdView;
    private TextView mLastSaved;
    private boolean mIsNewLabel = false;
    //!private Intent mIntentResult;
    private Label mLabel;

    private ArrayList<UUID> mChangedLabels = new ArrayList<>();


    public static LabelFragment newInstance(UUID labelId) {

        Bundle args = new Bundle();
        args.putSerializable(EXTRA_LABEL_ID, labelId);

        LabelFragment fragment = new LabelFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        UUID labelId = (UUID) getArguments().getSerializable(EXTRA_LABEL_ID);

        if (labelId == null) {
            mLabel = new Label();
            mIsNewLabel = true;
        } else {
            mLabel = LabelLab.get(getActivity()).getLabel(labelId);
            if (mLabel == null) {
                mLabel = new Label(labelId);
                mIsNewLabel = true;
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //!inflater.inflate(R.menu.label_menu_fragment, menu);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.label, container, false);

        mTitleField = (EditText) view.findViewById(R.id.label_title_text_edit);
        mTitleField.setText(mLabel.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mLabel.setTitle(s.toString());
                updateLabel();
            }
        });

        mIdView = (TextView) view.findViewById(R.id.label_id_text_view);
        mIdView.setText(mLabel.getId().toString());

        mLastSaved = (TextView) view.findViewById(R.id.label_last_saved_text_view);
        mLastSaved.setText(mLabel.getLastSavedDate().toString());

        return view;
    }

    private void updateLabel() {

        if (mIsNewLabel) {

            LabelLab.get(getActivity()).addLabel(mLabel);
            mIsNewLabel = false;

//            Intent intent = new Intent();
//            intent.putExtra("labelId", mL.getId());
//            getActivity().setResult(Activity.RESULT_OK, intent);

        } else {
            LabelLab.get(getActivity()).updateLabel(mLabel);

            //mCallbacks.onCrimeUpdated(mCrime);
            //Log.i("updateItem", mItem.getTitle() + " / " + mItem.getId().toString());
        }

        if (!mChangedLabels.contains(mLabel.getId())) {
            mChangedLabels.add(mLabel.getId());
        }

        Intent intent = new Intent();
        intent.putExtra("changedLabels", mChangedLabels);
        getActivity().setResult(Activity.RESULT_OK, intent);


        mLabel = LabelLab.get(getActivity()).getLabel(mLabel.getId());
        mLastSaved.setText(mLabel.getLastSavedDate().toString());
        //Log.i("setLastSaved", mLabel.getLastSavedDate().toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_item_delete:

                Intent intent = new Intent();
                intent.putExtra("itemId", mLabel.getId().toString());
                getActivity().setResult(Activity.RESULT_OK, intent);

                //!ItemLab.get(getActivity()).deleteItem(mItem);
                getActivity().finish();
                return true;
            default:

                return super.onOptionsItemSelected(item);

        }

    }
}


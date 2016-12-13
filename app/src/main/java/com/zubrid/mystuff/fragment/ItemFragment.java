package com.zubrid.mystuff.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zubrid.mystuff.R;
import com.zubrid.mystuff.activity.ChoiceLabelActivity;
import com.zubrid.mystuff.lab.ItemLab;
import com.zubrid.mystuff.lab.ItemLabelsLab;
import com.zubrid.mystuff.model.ChoiceItem;
import com.zubrid.mystuff.model.Item;
import com.zubrid.mystuff.model.Label;

import java.util.ArrayList;
import java.util.UUID;

public class ItemFragment extends Fragment {

    public static final String EXTRA_ITEM_ID = "mystuff.ITEM_ID";
    public static final String EXTRA_DELETED_ITEM = "ITEM_FRAGMENT_EXTRA_DELETED_ITEM";


    public static final int INTENT_CHOICE_LABEL = 1;
    private static final String TAG = "ItemFragment_TAG";

    private EditText mTitleField;
    private TextView mIdView;
    private TextView mLastSaved;
    private boolean mIsNewItem = false;
    private Item mItem;

    private ArrayList<UUID> mChangedItems = new ArrayList<>();
    private ItemFragmentListener mCallback;
    private boolean mPageAdded;
    private boolean mCreatingNewItems;

    public static ItemFragment newInstance(UUID itemId) {

        Bundle args = new Bundle();
        args.putSerializable(EXTRA_ITEM_ID, itemId);

        ItemFragment fragment = new ItemFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        UUID itemId = (UUID) getArguments().getSerializable(EXTRA_ITEM_ID);

        if (itemId == null) {
            mItem = new Item();
            mIsNewItem = true;
        } else {
            mItem = ItemLab.get(getActivity()).getItem(itemId);
            if (mItem == null) {
                mItem = new Item(itemId);
                mIsNewItem = true;
            }
        }

        if (mIsNewItem) {
            mCreatingNewItems = true;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.item_menu_fragment, menu);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.item, container, false);

        mTitleField = (EditText) view.findViewById(R.id.item_title_text_edit);
        mTitleField.setText(mItem.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mItem.setTitle(s.toString());
                updateItem();
            }
        });

        mIdView = (TextView) view.findViewById(R.id.item_id_text_view);
        mIdView.setText(mItem.getId().toString());

        mLastSaved = (TextView) view.findViewById(R.id.item_last_saved_text_view);
        mLastSaved.setText(mItem.getLastSavedDate().toString());

        Button buttonOpenLabels = (Button) view.findViewById(R.id.button_open_labels);
        buttonOpenLabels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                DialogFragment selectLabelDialog = new SelectLabelDialogFragment();
//                selectLabelDialog.show(getFragmentManager(), "SelectLabelDialog");

                Intent intent = ChoiceLabelActivity.newIntent(getContext(), mItem);
                startActivityForResult(intent, INTENT_CHOICE_LABEL);

            }
        });

        Button buttonAddPage = (Button) view.findViewById(R.id.item_button_add_page);
        buttonAddPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPage();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mIsNewItem) {
            getMyActionBar().setSubtitle(R.string.subtitle_new_item);
        } else {
            getMyActionBar().setSubtitle(R.string.subtitle_item);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Log.i(TAG, "onOptionsItemSelected: home");
        }

        switch (item.getItemId()) {

            case android.R.id.home:
                getActivity().onBackPressed();
                return true;

            case R.id.menu_item_delete:

                Intent intent = new Intent();
                intent.putExtra(EXTRA_DELETED_ITEM, mItem.getId());
                getActivity().setResult(Activity.RESULT_OK, intent);


                getActivity().finish();
                return true;
            default:

                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == INTENT_CHOICE_LABEL) {
            ArrayList<ChoiceItem> changedItems = (ArrayList<ChoiceItem>) data.getSerializableExtra("changedItems");

            for (ChoiceItem changedItem: changedItems) {
                if (changedItem.isChecked()) {
                    ItemLabelsLab.get(getContext()).addLabelToItem((Label)changedItem.getItem(), mItem);
                } else {
                    ItemLabelsLab.get(getContext()).deleteLabelFromItem((Label)changedItem.getItem(), mItem);
                }

            }
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity = (Activity) context;

        try {
            mCallback = (ItemFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ItemFragmentListener");
        }

    }

    private void updateItem() {

        if (mIsNewItem) {

            ItemLab.get(getActivity()).addItem(mItem);
            mIsNewItem = false;

        } else {
            ItemLab.get(getActivity()).updateItem(mItem);

        }

        if (mCreatingNewItems) {
            addPage();
        }

        if (!mChangedItems.contains(mItem.getId())) {
            mChangedItems.add(mItem.getId());
        }

        Intent intent = new Intent();
        intent.putExtra("changedItems", mChangedItems);
        getActivity().setResult(Activity.RESULT_OK, intent);


        mItem = ItemLab.get(getActivity()).getItem(mItem.getId());
        mLastSaved.setText(mItem.getLastSavedDate().toString());
        Log.i("setLastSaved", mItem.getLastSavedDate().toString());
    }

    private void addPage() {

        if (mPageAdded) {
            return;
        }

        mPageAdded = true;
        mCallback.addPage();
    }

    protected ActionBar getMyActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    //CallBack
    public interface ItemFragmentListener{
        void addPage();
    }
}

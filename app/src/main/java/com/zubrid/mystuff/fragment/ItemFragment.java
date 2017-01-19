package com.zubrid.mystuff.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zubrid.mystuff.R;
import com.zubrid.mystuff.activity.ChoiceLabelActivity;
import com.zubrid.mystuff.activity.FlowLayouDemo;
import com.zubrid.mystuff.lab.ItemStuffLab;
import com.zubrid.mystuff.lab.ItemLabelsLab;
import com.zubrid.mystuff.lab.LabelLab;
import com.zubrid.mystuff.model.ChoiceItem;
import com.zubrid.mystuff.model.Image;
import com.zubrid.mystuff.model.ItemLabel;
import com.zubrid.mystuff.model.ItemStuff;
import com.zubrid.mystuff.model.Label;
import com.zubrid.mystuff.utils.FlowLayout;
import com.zubrid.mystuff.utils.PictureUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class ItemFragment extends Fragment {

    public static final String EXTRA_ITEM_ID = "mystuff.ITEM_ID";
    public static final String EXTRA_DELETED_ITEM = "ITEM_FRAGMENT_EXTRA_DELETED_ITEM";

    public static final int INTENT_CHOICE_LABEL = 1;
    private static final int REQUEST_TAKE_PHOTO = 2;
    private static final String TAG = "ItemFragment_TAG";

    //region local variables
    private EditText mTitleField;
    private TextView mIdView;
    private TextView mLastSaved;
    private boolean mIsNewItem = false;
    private ItemStuff mItemStuff;
    private File mPhotoFile; //! TODO temp
    private ArrayList<UUID> mChangedItems = new ArrayList<>();
    private ItemFragmentListener mCallback;
    private boolean mPageAdded;
    private boolean mCreatingNewItems;
    private ImageView mPhotoView;
    private Context mContext;
    private RecyclerView mLabelRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<Label> mLabels = new ArrayList<>();
    private LabelAdapter mLabelAdapter;
    private FlowLayout mLabelsFlowLayout;
    private LayoutInflater mLayoutInflater;
    private TextView mSavedTime;

    //endregion

    //changes for new branch

    //! TODO GridView для отображения меток!

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

        mContext = getContext();

        UUID itemId = (UUID) getArguments().getSerializable(EXTRA_ITEM_ID);

        if (itemId == null) {
            mItemStuff = new ItemStuff();
            mIsNewItem = true;
        } else {
            mItemStuff = ItemStuffLab.get(getActivity()).getItem(itemId);
            if (mItemStuff == null) {
                mItemStuff = new ItemStuff(itemId);
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

        View view = inflater.inflate(R.layout.stuff_item, container, false);

        mLayoutInflater = inflater;

        mTitleField = (EditText) view.findViewById(R.id.item_title_text_edit);
        mTitleField.setText(mItemStuff.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mItemStuff.setTitle(s.toString());
                updateItem();
            }
        });

        mSavedTime = (TextView) view.findViewById(R.id.item_saved_time);

        mIdView = (TextView) view.findViewById(R.id.item_id_text_view);
        mIdView.setText(mItemStuff.getId().toString());

//!        mLastSaved = (TextView) view.findViewById(R.id.item_last_saved_text_view);
//        mLastSaved.setText(mItemStuff.getLastSavedDate().toString());

        if (mIsNewItem) {
            LinearLayout fakeLayout = (LinearLayout) view.findViewById(R.id.item_fake_layout);
            fakeLayout.setVisibility(View.GONE);
        }

        fillLabels();

        configureLabelsView(view);

        updateSavedTime();


        Button buttonOpenLabels = (Button) view.findViewById(R.id.button_open_labels);
        buttonOpenLabels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = ChoiceLabelActivity.newIntent(mContext, mItemStuff);
                startActivityForResult(intent, INTENT_CHOICE_LABEL);

            }
        });


//!         Button addPageButton = (Button) view.findViewById(R.id.item_button_add_page);
//        addPageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addPage();
//            }
//        });


        //
        Button takeImageButton = (Button) view.findViewById(R.id.take_image);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        PackageManager packageManager = getActivity().getPackageManager();

        boolean canTakePhoto = captureImage.resolveActivity(packageManager) != null;
        takeImageButton.setEnabled(canTakePhoto);

        takeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Image image = new Image();
                mPhotoFile = image.getImageFile(mContext);
                Uri uri = Uri.fromFile(mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                startActivityForResult(captureImage, REQUEST_TAKE_PHOTO);

            }
        });

        //! TODO временно в ImageView, нужно список какой нибудь.
        mPhotoView = (ImageView) view.findViewById(R.id.imageView);

        //! test

        Button testButton = (Button) view.findViewById(R.id.testButton);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), FlowLayouDemo.class);
                startActivity(intent);

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
                intent.putExtra(EXTRA_DELETED_ITEM, mItemStuff.getId());
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

            for (ChoiceItem changedItem : changedItems) {
                if (changedItem.isChecked()) {
                    ItemLabelsLab.get(mContext).addLabelToItem((Label) changedItem.getItem(), mItemStuff);
                } else {
                    ItemLabelsLab.get(mContext).deleteLabelFromItem((Label) changedItem.getItem(), mItemStuff);
                }

            }
        } else if (requestCode == REQUEST_TAKE_PHOTO) {
            updatePhotoView();
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

    //region Label list view

    private void configureLabelsView(View view) {

        mLinearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);

        mLabelRecyclerView = (RecyclerView) view.findViewById(R.id.item_recycler_view_labels);
        mLabelRecyclerView.setLayoutManager(mLinearLayoutManager);

        mLabelAdapter = new LabelAdapter();

        mLabelRecyclerView.setAdapter(mLabelAdapter);

        //
        mLabelsFlowLayout = (FlowLayout) view.findViewById(R.id.item_labels_flow_layout);
        FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(6, 6);

        for (Label label : mLabels) {
            View labelView = mLayoutInflater.inflate(R.layout.label_small, null);

            labelView.setLayoutParams(layoutParams);

            TextView labelViewTitle = (TextView)
                    labelView.findViewById(R.id.label_list_title_text_view);

            labelViewTitle.setText(label.getTitle());

            mLabelsFlowLayout.addView(labelView);
        }

    }

    //endregion

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    private void updateSavedTime() {

        //! TODO make format string
        Date savedDate = mItemStuff.getLastSavedDate();

        if (savedDate == null) {
            mSavedTime.setText(R.string.not_saved);
        } else {

            SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss dd.M.yyyy ", Locale.ENGLISH);
            String DateToStr = format.format(savedDate);

            mSavedTime.setText(DateToStr);
        }

    }

    private void updateItem() {

        if (mIsNewItem) {

            ItemStuffLab.get(getActivity()).addItem(mItemStuff);
            mIsNewItem = false;

        } else {
            ItemStuffLab.get(getActivity()).updateItem(mItemStuff);

        }

        if (mCreatingNewItems) {
            addPage();
        }

        if (!mChangedItems.contains(mItemStuff.getId())) {
            mChangedItems.add(mItemStuff.getId());
        }

        Intent intent = new Intent();
        intent.putExtra("changedItems", mChangedItems);
        getActivity().setResult(Activity.RESULT_OK, intent);

        mItemStuff = ItemStuffLab.get(getActivity()).getItem(mItemStuff.getId());
//!        mLastSaved.setText(mItemStuff.getLastSavedDate().toString());
//        Log.i("setLastSaved", mItemStuff.getLastSavedDate().toString());

        updateSavedTime();
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

    private void fillLabels() {

        ArrayList<ItemLabel> itemLabels = ItemLabelsLab.get(getActivity()).getLabelsByItem(mItemStuff);

        LabelLab labelLab = LabelLab.get(getActivity());

        for (ItemLabel itemLabel : itemLabels) {
            Label label = labelLab.getLabel(itemLabel.getLabelUUID());
            mLabels.add(label);
        }
    }


    //CallBack
    public interface ItemFragmentListener {
        void addPage();
    }

    //region Label RecyclerView

    private class LabelAdapter extends RecyclerView.Adapter<ItemFragment.ViewHolder> {

        public static final int VIEW_TYPE_ITEM = 0;
        public static final int VIEW_TYPE_SEPARATOR = 1;


        @Override
        public ItemFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.label_small, parent, false);
            return new ItemFragment.ViewHolder(view, viewType);

        }

        @Override
        public void onBindViewHolder(ItemFragment.ViewHolder holder, int position) {

            holder.itemView.setEnabled(true);

            Label label = mLabels.get(position);
            holder.bindItem(label);

        }


        //        @Override
//        public void onBindViewHolder(ItemHolder holder, int pos) {
//            ItemStuff item = mItems.get(pos);
//            holder.bindItem(item);
//            Log.d(TAG, "binding crime" + item + "at position" + pos);
//        }

        @Override
        public int getItemCount() {
            return mLabels.size();
        }

        public void notifyItemInserted(ItemStuff itemStuff) {

            notifyItemInserted(itemStuff.getOrderNumber());
        }

        public void notifyItemRemoved(ItemStuff itemStuff) {

            notifyItemRemoved(itemStuff.getOrderNumber());
        }

    }

    private class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {

        private final TextView mTitle;
        //        private final TextView mIdTextView;
        private Label mLabel;

        public ViewHolder(View view, int viewType) {
            //!super(itemView, mMultiSelector);

            super(view);

//            if (viewType == ItemsListFragment.ItemAdapter.VIEW_TYPE_SEPARATOR) {
//
            mTitle = (TextView) view.findViewById(R.id.label_list_title_text_view);


//
// mIdTextView = null;
//
//            } else {
//
//                mTitleTextView = (TextView) itemView.findViewById(R.id.item_list_title_text_view);
//                mIdTextView = (TextView) itemView.findViewById(R.id.item_list_id_text_view);
//
//                itemView.setOnClickListener(this);
//                //itemView.getId();
//                itemView.setLongClickable(true);
//                itemView.setOnLongClickListener(this);
//            }


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

        public void bindItem(Label label) {
            mLabel = label;
            mTitle.setText(label.getTitle());

        }

        //
        @Override
        public void onClick(View v) {

//            //Snackbar.make(v, "onClick", Snackbar.LENGTH_SHORT).show();
//
//            if (mItemStuff == null) {
//                return;
//            }

            //!selectItem(mItemStuff);

        }

        //
        @Override
        public boolean onLongClick(View v) {

            //Snackbar.make(v, "onLongClick", Snackbar.LENGTH_SHORT).show();

//!                ((AppCompatActivity) getActivity()).startSupportActionMode(mDeleteMode);
//                mMultiSelector.setSelected(this, true);
            return true;
        }
    }

    //endregion

}

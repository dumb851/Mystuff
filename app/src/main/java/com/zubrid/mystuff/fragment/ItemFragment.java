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
import android.widget.TextView;

import com.zubrid.mystuff.R;
import com.zubrid.mystuff.activity.ChoiceLabelActivity;
import com.zubrid.mystuff.activity.FlowLayouDemo;
import com.zubrid.mystuff.lab.ItemLab;
import com.zubrid.mystuff.lab.ItemLabelsLab;
import com.zubrid.mystuff.lab.LabelLab;
import com.zubrid.mystuff.model.ChoiceItem;
import com.zubrid.mystuff.model.Image;
import com.zubrid.mystuff.model.Item;
import com.zubrid.mystuff.model.ItemLabel;
import com.zubrid.mystuff.model.Label;
import com.zubrid.mystuff.utils.PictureUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import static com.zubrid.mystuff.R.layout.item;

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
    private Item mItem;
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
    //endregion

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

        View view = inflater.inflate(item, container, false);

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

        fillLabels();

        configureLabelListView(view);



        Button buttonOpenLabels = (Button) view.findViewById(R.id.button_open_labels);
        buttonOpenLabels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = ChoiceLabelActivity.newIntent(mContext, mItem);
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

            for (ChoiceItem changedItem : changedItems) {
                if (changedItem.isChecked()) {
                    ItemLabelsLab.get(mContext).addLabelToItem((Label) changedItem.getItem(), mItem);
                } else {
                    ItemLabelsLab.get(mContext).deleteLabelFromItem((Label) changedItem.getItem(), mItem);
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

    private void configureLabelListView(View view) {

        mLinearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);

        mLabelRecyclerView = (RecyclerView) view.findViewById(R.id.item_recycler_view_labels);
        mLabelRecyclerView.setLayoutManager(mLinearLayoutManager);

        mLabelAdapter = new LabelAdapter();

        mLabelRecyclerView.setAdapter(mLabelAdapter);

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

    private void fillLabels() {

        ArrayList<ItemLabel> itemLabels = ItemLabelsLab.get(getActivity()).getLabelsByItem(mItem);

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
//            Item item = mItems.get(pos);
//            holder.bindItem(item);
//            Log.d(TAG, "binding crime" + item + "at position" + pos);
//        }

        @Override
        public int getItemCount() {
            return mLabels.size();
        }

        public void notifyItemInserted(Item item) {

            notifyItemInserted(item.getOrderNumber());
        }

        public void notifyItemRemoved(Item item) {

            notifyItemRemoved(item.getOrderNumber());
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
//            if (mItem == null) {
//                return;
//            }

            //!selectItem(mItem);

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

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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zubrid.mystuff.lab.ItemLab;
import com.zubrid.mystuff.R;
import com.zubrid.mystuff.activity.ItemPagerActivity;
import com.zubrid.mystuff.model.Item;

import java.util.ArrayList;
import java.util.UUID;

public class ItemsListFragment extends Fragment {

    //var
    static final int INTENT_NEW_ITEM = 2;
    private static final String TAG = "ItemsListFragment";
    private ItemAdapter mAdapter;
    private RecyclerView mRecyclerView;
    //private MultiSelector mMultiSelector = new MultiSelector();
    private ArrayList<Item> mItems;
    private LinearLayoutManager mLinearLayoutManager;

    //private ModalMultiSelectorCallback mDeleteMode = new ModalMultiSelectorCallback(mMultiSelector) {

//        @Override
//        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
//            super.onCreateActionMode(actionMode, menu);
//            getActivity().getMenuInflater().inflate(R.menu.item_list_context, menu);
//            return true;
//        }
//
//        @Override
//        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
//            if (menuItem.getItemId() == R.id.menu_item_delete) {
//                // Need to finish the action mode before doing the following,
//                // not after. No idea why, but it crashes.
//                actionMode.finish();
//
//                for (int i = mItems.size(); i >= 0; i--) {
//                    if (mMultiSelector.isSelected(i, 0)) {
//                        Item item = mItems.get(i);
//                        ItemLab.get(getActivity()).deleteItem(item);
//                        mRecyclerView.getAdapter().notifyItemRemoved(i);
//                    }
//                }
//
//                mMultiSelector.clearSelections();
//                return true;
//
//            }
//            return false;
//        }

    //};


    //Override

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

//        if (mMultiSelector != null) {
//            Bundle bundle = savedInstanceState;
//            if (bundle != null) {
//                mMultiSelector.restoreSelectionStates(bundle.getBundle(TAG));
//            }
//
//            if (mMultiSelector.isSelectable()) {
//                if (mDeleteMode != null) {
//                    mDeleteMode.setClearOnPrepare(false);
//                    ((AppCompatActivity) getActivity()).startSupportActionMode(mDeleteMode);
//                }
//
//            }
//        }

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.list_items, container, false);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new ItemPagerActivity().newIntent(getContext(), null);
                    startActivityForResult(intent, INTENT_NEW_ITEM);
                }
            });
        }


        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView = (RecyclerView) v.findViewById(R.id.item_recycler_view);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mItems = ItemLab.get(getActivity()).getItems();
        mAdapter = new ItemAdapter();
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        outState.putBundle(TAG, mMultiSelector.saveSelectionStates());
//        super.onSaveInstanceState(outState);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //if (requestCode == INTENT_NEW_ITEM) {

        if (resultCode == Activity.RESULT_OK) {

            if (data.hasExtra("changedItems")) {

                mItems = ItemLab.get(getActivity()).getItems();

                ArrayList<UUID> changedItems = (ArrayList<UUID>) data.getSerializableExtra("changedItems");

                for (UUID itemId : changedItems) {

                    Item newItem = null;
                    Item newSeparator = null;

                    for (Item item : mItems) {
                        if (item.getId().equals(itemId)) {
                            newItem = item;

                            int index = mItems.indexOf(item);
                            if (index != 0) {
                                Item itemBefore = mItems.get(index - 1);
                                if (itemBefore.isSeparator()) {
                                    newSeparator = itemBefore;
                                }
                            }

                            break;
                        }
                    }

                    if (newItem != null) {
                        if (newSeparator != null) {
                            mAdapter.notifyItemInserted(newSeparator);
                        }

                        mAdapter.notifyItemInserted(newItem);
                        mLinearLayoutManager.scrollToPosition(newItem.getOrderNumber());
                    }
                }
            } else if (data.hasExtra(ItemFragment.EXTRA_DELETED_ITEM)) {

                UUID deletedItemUUID = (UUID) data.getSerializableExtra(ItemFragment.EXTRA_DELETED_ITEM);

                Item itemForDeleting = ItemLab.get(getActivity()).getItem(deletedItemUUID);

                for (Item item : mItems) {
                    if (item.getId().equals(deletedItemUUID)) {

                        int orderNumber = item.getOrderNumber();
                        mItems.remove(item);

                        ItemLab.get(getActivity()).deleteItemByUUID(deletedItemUUID);

                        mAdapter.notifyItemRemoved(orderNumber);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.list_items_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
//        if (menuItem.getItemId() == R.id.menu_item_new_crime) {
//            final Item item = new Item();
//            ItemLab.get(getActivity()).addItem(item);

        //!mRecyclerView.getAdapter().notifyItemInserted(mItems.indexOf(item));

        // NOTE: Left this code in for commentary. I believe this is what you would do
        // to wait until the new crime is added, then animate the selection of the new crime.
        // It does not work, though: the listener will be called immediately,
        // because no animations have been queued yet.
//                mRecyclerView.getItemAnimator().isRunning(
//                        new RecyclerView.ItemAnimator.ItemAnimatorFinishedListener() {
//                    @Override
//                    public void onAnimationsFinished() {
//                        selectCrime(crime);
//                    }
//                });
//            return true;
//        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.list_items_item_context_menu, menu);
    }

    //RecyclerView

    public static ItemsListFragment newInstance() {

        //!Bundle args = new Bundle();
        ItemsListFragment fragment = new ItemsListFragment();
        //!fragment.setArguments(args);

        return fragment;
    }

    private void selectItem(Item item) {
        // start an instance of CrimePagerActivity
//!        Intent i = new Intent(getActivity(), ItemActivity.class);
//        i.putExtra(ItemFragment.EXTRA_ITEM_ID, item.getId());
        Intent intent = new ItemPagerActivity().newIntent(getContext(), item.getId());
//            startActivity(intent);

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

    private class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {

        public static final int VIEW_TYPE_ITEM = 0;
        public static final int VIEW_TYPE_SEPARATOR = 1;


        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if (viewType == VIEW_TYPE_SEPARATOR) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.separator, parent, false);
                return new ItemHolder(view, viewType);
            }

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list, parent, false);
            return new ItemHolder(view, viewType);

        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {

            holder.itemView.setEnabled(true);

            Item item = mItems.get(position);
            holder.bindItem(item);

        }

        @Override
        public int getItemViewType(int position) {

            Item item = mItems.get(position);
            if (item.isSeparator()) {
                return VIEW_TYPE_SEPARATOR;
            }

            return VIEW_TYPE_ITEM;

        }

        //        @Override
//        public void onBindViewHolder(ItemHolder holder, int pos) {
//            Item item = mItems.get(pos);
//            holder.bindItem(item);
//            Log.d(TAG, "binding crime" + item + "at position" + pos);
//        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public void notifyItemInserted(Item item) {

            notifyItemInserted(item.getOrderNumber());
        }

        public void notifyItemRemoved(Item item) {

            notifyItemRemoved(item.getOrderNumber());
        }

    }

    private class ItemHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {

        private final TextView mTitleTextView;
        private final TextView mIdTextView;
        private Item mItem;

        public ItemHolder(View itemView, int viewType) {
            //!super(itemView, mMultiSelector);

            super(itemView);

            if (viewType == ItemAdapter.VIEW_TYPE_SEPARATOR) {

                mTitleTextView = (TextView) itemView.findViewById(R.id.separator_title);
                mIdTextView = null;

            } else {

                mTitleTextView = (TextView) itemView.findViewById(R.id.item_list_title_text_view);
                mIdTextView = (TextView) itemView.findViewById(R.id.item_list_id_text_view);

                itemView.setOnClickListener(this);
                //itemView.getId();
                itemView.setLongClickable(true);
                itemView.setOnLongClickListener(this);
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

        public void bindItem(Item item) {
            mItem = item;
            mTitleTextView.setText(item.getTitle());
            if (!item.isSeparator()) {
                mIdTextView.setText(item.getId().toString());
            }

            //!mSolvedCheckBox.setChecked(item.isSolved());
        }

        //
        @Override
        public void onClick(View v) {

            //TODO
            Snackbar.make(v, "onClick", Snackbar.LENGTH_SHORT).show();

            if (mItem == null) {
                return;
            }

            selectItem(mItem);

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
}
package com.zubrid.mystuff.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

import com.zubrid.mystuff.R;
import com.zubrid.mystuff.activity.ItemPagerActivity;
import com.zubrid.mystuff.lab.ItemStuffLab;
import com.zubrid.mystuff.model.ItemStuff;

import java.util.ArrayList;

public class ItemsListFragment extends Fragment {

    //var
    static final int INTENT_NEW_ITEM = 2;
    private static final String TAG = "ItemsListFragment";
    private ItemAdapter mAdapter;
    private RecyclerView mRecyclerView;
    //private MultiSelector mMultiSelector = new MultiSelector();
    private ArrayList<ItemStuff> mItemStuffs;
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
//                for (int i = mItemStuffs.size(); i >= 0; i--) {
//                    if (mMultiSelector.isSelected(i, 0)) {
//                        ItemStuff item = mItemStuffs.get(i);
//                        ItemStuffLab.get(getActivity()).deleteItem(item);
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
        mItemStuffs = ItemStuffLab.get(getActivity()).getItems();
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

        //! old code
        // if (requestCode == INTENT_NEW_ITEM) {

//        if (resultCode == Activity.RESULT_OK) {
//
//            if (data.hasExtra("changedItems")) {
//
//                mItemStuffs = ItemStuffLab.get(getActivity()).getItems();
//
//                ArrayList<UUID> changedItems = (ArrayList<UUID>) data.getSerializableExtra("changedItems");
//
//                for (UUID itemId : changedItems) {
//
//                    ItemStuff newItemStuff = null;
//                    ItemStuff newSeparator = null;
//
//                    ItemStuff changedItem = null;
//
//                    for (ItemStuff itemStuff : mItemStuffs) {
//                        if (itemStuff.getId().equals(itemId)) {
//
//                            changedItem = itemStuff;
//
//                            //int index = mItemStuffs.indexOf(itemStuff);
////                            if (index != 0) {
////                                ItemStuff itemStuffBefore = mItemStuffs.get(index - 1);
////                                if (itemStuffBefore.isSeparator()) {
////                                    newSeparator = itemStuffBefore;
////                                }
////                            }
//
//                            break;
//                        }
//                    }
//
//                    if (changedItem != null) {
//
//                        mAdapter.notifyItemChanged(changedItem.getOrderNumber());
//
//                    }
//
//
//                    if (newItemStuff != null) {
////                        if (newSeparator != null) {
////                            mAdapter.notifyItemInserted(newSeparator);
////                        }
//
//                        mAdapter.notifyItemInserted(newItemStuff);
//                        mLinearLayoutManager.scrollToPosition(newItemStuff.getOrderNumber());
//                    }
//                }
//
//            } else if (data.hasExtra(ItemFragment.EXTRA_DELETED_ITEM)) {
//
//                UUID deletedItemUUID = (UUID) data.getSerializableExtra(ItemFragment.EXTRA_DELETED_ITEM);
//
//                ItemStuff itemStuffForDeleting = ItemStuffLab.get(getActivity()).getItem(deletedItemUUID);
//
//                for (ItemStuff itemStuff : mItemStuffs) {
//                    if (itemStuff.getId().equals(deletedItemUUID)) {
//
//                        int orderNumber = itemStuff.getOrderNumber();
//                        mItemStuffs.remove(itemStuff);
//
//                        ItemStuffLab.get(getActivity()).moveItemToTrash(itemStuffForDeleting);
//
//                        //!mAdapter.notifyItemRemoved(orderNumber);
//                        break;
//                    }
//                }
//            }
//        }

        mItemStuffs = ItemStuffLab.get(getActivity()).getItems();
        mAdapter.updateList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.list_items_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
//        if (menuItem.getItemId() == R.id.menu_item_new_crime) {
//            final ItemStuff item = new ItemStuff();
//            ItemStuffLab.get(getActivity()).addItem(item);

        //!mRecyclerView.getAdapter().notifyItemInserted(mItemStuffs.indexOf(item));

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

    private void selectItem(ItemStuff itemStuff) {
        // start an instance of CrimePagerActivity
//!        Intent i = new Intent(getActivity(), ItemActivity.class);
//        i.putExtra(ItemFragment.EXTRA_ITEM_ID, itemStuff.getId());
        Intent intent = new ItemPagerActivity().newIntent(getContext(), itemStuff.getId());
//            startActivity(intent);

        startActivityForResult(intent, 0);

//!        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            // NOTE: shared element transition here.
//            // Support library fragments do not support the three parameter
//            // startActivityForResult call. So to get this to work, the entire
//            // project had to be shifted over to use stdlib fragments,
//            // and the v13 ViewPager.
//            int index = mItemStuffs.indexOf(itemStuff);
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

        public void updateList() {

            notifyDataSetChanged();
        }


        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {

            holder.itemView.setEnabled(true);

            ItemStuff itemStuff = mItemStuffs.get(position);

            boolean showSeparator = false;

            if (position == 0) {
                showSeparator = true;
            } else {
                ItemStuff itemStuffBefore = mItemStuffs.get(position - 1);

                if (!itemStuff.getTitleFirstLetter().equalsIgnoreCase(
                        itemStuffBefore.getTitleFirstLetter())) {
                    showSeparator = true;
                }
            }

            if (showSeparator) {

                holder.setSeparatorVisible();

            } else {
                holder.setSeparatorGone();

            }

            holder.bindItem(itemStuff);

        }

        @Override
        public int getItemViewType(int position) {

            ItemStuff itemStuff = mItemStuffs.get(position);
            if (itemStuff.isSeparator()) {
                return VIEW_TYPE_SEPARATOR;
            }

            return VIEW_TYPE_ITEM;

        }

        //        @Override
//        public void onBindViewHolder(ItemHolder holder, int pos) {
//            ItemStuff item = mItemStuffs.get(pos);
//            holder.bindItem(item);
//            Log.d(TAG, "binding crime" + item + "at position" + pos);
//        }

        @Override
        public int getItemCount() {
            return mItemStuffs.size();
        }

        public void notifyItemInserted(ItemStuff itemStuff) {

            notifyItemInserted(itemStuff.getOrderNumber());
        }

        public void notifyItemRemoved(ItemStuff itemStuff) {

            notifyItemRemoved(itemStuff.getOrderNumber());
        }

    }

    private class ItemHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {

        private final TextView mTitleTextView;
        private final TextView mSeparatorTextView;
        private final TextView mIdTextView;
        private ItemStuff mItemStuff;


        public ItemHolder(View itemView, int viewType) {
            //!super(itemView, mMultiSelector);

            super(itemView);

            if (viewType == ItemAdapter.VIEW_TYPE_SEPARATOR) {

                mTitleTextView = (TextView) itemView.findViewById(R.id.separator_title);
                mIdTextView = null;
                mSeparatorTextView = null;
            } else {

                mTitleTextView = (TextView) itemView.findViewById(R.id.item_list_title_text_view);
                mIdTextView = (TextView) itemView.findViewById(R.id.item_list_id_text_view);
                mSeparatorTextView = (TextView) itemView.findViewById(R.id.separator_text_view);


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

        public void bindItem(ItemStuff itemStuff) {

            mItemStuff = itemStuff;
            mTitleTextView.setText(itemStuff.getTitle());

            mSeparatorTextView.setText(itemStuff.getTitleFirstLetter().toUpperCase());

            mIdTextView.setText(itemStuff.getId().toString());


//            if (mSeparatorTextView != null) {
//                mSeparatorTextView.setVisibility(View.GONE);
//            }
//
//            if (itemStuff.getTitle().equalsIgnoreCase("test")) {
//                mSeparatorTextView.setVisibility(View.VISIBLE);
//            }
        }

        void setSeparatorGone() {

            if (mSeparatorTextView != null) {
                mSeparatorTextView.setVisibility(View.GONE);

            }

        }

        void setSeparatorVisible() {

            if (mSeparatorTextView != null) {
                mSeparatorTextView.setVisibility(View.VISIBLE);

            }

        }

        //
        @Override
        public void onClick(View v) {

            //Snackbar.make(v, "onClick", Snackbar.LENGTH_SHORT).show();

            if (mItemStuff == null) {
                return;
            }

            selectItem(mItemStuff);

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
}
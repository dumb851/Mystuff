package com.zubrid.mystuff.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zubrid.mystuff.R;
import com.zubrid.mystuff.fragment.ItemFragment;
import com.zubrid.mystuff.lab.ItemLab;
import com.zubrid.mystuff.model.ItemStuff;

import java.util.ArrayList;
import java.util.UUID;

public class ItemPagerActivity extends AppCompatActivity
    implements ItemFragment.ItemFragmentListener{

    private static final String TAG = "ItemPagerActivity_TAG";
    ViewPager mViewPager;
    FragmentStatePagerAdapter mPagerAdapter;
    ArrayList<ItemStuff> mItemStuffs;

    public static ActivityOptions getTransition(Activity activity, View crimeView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            crimeView.setTransitionName("item");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity,
                    crimeView, "item");

            return options;
        } else {
            return null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID itemId = (UUID) getIntent().getSerializableExtra(ItemFragment.EXTRA_ITEM_ID);

        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mViewPager.setTransitionName("item");
        }
        setContentView(mViewPager);

        if (itemId == null) {
            mItemStuffs = new ArrayList<>();
            mItemStuffs.add(new ItemStuff());
        } else {
            mItemStuffs = ItemLab.get(this).getItems();
        }

        FragmentManager fm = getSupportFragmentManager();
        mPagerAdapter = new FragmentStatePagerAdapter(fm) {
            @Override
            public int getCount() {
                return mItemStuffs.size();
            }

            @Override
            public Fragment getItem(int pos) {
                UUID itemId = mItemStuffs.get(pos).getId();
                return ItemFragment.newInstance(itemId);
            }


        };

        mViewPager.setAdapter(mPagerAdapter);

        for (int i = 0; i < mItemStuffs.size(); i++) {
            if (mItemStuffs.get(i).getId().equals(itemId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    public static Intent newIntent(Context packageContext, UUID itemId) {
        Intent intent = new Intent(packageContext, ItemPagerActivity.class);
        intent.putExtra(ItemFragment.EXTRA_ITEM_ID, itemId);
        return intent;
    }


    // Callback ItemFragment.ItemFragmentListener
    @Override
    public void addPage() {
        mItemStuffs.add(new ItemStuff());
        mPagerAdapter.notifyDataSetChanged();
    }
}



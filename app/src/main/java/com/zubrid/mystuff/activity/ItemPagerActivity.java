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

import com.zubrid.mystuff.lab.ItemLab;
import com.zubrid.mystuff.R;
import com.zubrid.mystuff.fragment.ItemFragment;
import com.zubrid.mystuff.model.Item;

import java.util.ArrayList;
import java.util.UUID;

public class ItemPagerActivity extends AppCompatActivity {
    ViewPager mViewPager;

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

        final ArrayList<Item> items;
        UUID itemId = (UUID) getIntent().getSerializableExtra(ItemFragment.EXTRA_ITEM_ID);

        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mViewPager.setTransitionName("item");
        }
        setContentView(mViewPager);

        //TODO here убрать сепараторы
        if (itemId == null) {
            items = new ArrayList<>();
            items.add(new Item());
        } else {
            items = ItemLab.get(this).getWithoutSeparatorsItems();
        }

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public int getCount() {
                return items.size();
            }

            @Override
            public Fragment getItem(int pos) {
                UUID itemId = items.get(pos).getId();
                return ItemFragment.newInstance(itemId);
            }
        });

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equals(itemId)) {
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

}


//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//
//import com.zubrid.mystuff.R;
//
//public class ItemActivity extends AppCompatActivity {
//
//    private static final String EXTRA_ITEM_ID =
//            "com.zubrid.mystuff.item_id";
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_fragment);
//
//        FragmentManager fm = getSupportFragmentManager();
//        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
//
//        if (fragment == null) {
//            fragment = createFragment();
//            fm.beginTransaction()
//                    .add(R.id.fragment_container, fragment)
//                    .commit();
//        }
//    }
//    protected Fragment createFragment() {
//
//        UUID itemId = (UUID) getIntent()
//                .getSerializableExtra(EXTRA_ITEM_ID);
//
//        return ItemFragment.newInstance(itemId);
//    }
//

//
//    public static ActivityOptions getTransition(Activity activity, View itemView) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            itemView.setTransitionName("item");
//
//            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity,
//                    itemView, "item");
//
//            return options;
//        } else {
//            return null;
//        }
//    }
//}

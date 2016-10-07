package com.zubrid.mystuff.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.zubrid.mystuff.lab.LabelLab;
import com.zubrid.mystuff.R;
import com.zubrid.mystuff.fragment.LabelFragment;
import com.zubrid.mystuff.model.Label;

import java.util.ArrayList;
import java.util.UUID;

public class LabelPagerActivity extends AppCompatActivity {

    ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ArrayList<Label> labels;
        UUID labelId = (UUID) getIntent().getSerializableExtra(LabelFragment.EXTRA_LABEL_ID);

        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mViewPager.setTransitionName("label");
        }
        setContentView(mViewPager);

        if (labelId == null) {
            labels = new ArrayList<>();
            labels.add(new Label());
        } else {
            labels = LabelLab.get(this).getLabels();
        }

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public int getCount() {
                return labels.size();
            }

            @Override
            public Fragment getItem(int pos) {
                UUID labelId = labels.get(pos).getId();
                return LabelFragment.newInstance(labelId);
            }
        });

        for (int i = 0; i < labels.size(); i++) {
            if (labels.get(i).getId().equals(labelId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    public static Intent newIntent(Context packageContext, UUID labelId) {
        Intent intent = new Intent(packageContext, LabelPagerActivity.class);
        intent.putExtra(LabelFragment.EXTRA_LABEL_ID, labelId);
        return intent;
    }

}
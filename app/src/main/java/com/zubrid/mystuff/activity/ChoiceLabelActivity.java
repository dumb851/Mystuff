package com.zubrid.mystuff.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.zubrid.mystuff.fragment.ChoiceLabelFragment;
import com.zubrid.mystuff.fragment.ItemFragment;

public class ChoiceLabelActivity extends SingleFragmentActivity{

    private ChoiceLabelFragment mFragment;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, ChoiceLabelActivity.class);
        //intent.putExtra(ItemFragment.EXTRA_ITEM_ID, itemId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        mFragment = ChoiceLabelFragment.newInstance();
        return mFragment;
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();

//        Bundle bundle = new Bundle();
//        bundle.putSerializable("changedItems", mFragment.getChangedLabels());
        intent.putExtra("changedItems", mFragment.getChangedLabels());

        setResult(Activity.RESULT_OK, intent);

        super.onBackPressed();

    }
}

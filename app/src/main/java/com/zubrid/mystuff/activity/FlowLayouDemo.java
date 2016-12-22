package com.zubrid.mystuff.activity;

/**
 * Created by dumb851 on 22.12.2016.
 */

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.zubrid.mystuff.R;
import com.zubrid.mystuff.utils.FlowLayout;

public class FlowLayouDemo extends AppCompatActivity {
    private TextView tvTitleBusiness;
    private FlowLayout flowBusiness;
    private TextView tvTitlePrivate;
    private FlowLayout flowPrivate;
    //private ArrayList<TagModel> arrayList;

    private void findViews() {
        tvTitleBusiness = (TextView) findViewById(R.id.tvTitleBusiness);
        flowBusiness = (FlowLayout) findViewById(R.id.flowBusiness);
        tvTitlePrivate = (TextView) findViewById(R.id.tvTitlePrivate);
        flowPrivate = (FlowLayout) findViewById(R.id.flowPrivate);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_layou_demo);
        findViews();
        addLayouts();
    }

    private void addLayouts() {
//        if (arrayList == null) {
//            arrayList = new ArrayList<>();
//        }
        flowBusiness.removeAllViews();
        flowPrivate.removeAllViews();
        for (int i = 0; i < 75; i++) {

            final boolean[] selected = {false};
            View view = this.getLayoutInflater().inflate(R.layout.text_view, null);
            final TextView textView = (TextView) view.findViewById(R.id.tvText);
            if (i % 5 == 0) {
                //arrayList.add(new TagModel(i, false, "Business VIEW : " + i));
                textView.setText("Busi VIEW To  IS : " + i);
            } else {
                //arrayList.add(new TagModel(i, false, "TEXT IS : " + i));
                textView.setText("Busi IS : " + i);
            }
            //textView.setBackgroundResource(R.drawable.unselected_tag);
            textView.setTextColor(Color.parseColor("#3F51B5"));
            textView.setTag(i);
            if(i<=50){
                flowBusiness.addView(view);
            }else {
                textView.setText("Priv View : "+i);
                flowPrivate.addView(view);
            }

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selected[0]) {
                        selected[0] = false;
                        //textView.setBackgroundResource(R.drawable.unselected_tag);
                        textView.setTextColor(Color.parseColor("#3F51B5"));
                    } else {
                        selected[0] = true;
                        //textView.setBackgroundResource(R.drawable.selected_tag);
                        textView.setTextColor(Color.parseColor("#FFFFFF"));
                    }
                }
            });

        }
    }
}
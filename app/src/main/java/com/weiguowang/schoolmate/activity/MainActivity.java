package com.weiguowang.schoolmate.activity;

import android.os.Bundle;
import android.view.View;

import com.weiguowang.schoolmate.R;
import com.weiguowang.schoolmate.TActivity;
import com.weiguowang.schoolmate.view.CircleMenuLayout;

public class MainActivity extends TActivity {

    private CircleMenuLayout mCircleMenuLayout;

    private String[] mItemTexts = new String[]{"安全中心 ", "特色服务", "投资理财","转账汇款", "我的账户", "信用卡", "安全中心 ", "特色服务", "投资理财",
            "转账汇款", "我的账户", "信用卡","15KM ", "特色服务", "投资理财"};
    private int[] mItemImgs = new int[]{R.mipmap.ic_launcher, R.mipmap.menu1,
            R.mipmap.menu1, R.mipmap.menu1, R.mipmap.menu1, R.mipmap.menu1, R.mipmap.menu1, R.mipmap.menu1,
            R.mipmap.menu1, R.mipmap.menu1, R.mipmap.ic_launcher, R.mipmap.menu1,R.mipmap.ic_launcher, R.mipmap.menu1, R.mipmap.menu1, R.mipmap.menu1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setEvent();
        testData();
    }

    private void testData() {

    }

    private void initView() {
        mCircleMenuLayout = (CircleMenuLayout) findViewById(R.id.id_menulayout);
        mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);
    }

    private void setEvent() {
        mCircleMenuLayout.setOnMenuItemClickListener(new CircleMenuLayout.OnMenuItemClickListener() {
            @Override
            public void itemClick(View view, int pos) {
                toastyInfo("Click "+mItemTexts[pos]);
            }

            @Override
            public void itemCenterClick(View view) {
                toastyInfo("Click Center");
            }
        });
    }

}

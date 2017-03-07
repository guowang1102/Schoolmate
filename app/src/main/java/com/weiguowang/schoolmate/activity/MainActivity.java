package com.weiguowang.schoolmate.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.weiguowang.schoolmate.R;
import com.weiguowang.schoolmate.TActivity;
import com.weiguowang.schoolmate.entity.Person;
import com.weiguowang.schoolmate.utils.Toasty;
import com.weiguowang.schoolmate.view.CircleMenuLayout;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends TActivity {

    private CircleMenuLayout mCircleMenuLayout;

    private String[] mItemTexts = new String[]{"安全中心 ", "特色服务", "投资理财",
            "转账汇款", "我的账户", "信用卡", "安全中心 ", "特色服务", "投资理财",
            "转账汇款", "我的账户", "信用卡","安全中心 ", "特色服务", "投资理财",};
    private int[] mItemImgs = new int[]{R.mipmap.menu1, R.mipmap.menu1,
            R.mipmap.menu1, R.mipmap.menu1, R.mipmap.menu1, R.mipmap.menu1, R.mipmap.menu1, R.mipmap.menu1,
            R.mipmap.menu1, R.mipmap.menu1, R.mipmap.menu1, R.mipmap.menu1,R.mipmap.menu1, R.mipmap.menu1, R.mipmap.menu1, R.mipmap.menu1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCircleMenuLayout = (CircleMenuLayout) findViewById(R.id.id_menulayout);
        mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);
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

        Person p2 = new Person();
        p2.setName("weiguowang");
        p2.setAddress("广东广州");
        p2.save(new SaveListener<String>() {
            @Override
            public void done(String objectId,BmobException e) {
                if(e==null){
                    toastyInfo("添加数据成功，返回objectId为："+objectId);
                }else{
                    toastyInfo("创建数据失败：" + e.getMessage());
                }
            }
        });
    }

}

package com.weiguowang.schoolmate.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.weiguowang.schoolmate.R;

import java.util.ArrayList;
import java.util.List;

/**
 * function:
 * Created by 韦国旺 on 2017/3/16 0016.
 * Copyright (c) 2017 北京联龙博通 All Rights Reserved.
 */
public class MenuItemAdapter extends BaseAdapter {

    //上下文
    private Context mContext;
    //菜单列表
    private List<String> mListData;
    private int selectedPos = -1;
    private String selectedText = "";
    private int normalDrawbleId;
    private Drawable selectedDrawble;
    private float textSize;
    private OnClickListener onClickListener;
    private OnItemClickListener mOnItemClickListener;

    public MenuItemAdapter(Context context, List<String> listData, int sId, int nId) {
        mContext = context;
        mListData = listData;
        selectedDrawble = mContext.getResources().getDrawable(sId);
        normalDrawbleId = nId;
        init();
    }

    private void init() {
        onClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPos = (Integer) view.getTag();
                setSelectedPosition(selectedPos);
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, selectedPos);
                }
            }
        };
    }

    /**
     * 设置选中的position,并通知刷新其它列表
     */
    public void setSelectedPosition(int pos) {
        if (mListData != null && pos < mListData.size()) {
            selectedPos = pos;
            selectedText = mListData.get(pos);
            notifyDataSetChanged();
        }
    }

    /**
     * 设置选中的position,但不通知刷新
     */
    public void setSelectedPositionNoNotify(int pos, ArrayList<String> list) {
        selectedPos = pos;
        mListData = list;
        if (mListData != null && pos < mListData.size()) {
            selectedText = mListData.get(pos);
        }
    }

    /**
     * 获取选中的position
     */
    public int getSelectedPosition() {

        if (mListData != null && selectedPos < mListData.size()) {
            return selectedPos;
        }

        return -1;
    }

    /**
     * 设置列表字体大小
     */
    public void setTextSize(float tSize) {
        textSize = tSize;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view;
        if (convertView == null) {
            view = (TextView) LayoutInflater.from(mContext).inflate(R.layout.choose_item, parent, false);
        } else {
            view = (TextView) convertView;
        }
        view.setTag(position);
//        String mString = "";
//        if (mListData != null) {
//            if (position < mListData.size()) {
//                mString = mListData.get(position);
//            }
//        }
//         view.setText(mString);

        //TODO 新增测试为什么无法正常显示

        String mString = mListData.get(position);
        view.setText(mString);
//        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

        if (selectedText != null && selectedText.equals(mString)) {
            view.setBackgroundDrawable(selectedDrawble);//设置选中的背景图
        } else {
            view.setBackgroundDrawable(mContext.getResources().getDrawable(normalDrawbleId));//设置未选中状态背景图
        }
        view.setPadding(20, 0, 0, 0);
        view.setOnClickListener(onClickListener);
        return view;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
    }

    /**
     * 重新定义菜单选项单击接口
     */
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}

package com.weiguowang.schoolmate.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.view.ViewGroup.LayoutParams;

import com.weiguowang.schoolmate.R;
import com.weiguowang.schoolmate.utils.Toasty;

import java.util.ArrayList;
import java.util.List;

/**
 * function: 列表选择
 * Created by 韦国旺 on 2017/3/16 0016.
 * Copyright (c) 2017 All Rights Reserved.
 */
public class ListPopupWindow extends PopupWindow {

    private View conentView;
    private Context context;
    private ArrayList<String> list;
    private OnSelectListener listener;
    private ListView mListView;
    private MenuItemAdapter adapter;


    public void setDatas(ArrayList<String> list){
        this.list = list;
    }

    public void setSelectListener(OnSelectListener listener) {
        this.listener = listener;
    }

    public ListPopupWindow(Context context,ArrayList<String> list){
        super(context);
        this.context = context;
        this.list = list;
        init();
    }

    private void init(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.layout_list_pop, null);
        mListView = (ListView) conentView.findViewById(R.id.listView);
        this.setContentView(conentView);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.MATCH_PARENT);

        adapter = new MenuItemAdapter(context, list,R.mipmap.choose_item_selected,R.drawable.choose_eara_item_selector);
        mListView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MenuItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Toasty.info(context,list.get(position)).show();
                if(listener!=null){
                    listener.getValue(list.get(position));
                }
                dismiss();
            }
        });

    }

    public interface OnSelectListener {
        void getValue(String value);
    }
}

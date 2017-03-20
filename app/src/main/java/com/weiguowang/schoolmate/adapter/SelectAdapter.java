package com.weiguowang.schoolmate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weiguowang.schoolmate.R;
import com.zhy.autolayout.utils.AutoUtils;
import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * function : 单列选择
 * Created by 韦国旺 on 2016/10/12.
 * Copyright (c) 2016 北京联龙博通 All Rights Reserved.
 */

public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> strList;
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public SelectAdapter(Context context, List<String> strList) {
        this.mContext = context;
        this.strList = strList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_select, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.txt = (TextView) view.findViewById(R.id.item_select_txt);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txt.setText(strList.get(position));
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return strList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
            AutoUtils.autoSize(arg0);
        }

        TextView txt;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


}

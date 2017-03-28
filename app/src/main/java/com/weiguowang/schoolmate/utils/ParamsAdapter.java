package com.weiguowang.schoolmate.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.wells.nettest.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wells on 2016/10/12.
 */

public class ParamsAdapter extends RecyclerView.Adapter<ParamsAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private final static int DEFAULT_ITEM_COUNT = 3;
    private List<ParamInfo> paramInfos;

    public ParamsAdapter(Context context) {
        this(context, DEFAULT_ITEM_COUNT);
    }

    public ParamsAdapter(Context context, int defaultItemCount) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        paramInfos = new ArrayList<>();
        for (int i = 0; i < defaultItemCount; i++) {
            paramInfos.add(new ParamInfo("", ""));
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_param, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.key = (EditText) view.findViewById(R.id.param_key);
        viewHolder.value = (EditText) view.findViewById(R.id.param_value);
        viewHolder.deleteBtn = (Button) view.findViewById(R.id.param_delete);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.key.setText(paramInfos.get(position).getKey());
        holder.value.setText(paramInfos.get(position).getValue());


        holder.key.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                paramInfos.get(holder.getLayoutPosition()).setKey(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        holder.value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                paramInfos.get(holder.getLayoutPosition()).setValue(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int modPosition= holder.getLayoutPosition();
                Log.v("info", "position is " + modPosition);
                paramInfos.remove(modPosition); //删除当前行
                notifyItemRemoved(modPosition);

                for(int i=0;i<paramInfos.size();i++){
                    Log.v("info", "数据 is " + paramInfos.get(i).toString());
                }
                if (modPosition != paramInfos.size()) { // 如果移除的是最后一个，忽略
                    notifyItemRangeChanged(modPosition, paramInfos.size() - modPosition);
                }
            }
        });

    }

    /**
     * 添加一项
     */
    public void addOne() {
        paramInfos.add(new ParamInfo("", ""));
        notifyItemInserted(paramInfos.size() - 1);
    }

    /**
     * 获取输入的值
     *
     * @return
     */
    public HashMap<String, String> getParams() {
        for (int i = 0; i < paramInfos.size(); i++) {
            Log.v("info", "data is " + paramInfos.get(i).toString());
        }

        HashMap<String, String> map = new HashMap<>();
        for (ParamInfo p : paramInfos) {
            if (!TextUtils.isEmpty(p.getKey())) {
                map.put(p.getKey(), p.getValue());
            }
        }

        return map;
    }

    @Override
    public int getItemCount() {
        return paramInfos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        EditText key;
        EditText value;
        Button deleteBtn;
        RelativeLayout mLayout;
    }

    class ParamInfo implements Serializable {

        private String key;
        private String value;

        public ParamInfo(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return "ParamInfo{" +
                    "key='" + key + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }


}
